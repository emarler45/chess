package service;

import dataaccess.*;
import model.*;

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

    public void clearAll(){
        userdao.clearAll();
        gamesdao.clearAll();
        authdao.clearAll();
    }
}
