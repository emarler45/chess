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
    public void clearDB() throws DataAccessException {
        authdao.clearAll();
    }

    @Test
    public void insertAuthData() throws DataAccessException {
        authdao.insertData(new AuthData("023e4567-e89b-12d3-a456-426614174005", "username"));
    }

    @Test
    public void failedGetData() throws Exception {
        String realToken = "023e4567-e89b-12d3-a456-426614174001";
        String fakeToken = "023e4567-e89b-12d3-a456-426614174005";
        // try to read when empty
        assertThrows(UnauthorizedException.class, () -> authdao.getAuthData(realToken));
        // insert data
        authdao.insertData(new AuthData(realToken, "username"));
        // use wrong token
        assertThrows(UnauthorizedException.class, () -> authdao.getAuthData(fakeToken));
    }

    @Test
    public void writeThenRead() throws Exception {
        String username = "username";
        String token = "023e4567-e89b-12d3-a456-426614174001";
        AuthData data = new AuthData(token, username);
        authdao.insertData(data);
        AuthData retrieved = authdao.getAuthData(token);
        assertEquals(token, retrieved.authToken());
        assertEquals(username, retrieved.username());
    }

    @Test
    public void verifyReturnsBool() throws Exception {
        String realToken = "023e4567-e89b-12d3-a456-426614174001";
        String fakeToken = "023e4567-e89b-12d3-a456-426614174003";
        AuthData data = new AuthData(realToken, "gamer");
        authdao.insertData(data);
        assert(authdao.verify(realToken));
        assertThrows(UnauthorizedException.class, () -> authdao.verify(fakeToken));
    }
    
    @Test
    public void successfulDelete() throws Exception {
        String realToken = "023e4567-e89b-12d3-a456-426614174001";
        AuthData data = new AuthData(realToken, "gamer");
        authdao.insertData(data);
        assert(authdao.verify(realToken));
        authdao.deleteData(realToken);
        assertThrows(UnauthorizedException.class, () -> authdao.getAuthData(realToken));
    }
    
    @Test
    public void rejectsBadInputs() throws Exception {
        String star = "*";
        String inject = "nothing); DROP TABLE auths";
        AuthData bad1 = new AuthData(star, "user");
        AuthData bad2 = new AuthData("023e4567-e89b-12d3-a456-426614174001", star);
        AuthData bad3 = new AuthData("023e4567-e89b-12d3-a456-426614174001", inject);
        assertThrows(IllegalArgumentException.class, () -> authdao.insertData(bad1));
        assertThrows(IllegalArgumentException.class, () -> authdao.insertData(bad2));
        assertThrows(IllegalArgumentException.class, () -> authdao.insertData(bad3));
        assertThrows(IllegalArgumentException.class, () -> authdao.getAuthData(star));
        assertThrows(IllegalArgumentException.class, () -> authdao.getAuthData(inject));
        assertThrows(IllegalArgumentException.class, () -> authdao.deleteData(star));
        assertThrows(IllegalArgumentException.class, () -> authdao.deleteData(inject));
    }

}
