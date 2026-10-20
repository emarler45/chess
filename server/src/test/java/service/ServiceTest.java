package service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class ServiceTest {

    public ChessUserService user = new ChessUserService();

    public RegisterRequest makeRegisterReq(){
        return new RegisterRequest("username", "password", "email");
    }

    @Test
    public void returnsResult() throws AlreadyTakenException {
        RegisterRequest r = new RegisterRequest("username", "password", "email");
        RegisterResult result = user.registerUser(r);
        assertNotNull(result);
        assertInstanceOf(String.class, result.username());
        assertInstanceOf(String.class, result.authToken());
    }

    @Test
    public void returnsPassedUsername() throws AlreadyTakenException {
        String username = "elki";
        RegisterRequest r = new RegisterRequest(username, "password", "email");
        RegisterResult result = user.registerUser(r);
        assertEquals(username, result.username());
    }

    @Test
    @DisplayName("Throws for repeat username only")
    public void rejectsSameUsername() throws AlreadyTakenException {
        String username1 = "gamer";
        String username2 = "gaming";
        RegisterRequest r1 = new RegisterRequest(username1, "pw", "email1");
        RegisterRequest r2 = new RegisterRequest(username2, "pw", "email1");
        RegisterRequest r3 = new RegisterRequest(username2, "wp", "email2");
        RegisterResult result1 = user.registerUser(r1);
        RegisterResult result2 = user.registerUser(r2);
        assertThrows(AlreadyTakenException.class, () -> user.registerUser(r3));
    }

    @Test
    public void generatesAuthToken() throws AlreadyTakenException {
        RegisterRequest r = makeRegisterReq();
        RegisterResult result = user.registerUser(r);
        assertInstanceOf(UUID.class, UUID.fromString(result.authToken()));
    }

}
