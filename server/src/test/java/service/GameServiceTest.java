package service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    private final ChessGameService service = new ChessGameService();

    @Test
    public void returnsGameID() throws UnauthorizedException {
        CreateGameRequest r = new CreateGameRequest("mygame");
        CreateGameResult result = service.createGame("123e4567-e89b-12d3-a456-426614174000", r);
        assertInstanceOf(int.class, result.gameID());
        assertNotEquals(0, result.gameID());
    }

    @Test
    public void attemptCreateUnauthorized() {
        String badAuthToken = "023e4567-e89b-12d3-a456-426614174001";
        CreateGameRequest r = new CreateGameRequest("mygame");
        assertThrows(UnauthorizedException.class, () -> service.createGame(badAuthToken, r));
    }

    @Test
    public void createRejectsEmptyName() {
        CreateGameRequest r = new CreateGameRequest("");
        assertThrows(IllegalArgumentException.class, () -> service.createGame("123e4567-e89b-12d3-a456-426614174000", r));
    }
}
