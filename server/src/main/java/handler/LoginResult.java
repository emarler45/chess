package handler;

public record LoginResult(
        String username,
        String authToken
) {
}
