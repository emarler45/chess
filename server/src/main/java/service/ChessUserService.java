package service;


import dataaccess.UserDAOMemory;
import model.UserData;

public class ChessUserService {

    private final UserDAOMemory userdao = new UserDAOMemory();

    public CSR.RegisterResult registerUser(CSR.RegisterRequest r) {
        String username = r.username();
        userdao.insertUser(new UserData(username, r.password(), r.email()));
//        UserData foundData = UserDAO.getUserData(username);
        return new CSR.RegisterResult("authToken", username);
    }
}
