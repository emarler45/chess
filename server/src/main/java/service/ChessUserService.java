package service;


import dataaccess.UserDAO;
import dataaccess.UserDAOMemory;
import model.UserData;

public class ChessUserService {

    private final UserDAO userdao = new UserDAOMemory();

    public CSR.RegisterResult registerUser(CSR.RegisterRequest r) throws AlreadyTakenException {
        String username = r.username();
        UserData foundData = userdao.getUserData(username);
        if (foundData != null) {
            throw new AlreadyTakenException("That username is already taken!");
        }
        userdao.insertUser(new UserData(username, r.password(), r.email()));
        return new CSR.RegisterResult("authToken", username);
    }
}
