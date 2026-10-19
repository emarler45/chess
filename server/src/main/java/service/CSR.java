package service;


// CSR stands for ChessServiceRecords. The signature has been abbreviated for convenience.
public class CSR {

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
