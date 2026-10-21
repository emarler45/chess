package service;

import dataaccess.DataNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class UserServiceTest {

    public ChessUserService user = new ChessUserService();

    public RegisterRequest makeRegisterReq(){
        return new RegisterRequest("gaming", "password", "email");
    }

    @Test
    public void returnsResult() throws AlreadyTakenException {
        RegisterRequest r = new RegisterRequest("gaming", "password", "email");
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

    @Test
    public void attemptUserLogin() throws Exception {
        LoginRequest r = new LoginRequest("username", "pw");
        LoginResult result = user.loginUser(r);
        assertEquals("username", result.username());
        assertInstanceOf(UUID.class, UUID.fromString(result.authToken()));
    }

    @Test
    public void attemptLoginFakeUsername(){
        LoginRequest r = new LoginRequest("gaming", "pw");
        assertThrows(DataNotFoundException.class, () -> user.loginUser(r));
    }

    @Test
    public void attemptLoginWrongPassword(){
        LoginRequest r = new LoginRequest("username", "pass");
        assertThrows(UnauthorizedException.class, () -> user.loginUser(r));
    }

    @Test
    public void logoutUser() throws UnauthorizedException{
        LogoutRequest r = new LogoutRequest("123e4567-e89b-12d3-a456-426614174000");
        user.logoutUser(r);
    }

    @Test
    public void logoutBadToken() {
        LogoutRequest r = new LogoutRequest("123e4567-e89b-12d3-a456-426614174001");
        assertThrows(UnauthorizedException.class, () -> user.logoutUser(r));
    }

}
