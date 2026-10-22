package service;

import dataaccess.*;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ChessService {

    private UserDAO userdao;
    private GamesDAO gamesdao;
    private AuthDAO authdao;

    public ChessService() {
        UserDAO userdao = new UserDAOMemory();
        GamesDAO gamesdao = new GamesDAOMemory();
        AuthDAO authdao = new AuthDAOMemory();
    }

    public ChessService(UserDAO users, GamesDAO games, AuthDAO auths) {
        userdao = users;
        gamesdao = games;
        authdao = auths;
    }

    public RegisterResult registerUser(RegisterRequest r) throws Exception {
        if (r.username().isEmpty() || r.password().isEmpty() || r.email().isEmpty()){
            throw new IllegalArgumentException("All fields must be filled.");
        }
        String username = r.username();
        UserData foundData = userdao.getUserData(username);
        if (foundData != null) {
            throw new AlreadyTakenException("That username is already taken!");
        } else {
            userdao.insertUser(new UserData(username, r.password(), r.email()));
            AuthData auth = generateAuth(username);
            authdao.insertData(auth);
            return new RegisterResult(auth.authToken(), auth.username());
        }
    }

    private AuthData generateAuth(String name) {
        String authToken = UUID.randomUUID().toString();
        return new AuthData(authToken, name);
    }

    public LoginResult loginUser(LoginRequest r) throws Exception{
        if (r.username().isEmpty() || r.password().isEmpty()){
            throw new IllegalArgumentException("Username and password must not be empty.");
        }
        UserData foundData = userdao.getUserData(r.username());
        if (foundData == null){
            throw new DataNotFoundException("No user with name %s".formatted(r.username()));
        } else {
            if (foundData.password().equals(r.password())) {
                AuthData newAuth = generateAuth(r.username());
                authdao.insertData(newAuth);
                return new LoginResult(newAuth.username(), newAuth.authToken());
            } else {
                throw new UnauthorizedException("Invalid password");
            }
        }
    }

    public void logoutUser(LogoutRequest r) throws UnauthorizedException{
        if (authdao.verify(r.authToken())) {
            authdao.deleteData(r.authToken());
        } else {
            throw new UnauthorizedException("Token not found.");
        }
    }

    public CreateGameResult createGame(String token, CreateGameRequest r) throws UnauthorizedException{
        if (r.gameName().isEmpty()){
            throw new IllegalArgumentException("Game name must not be blank.");
        }
        int gameID = 0;
        if (authdao.verify(token)){
            do {
                gameID = ThreadLocalRandom.current().nextInt(1000, 10000);
            } while (gamesdao.getGame(gameID) != null);
            gamesdao.insertGame(gameID, r.gameName());
        } else {
            throw new UnauthorizedException("Invalid Token");
        }
        return new CreateGameResult(gameID);
    }

    public void joinGame(String authToken, JoinGameRequest r) throws Exception {
        ArrayList<String> colors = new ArrayList<>(List.of("WHITE", "BLACK"));
        if (authdao.verify(authToken)){
            if (!colors.contains(r.playerColor())){
                throw new IllegalArgumentException("Player color must be WHITE or BLACK.");
            }
            if (r.gameID() == 0){
                throw new IllegalArgumentException("Game ID cannot be empty.");
            }
            GameData targetGame = gamesdao.getGame(r.gameID());
            if (targetGame == null){
                throw new DataNotFoundException("No game with ID %d found.".formatted(r.gameID()));
            }
            AuthData userAuth = authdao.getAuthData(authToken);
            if (Objects.equals(r.playerColor(), "WHITE")){
                if (targetGame.whiteUsername() != null){
                    throw new AlreadyTakenException("WHITE is already taken.");
                } else {
                    gamesdao.updateGame(new GameData(
                            userAuth.username(), targetGame.blackUsername(), targetGame.gameID(),
                            targetGame.gameName(), targetGame.game()
                    ));
                }
            } else {
                if (targetGame.blackUsername() != null) {
                    throw new AlreadyTakenException("BLACK is already taken.");
                } else {
                    gamesdao.updateGame(new GameData(
                            targetGame.whiteUsername(), userAuth.username(), targetGame.gameID(),
                            targetGame.gameName(), targetGame.game()
                    ));
                }
            }
        } else {
            throw new UnauthorizedException("Invalid Token.");
        }
    }



    public void clearAll(){
        userdao.clearAll();
        gamesdao.clearAll();
        authdao.clearAll();
    }
}
