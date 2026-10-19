package service;



public class ChessUserService {

    public CSR.RegisterResult registerUser(CSR.RegisterRequest r) {
        return new CSR.RegisterResult("gub", "username");
    }
}
