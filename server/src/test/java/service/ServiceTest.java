package service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class ServiceTest {

    public ChessUserService user = new ChessUserService();

    public CSR.RegisterRequest makeRegisterReq(){
        return new CSR.RegisterRequest("username", "password", "email");
    }

    @Test
    public void returnsResult() throws AlreadyTakenException {
        CSR.RegisterRequest r = new CSR.RegisterRequest("username", "password", "email");
        CSR.RegisterResult result = user.registerUser(r);
        assertNotNull(result);
        assertInstanceOf(String.class, result.username());
        assertInstanceOf(String.class, result.authToken());
    }

    @Test
    public void returnsPassedUsername() throws AlreadyTakenException {
        String username = "elki";
        CSR.RegisterRequest r = new CSR.RegisterRequest(username, "password", "email");
        CSR.RegisterResult result = user.registerUser(r);
        assertEquals(username, result.username());
    }

    @Test
    @DisplayName("Throws for repeat username only")
    public void rejectsSameUsername() throws AlreadyTakenException {
        String username1 = "gamer";
        String username2 = "gaming";
        CSR.RegisterRequest r1 = new CSR.RegisterRequest(username1, "pw", "email1");
        CSR.RegisterRequest r2 = new CSR.RegisterRequest(username2, "pw", "email1");
        CSR.RegisterRequest r3 = new CSR.RegisterRequest(username2, "wp", "email2");
        CSR.RegisterResult result1 = user.registerUser(r1);
        CSR.RegisterResult result2 = user.registerUser(r2);
        assertThrows(AlreadyTakenException.class, () -> user.registerUser(r3));
    }

    @Test
    public void generatesAuthToken() throws AlreadyTakenException {
        CSR.RegisterRequest r = makeRegisterReq();
        CSR.RegisterResult result = user.registerUser(r);
        assertInstanceOf(UUID.class, UUID.fromString(result.authToken()));
    }

}
