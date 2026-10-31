package service;

import dataaccess.AuthDAOMemory;
import dataaccess.DataAccessException;
import dataaccess.GamesDAOMemory;
import dataaccess.UserDAOMemory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {

    private final ChessService clear = new ChessService(new UserDAOMemory(), new GamesDAOMemory(), new AuthDAOMemory());

    public ClearServiceTest() throws DataAccessException {
    }

    @Test
    public void clearsAllUsers() throws DataAccessException {
        clear.clearAll();
    }
}
