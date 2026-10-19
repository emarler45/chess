package service;


import dataaccess.*;
import model.*;

import java.util.UUID;

public class ChessUserService {

    private final UserDAO userdao = new UserDAOMemory();
    private final AuthDAO authdao = new AuthDAOMemory();

    public CSR.RegisterResult registerUser(CSR.RegisterRequest r) throws AlreadyTakenException {
        String username = r.username();
        UserData foundData = userdao.getUserData(username);
        if (foundData != null) {
            throw new AlreadyTakenException("That username is already taken!");
        } else {
            userdao.insertUser(new UserData(username, r.password(), r.email()));
            AuthData auth = generateAuth(username);
            authdao.insertData(auth);
            return new CSR.RegisterResult(auth.authToken(), auth.username());
        }

    }

    private AuthData generateAuth(String name) {
        String authToken = UUID.randomUUID().toString();
        return new AuthData(authToken, name);
    }
}
