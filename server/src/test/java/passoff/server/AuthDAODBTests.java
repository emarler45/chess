package passoff.server;

import dataaccess.AuthDAOdb;
import dataaccess.DataAccessException;
import jdk.jshell.spi.ExecutionControlProvider;
import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import service.UnauthorizedException;

import static org.junit.jupiter.api.Assertions.*;

public class AuthDAODBTests {

    private final AuthDAOdb authdao = new AuthDAOdb();

    public AuthDAODBTests() throws DataAccessException {
    }

    @AfterEach
    public void clearDB() {
        authdao.clearAll();
    }

    @Test
    public void insertAuthData() throws DataAccessException {
        authdao.insertData(new AuthData("fakeToken", "username"));
    }

    @Test
    public void failedGetData() throws Exception {
        // try to read when empty
        assertThrows(UnauthorizedException.class, () -> authdao.getAuthData("realToken"));
        // insert data
        authdao.insertData(new AuthData("realToken", "username"));
        // use wrong token
        assertThrows(UnauthorizedException.class, () -> authdao.getAuthData("fakeToken"));
    }

    @Test
    public void writeThenRead() throws Exception {
        String username = "username";
        String token = "realAuthToken";
        AuthData data = new AuthData(token, username);
        authdao.insertData(data);
        AuthData retrieved = authdao.getAuthData(token);
        assertEquals(token, retrieved.authToken());
        assertEquals(username, retrieved.username());
    }

    @Test
    public void verifyReturnsBool() throws Exception {
        AuthData data = new AuthData("realToken", "gamer");
        authdao.insertData(data);
        assert(authdao.verify("realToken"));
        assertFalse(authdao.verify("fakeToken"));
    }

}
