package service;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {

    private final ChessService clear = new ChessService();

    @Test
    public void clearsAllUsers() throws DataAccessException {
        clear.clearAll();
    }
}
