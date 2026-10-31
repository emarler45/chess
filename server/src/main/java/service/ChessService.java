package service;

import dataaccess.*;
import dataaccess.sql.AuthDAOdb;
import dataaccess.sql.GamesDAOdb;
import dataaccess.sql.UserDAOdb;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ChessService {

    private UserDAO userdao;
    private GamesDAO gamesdao;
    private AuthDAO authdao;

    public ChessService() throws DataAccessException {
        authdao = new AuthDAOdb();
        gamesdao = new GamesDAOdb();
        userdao = new UserDAOdb();
    }

    public ChessService(UserDAO u, GamesDAO g, AuthDAO a){
        userdao = u;
        gamesdao = g;
        authdao = a;
    }


    public RegisterResult registerUser(RegisterRequest r) throws Exception {
        if (r.username() == null || r.password() == null || r.email() == null
            || r.username().isEmpty() || r.password().isEmpty() || r.email().isEmpty()){
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
        if (r.username() == null || r.password() == null){
            throw new IllegalArgumentException("Username and password must not be empty.");
        }
        UserData foundData = userdao.getUserData(r.username());
        if (foundData == null){
            throw new UnauthorizedException("No user with name %s".formatted(r.username()));
        } else {
            if (userdao.verifyPassword(r.username(), r.password())) {
                AuthData newAuth = generateAuth(r.username());
                authdao.insertData(newAuth);
                return new LoginResult(newAuth.username(), newAuth.authToken());
            } else {
                throw new UnauthorizedException("Invalid password");
            }
        }
    }

    public void logoutUser(String token) throws Exception {
        if (authdao.verify(token)) {
            authdao.deleteData(token);
        } else {
            throw new UnauthorizedException("Token not found.");
        }
    }

    public CreateGameResult createGame(String token, CreateGameRequest r) throws Exception {
        if (authdao.verify(token)) {
            if (r.gameName() == null || r.gameName().isEmpty()) {
                throw new IllegalArgumentException("Game name must not be blank.");
            }
            int gameID = gamesdao.createGame(r.gameName());
            return new CreateGameResult(gameID);
        } else {
            throw new UnauthorizedException("Invalid token");
        }
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

    public ArrayList<GameInfo> listGames(String token) throws Exception {
        if (authdao.verify(token)){
            ArrayList<GameData> gamesList = gamesdao.getGameList();
            ArrayList<GameInfo> gameInfoList = new ArrayList<>();
            for (GameData game : gamesList){
//                String whiteUser = (game.whiteUsername() == null) ? "null" : game.whiteUsername();
//                String blackUser = (game.blackUsername() == null) ? "null" : game.blackUsername();
//                gameInfoList.add(new GameInfo(game.gameID(), whiteUser, blackUser, game.gameName()));
                gameInfoList.add(new GameInfo(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName()));

            }
            return gameInfoList;
        } else {
            throw new UnauthorizedException("Invalid Token.");
        }
    }

    public void clearAll() throws DataAccessException {
        userdao.clearAll();
        gamesdao.clearAll();
        authdao.clearAll();
    }
}
