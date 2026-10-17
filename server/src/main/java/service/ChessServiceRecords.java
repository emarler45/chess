package service;

public class ChessServiceRecords {

    public record RegisterRequest(
            String username,
            String password,
            String email
    ){}

    public record RegisterResult(
            String authToken,
            String username
    ){}


}
