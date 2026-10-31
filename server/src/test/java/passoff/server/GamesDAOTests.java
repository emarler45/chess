package passoff.server;

import dataaccess.*;
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


}
