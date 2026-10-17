package service;


public class ChessUserService extends ChessService{

    public RegisterResult registerUser(RegisterRequest r) {
        return new RegisterResult("gub", "username");
    }
}
