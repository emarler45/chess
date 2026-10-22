package handler;

public record RegisterResult(
        String authToken,
        String username
){}