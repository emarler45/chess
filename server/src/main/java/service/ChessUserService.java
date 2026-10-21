package service;


import dataaccess.*;
import model.*;

import java.util.UUID;

public class ChessUserService {

    private final UserDAO userdao = new UserDAOMemory();
    private final AuthDAO authdao = new AuthDAOMemory();

    public RegisterResult registerUser(RegisterRequest r) throws AlreadyTakenException {
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

    public void logoutUser(LogoutRequest r){
        if (authdao.verify(r.authToken())) {
            authdao.deleteData(r.authToken());
        } else {
            throw new UnauthorizedException("Token not found.");
        }
    }
}
