package service;

import dataaccess.DataNotFoundException;
import model.GameData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    private final ChessService service = new ChessService();

    private String registerUser(String username) throws Exception {
        RegisterResult resp = service.registerUser(new RegisterRequest(username, "password", "email"));
        return resp.authToken();
    }

    @Test
    public void returnsGameID() throws Exception {
        String token = registerUser("username");
        CreateGameRequest r = new CreateGameRequest("mygame");
        CreateGameResult result = service.createGame(token, r);
        assertInstanceOf(Integer.class, result.gameID());
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

    public int joinAsBothColors() throws Exception{
        String token = registerUser("username");
        String token2 = registerUser("nameuser");
        CreateGameResult r = service.createGame(token, new CreateGameRequest("nameofgame"));
        service.joinGame(token, new JoinGameRequest("WHITE", r.gameID()));
        service.joinGame(token2, new JoinGameRequest("BLACK", r.gameID()));
        return r.gameID();
    }
    public int joinAsWhite() throws Exception{
        String token = registerUser("username");
        CreateGameResult r = service.createGame(token, new CreateGameRequest("nameofgame"));
        service.joinGame(token, new JoinGameRequest("WHITE", r.gameID()));
        return r.gameID();
    }

    @Test
    public void rejectsBadRequests() throws Exception{
        String token = registerUser("username");
        CreateGameResult r = service.createGame(token, new CreateGameRequest("nameofgame"));
        assertThrows(IllegalArgumentException.class, () -> service.joinGame(token, new JoinGameRequest("", r.gameID())));
        assertThrows(IllegalArgumentException.class, () -> service.joinGame(token, new JoinGameRequest("HI", r.gameID())));
        assertThrows(IllegalArgumentException.class, () -> service.joinGame(token, new JoinGameRequest("white", r.gameID())));
        assertThrows(IllegalArgumentException.class, () -> service.joinGame(token, new JoinGameRequest("WHITE", 0)));
        assertThrows(DataNotFoundException.class, () -> service.joinGame(token, new JoinGameRequest("BLACK", 1234)));
    }

    @Test
    public void rejectsTakenSlot() throws Exception{
        int gameID = joinAsBothColors();
        String token = registerUser("gamer");
        assertThrows(AlreadyTakenException.class, () -> service.joinGame(token, new JoinGameRequest("WHITE", gameID)));
        assertThrows(AlreadyTakenException.class, () -> service.joinGame(token, new JoinGameRequest("BLACK", gameID)));
    }

    @Test
    public void joinRejectsBadToken() throws Exception {
        int gameID = joinAsWhite();
        assertThrows(UnauthorizedException.class, () -> service.joinGame("bad token", new JoinGameRequest("BLACK", gameID)));
    }

    @Test
    public void listGamesNoError() throws Exception {
        String token = registerUser("username");
        service.listGames(token);
    }

    @Test
    public void listRejectsBadToken() throws Exception {
        String token = registerUser("username");
        service.listGames(token);
        assertThrows(UnauthorizedException.class, () -> service.listGames("badtoken"));
    }

}
