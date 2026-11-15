package client;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import server.ResponseException;
import server.Server;
import server.ServerFacade;
import service.ListGamesResult;
import service.LoginResult;
import service.RegisterResult;

import java.lang.module.ResolutionException;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade("http://localhost:" + port);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @AfterEach
    void clearData() {
        facade.delete();
    }


    @Test
    public void registerSuccessfully() {
        RegisterResult r = facade.register("username", "pw", "example@email.com");
        assertEquals("username", r.username());
    }

    @Test
    public void failRegister(){
        assertThrows(ResponseException.class, () -> facade.register("user", "pw", "email"));
    }

    @Test
    public void successfullyLogin() {
        facade.register("thisUser", "p", "my.email@hotmail.com");
        assertDoesNotThrow(() -> facade.login("thisUser", "p"));
    }

    @Test
    public void failLogin() {
        facade.register("thisUser", "p", "my.email@hotmail.com");
        assertThrows(ResponseException.class, () -> facade.login("someOtherUser", "p"));
        assertThrows(ResponseException.class, () -> facade.login("thisUser", "password"));
    }

    @Test
    public void successfullyLogout() {
        facade.register("thisUser", "p", "my.email@hotmail.com");
        LoginResult r = facade.login("thisUser", "p");
        assertDoesNotThrow(() -> facade.logout(r.authToken()));
    }

    @Test
    public void failLogout() {
        assertThrows(ResponseException.class, () -> facade.logout("badToken"));
        facade.register("username", "pass", "email.e@email.com");
        assertThrows(ResponseException.class, () -> facade.logout("badToken"));
        facade.login("username", "pass");
        assertThrows(ResponseException.class, () -> facade.logout("badToken"));
    }

    @Test
    public void successfullyListGames() {
        facade.register("username", "pass", "email.e@email.com");
        LoginResult r =  facade.login("username", "pass");
        assertDoesNotThrow(() -> facade.listGames(r.authToken()));
    }

    @Test
    public void failListGames() {
        assertThrows(ResponseException.class,() -> facade.listGames("someToken"));
        facade.register("username", "pass", "email.e@email.com");
        LoginResult r =  facade.login("username", "pass");
        assertThrows(ResponseException.class,() -> facade.listGames("someToken"));
    }

    @Test
    public void successfullyCreateAndListGame() {
        facade.register("username", "pass", "email.e@email.com");
        LoginResult r =  facade.login("username", "pass");
        String token = r.authToken();
        assertDoesNotThrow(() -> facade.createGame(token, "newGame"));
        ListGamesResult list = facade.listGames(token);
        String retrieved = list.games().getFirst().gameName();
        assertEquals("newGame", retrieved);
    }

    @Test
    public void failCreateGame(){
        facade.register("username", "pass", "email.e@email.com");
        LoginResult r =  facade.login("username", "pass");
        String token = r.authToken();
        assertThrows(ResponseException.class, () -> facade.createGame("fakeToken", "gamename"));
        ListGamesResult list = facade.listGames(token);
        assert(list.games().isEmpty());
    }

    @Test
    public void successfullyJoinGame(){
        facade.register("username", "pass", "email.e@email.com");
        facade.register("gamer", "pass", "other@email.com");
        LoginResult r1 =  facade.login("username", "pass");
        LoginResult r2 =  facade.login("gamer", "pass");
        String token1 = r1.authToken();
        String token2 = r2.authToken();
        int gameID = facade.createGame(token1, "test").gameID();
        assertDoesNotThrow(() -> facade.joinGame(token1, gameID, "WHITE"));
        assertDoesNotThrow(() -> facade.joinGame(token2, gameID, "BLACK"));
    }

    @Test
    public void failJoinGame() {
        facade.register("username", "pass", "email.e@email.com");
        facade.register("gamer", "pass", "other@email.com");
        LoginResult r1 =  facade.login("username", "pass");
        LoginResult r2 =  facade.login("gamer", "pass");
        String token1 = r1.authToken();
        String token2 = r2.authToken();
        int gameID = facade.createGame(token1, "test").gameID();
        assertThrows(ResponseException.class, () -> facade.joinGame("badtoken", gameID, "WHITE"));
        assertThrows(ResponseException.class, () -> facade.joinGame(token1, gameID+1, "WHITE"));
        assertThrows(ResponseException.class, () -> facade.joinGame(token2, gameID, "YELLOW"));
    }

    @Test
    public void sucessfullyClearData() {
        facade.register("gamer", "pass", "gamer@gmail.com");
        facade.delete();
        assertThrows(ResponseException.class, () -> facade.login("gamer", "pass"));
    }

}
