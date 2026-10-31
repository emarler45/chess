package service;

import dataaccess.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    private final ChessService servicemem = new ChessService(new UserDAOMemory(), new GamesDAOMemory(), new AuthDAOMemory());

    public GameServiceTest() throws DataAccessException {
    }

    private String registerUser(String username) throws Exception {
        RegisterResult resp = servicemem.registerUser(new RegisterRequest(username, "password", "email"));
        return resp.authToken();
    }

    @Test
    public void returnsGameID() throws Exception {
        String token = registerUser("username");
        CreateGameRequest r = new CreateGameRequest("mygame");
        CreateGameResult result = servicemem.createGame(token, r);
        assertInstanceOf(Integer.class, result.gameID());
        assertNotEquals(0, result.gameID());
    }

    @Test
    public void attemptCreateUnauthorized() {
        String badAuthToken = "023e4567-e89b-12d3-a456-426614174001";
        CreateGameRequest r = new CreateGameRequest("mygame");
        assertThrows(UnauthorizedException.class, () -> servicemem.createGame(badAuthToken, r));
    }

    @Test
    public void createRejectsEmptyName() throws Exception {
        String token = registerUser("hilol");
        CreateGameRequest r = new CreateGameRequest("");
        assertThrows(IllegalArgumentException.class, () -> servicemem.createGame(token, r));
    }

    public int joinAsBothColors() throws Exception{
        String token = registerUser("username");
        String token2 = registerUser("nameuser");
        CreateGameResult r = servicemem.createGame(token, new CreateGameRequest("nameofgame"));
        servicemem.joinGame(token, new JoinGameRequest("WHITE", r.gameID()));
        servicemem.joinGame(token2, new JoinGameRequest("BLACK", r.gameID()));
        return r.gameID();
    }
    public int joinAsWhite() throws Exception{
        String token = registerUser("username");
        CreateGameResult r = servicemem.createGame(token, new CreateGameRequest("nameofgame"));
        servicemem.joinGame(token, new JoinGameRequest("WHITE", r.gameID()));
        return r.gameID();
    }

    @Test
    public void rejectsBadRequests() throws Exception{
        String token = registerUser("username");
        CreateGameResult r = servicemem.createGame(token, new CreateGameRequest("nameofgame"));
        assertThrows(IllegalArgumentException.class, () -> servicemem.joinGame(token, new JoinGameRequest("", r.gameID())));
        assertThrows(IllegalArgumentException.class, () -> servicemem.joinGame(token, new JoinGameRequest("HI", r.gameID())));
        assertThrows(IllegalArgumentException.class, () -> servicemem.joinGame(token, new JoinGameRequest("white", r.gameID())));
        assertThrows(IllegalArgumentException.class, () -> servicemem.joinGame(token, new JoinGameRequest("WHITE", 0)));
        assertThrows(DataNotFoundException.class, () -> servicemem.joinGame(token, new JoinGameRequest("BLACK", 1234)));
    }

    @Test
    public void rejectsTakenSlot() throws Exception{
        int gameID = joinAsBothColors();
        String token = registerUser("gamer");
        assertThrows(AlreadyTakenException.class, () -> servicemem.joinGame(token, new JoinGameRequest("WHITE", gameID)));
        assertThrows(AlreadyTakenException.class, () -> servicemem.joinGame(token, new JoinGameRequest("BLACK", gameID)));
    }

    @Test
    public void joinRejectsBadToken() throws Exception {
        int gameID = joinAsWhite();
        assertThrows(UnauthorizedException.class, () -> servicemem.joinGame("bad token", new JoinGameRequest("BLACK", gameID)));
    }

    @Test
    public void listGamesNoError() throws Exception {
        String token = registerUser("gamer");
        int gameID = joinAsWhite();
        ListGamesResult resp = new ListGamesResult(servicemem.listGames(token));
        GameInfo expInfo = new GameInfo(gameID, "username", null, "nameofgame");
        ArrayList<GameInfo> expList = new ArrayList<>();
        expList.add(expInfo);
        ListGamesResult exp = new ListGamesResult(expList);
        assertEquals(exp, resp);
    }

    @Test
    public void listRejectsBadToken() throws Exception {
        String token = registerUser("username");
        servicemem.listGames(token);
        assertThrows(UnauthorizedException.class, () -> servicemem.listGames("badtoken"));
    }

}
