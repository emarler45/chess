package service;



public class ChessUserService {

    public ChessServiceRecords.RegisterResult registerUser(ChessServiceRecords.RegisterRequest r) {
        return new ChessServiceRecords.RegisterResult("gub", "username");
    }
}
