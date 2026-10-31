package dataaccess.sql;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import dataaccess.*;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
        assertThrows(IllegalArgumentException.class, () -> games.createGame("*"));
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

    @Test
    public void joinGameSuccess() throws Exception {
        int myID = games.createGame("newGame");
        GameData gData = new GameData("gamer", null, myID, "newGame", null);
        games.updateGame(gData);
        GameData updated = games.getGame(myID);
        assertEquals("gamer", updated.whiteUsername());
    }

    @Test
    public void updateGameObj() throws Exception {
        int myID = games.createGame("newGame");
        ChessGame updatedGame = new ChessGame();
        updatedGame.makeMove(new ChessMove(new ChessPosition(2,1), new ChessPosition(3, 1), null));
        GameData gData = new GameData(null, null, myID, null, updatedGame);
        games.updateGame(gData);
        GameData updated = games.getGame(myID);
        assertEquals(updatedGame, updated.game());
    }

    @Test
    public void failedUpdateGame() throws Exception {
        int myID = games.createGame("testGame");
        int badID = myID + 1;
        GameData gData = new GameData("gamer", null, badID, "newGame", null);
        assertThrows(DataNotFoundException.class, () -> games.updateGame(gData));
    }

    @Test
    public void listGamesSuccess() throws Exception {
        int id1 = games.createGame("myGame");
        int id2 = games.createGame("testGame");
        int id3 = games.createGame("lastGame");
        ArrayList<GameData> result = games.getGameList();
        assert(result.contains(games.getGame(id1)));
        assert(result.contains(games.getGame(id2)));
        assert(result.contains(games.getGame(id3)));
    }

    @Test
    public void listNoGames() throws Exception {
        ArrayList<GameData> result = games.getGameList();
        assertEquals(new ArrayList<>(), result);
    }

}
