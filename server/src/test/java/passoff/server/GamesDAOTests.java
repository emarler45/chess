package passoff.server;

import chess.ChessGame;
import dataaccess.*;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GamesDAOTests {

    private final GamesDAO games = new GamesDAOdb();

    public GamesDAOTests() throws DataAccessException {

    }

    @AfterEach
    public void clearAll() throws DataAccessException {
        games.clearAll();
    }


    @Test
    public void successfullyCreateGame() throws Exception {
        games.createGame("gameName");
    }

    @Test
    public void failCreateGame() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> games.createGame("*****"));
    }

    @Test
    public void writeThenReadGame() throws Exception {
        int myID = games.createGame("gameName");
        GameData found = games.getGame(myID);
        assertEquals("gameName", found.gameName());
        assertInstanceOf(Integer.class, found.gameID());
        assertNull(found.blackUsername());
        assertNull(found.whiteUsername());
        ChessGame newGame = new ChessGame();
        assertEquals(newGame, found.game());
    }

    @Test
    public void failReadGame() throws Exception {
        int myID = games.createGame("ThisGame");
        int badID = myID + 1;
        assertNull(games.getGame(badID));
    }

}
