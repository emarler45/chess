package service;

import dataaccess.DataNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class UserServiceTest {

    public ChessService service = new ChessService();

    public RegisterRequest makeRegisterReq(){
        return new RegisterRequest("gaming", "password", "email");
    }

    @Test
    public void returnsResult() throws Exception {
        RegisterRequest r = new RegisterRequest("gaming", "password", "email");
        RegisterResult result = service.registerUser(r);
        assertNotNull(result);
        assertInstanceOf(String.class, result.username());
        assertInstanceOf(String.class, result.authToken());
    }

    @Test
    public void returnsPassedUsername() throws Exception {
        String username = "elki";
        RegisterRequest r = new RegisterRequest(username, "password", "email");
        RegisterResult result = service.registerUser(r);
        assertEquals(username, result.username());
    }

    @Test
    public void registerRejectsEmptyFields() {
        RegisterRequest r1 = new RegisterRequest("", "", "");
        RegisterRequest r2 = new RegisterRequest("username", "", "");
        RegisterRequest r3 = new RegisterRequest("username", "password", "");
        assertThrows(IllegalArgumentException.class, () -> service.registerUser(r1));
        assertThrows(IllegalArgumentException.class, () -> service.registerUser(r2));
        assertThrows(IllegalArgumentException.class, () -> service.registerUser(r3));
    }

    @Test
    @DisplayName("Throws for repeat username only")
    public void rejectsSameUsername() throws Exception {
        String username1 = "gamer";
        String username2 = "gaming";
        RegisterRequest r1 = new RegisterRequest(username1, "pw", "email1");
        RegisterRequest r2 = new RegisterRequest(username2, "pw", "email1");
        RegisterRequest r3 = new RegisterRequest(username2, "wp", "email2");
        RegisterResult result1 = service.registerUser(r1);
        RegisterResult result2 = service.registerUser(r2);
        assertThrows(AlreadyTakenException.class, () -> service.registerUser(r3));
    }

    @Test
    public void generatesAuthToken() throws Exception {
        RegisterRequest r = makeRegisterReq();
        RegisterResult result = service.registerUser(r);
        assertInstanceOf(UUID.class, UUID.fromString(result.authToken()));
    }

    @Test
    public void loginRejectsEmptyFields() throws Exception{
        LoginRequest r1 = new LoginRequest(null, null);
        LoginRequest r2 = new LoginRequest("username", null);
        assertThrows(IllegalArgumentException.class, () -> service.loginUser(r1));
        assertThrows(IllegalArgumentException.class, () -> service.loginUser(r2));
    }

    @Test
    public void attemptUserLogin() throws Exception {
        service.registerUser(new RegisterRequest("username", "pw", "email"));
        LoginRequest r = new LoginRequest("username", "pw");
        LoginResult result = service.loginUser(r);
        assertEquals("username", result.username());
        assertInstanceOf(UUID.class, UUID.fromString(result.authToken()));
    }

    @Test
    public void attemptLoginFakeUsername(){
        LoginRequest r = new LoginRequest("gaming", "pw");
        assertThrows(DataNotFoundException.class, () -> service.loginUser(r));
    }

    @Test
    public void attemptLoginWrongPassword() throws Exception {
        service.registerUser(new RegisterRequest("username", "password", "email"));
        LoginRequest r = new LoginRequest("username", "pass");
        assertThrows(UnauthorizedException.class, () -> service.loginUser(r));
    }

    @Test
    public void logoutUser() throws Exception{
        RegisterResult r = service.registerUser(new RegisterRequest("username", "pw", "email"));
        LogoutRequest logr = new LogoutRequest(r.authToken());
        service.logoutUser(logr);
    }

    @Test
    public void logoutBadToken() {
        LogoutRequest r = new LogoutRequest("123e4567-e89b-12d3-a456-426614174001");
        assertThrows(UnauthorizedException.class, () -> service.logoutUser(r));
    }

}
