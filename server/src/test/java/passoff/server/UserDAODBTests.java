package passoff.server;

import dataaccess.*;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAODBTests {

    private final UserDAO users = new UserDAOdb();

    public UserDAODBTests() throws DataAccessException {
    }


    public void insertNewUser() throws DataAccessException {
        UserData newuser = new UserData("username", "pw", "me@email.com");
        users.insertUser(newuser);
    }

    @AfterEach
    public void clear() throws DataAccessException {
        users.clearAll();
    }

    @Test
    public void successfulInsert() throws DataAccessException {
        insertNewUser();
    }

    @Test
    public void badInserts() throws DataAccessException {
        UserData baduser1 = new UserData("*", "pw", "me@email.com");
        UserData baduser2 = new UserData("username", "\\n", "me@email.com");
        UserData baduser3 = new UserData("username", "pw", "myemail.com");
        assertThrows(IllegalArgumentException.class, () -> users.insertUser(baduser1));
        assertThrows(IllegalArgumentException.class, () -> users.insertUser(baduser2));
        assertThrows(IllegalArgumentException.class, () -> users.insertUser(baduser3));
    }

    @Test
    public void successfulGetUser() throws DataAccessException {
        insertNewUser();
        UserData foundData = users.getUserData("username");
        assertEquals("username", foundData.username());
        assertEquals("me@email.com", foundData.email());
    }

    @Test
    public void passwordVerify() throws DataAccessException {
        insertNewUser();
        assert(users.verifyPassword("username", "pw"));
        assertFalse(users.verifyPassword("username", "password"));
    }


    @Test
    public void failedGetUser() throws DataAccessException {
        assertNull(users.getUserData("username"));
        insertNewUser();
        assertNull(users.getUserData("user"));
    }

    @Test
    public void successfulClear() throws DataAccessException {
        insertNewUser();
        users.getUserData("username");
        users.clearAll();
        assertNull(users.getUserData("username"));
    }
}
