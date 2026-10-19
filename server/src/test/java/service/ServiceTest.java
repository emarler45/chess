package service;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;


public class ServiceTest {

    public ChessUserService user = new ChessUserService();

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
    public void rejectsSameUsername() throws AlreadyTakenException {
        String username1 = "gamer";
        String username2 = "gamer";
        CSR.RegisterRequest r1 = new CSR.RegisterRequest(username1, "pw", "email1");
        CSR.RegisterRequest r2 = new CSR.RegisterRequest(username2, "wp", "email2");
        CSR.RegisterResult result1 = user.registerUser(r1);
        assertThrows(AlreadyTakenException.class, () -> user.registerUser(r2));
    }

}
