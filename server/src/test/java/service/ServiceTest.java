package service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;


public class ServiceTest {

    public ChessUserService user = new ChessUserService();

    @Test
    public void returnsResult() {
        CSR.RegisterRequest r = new CSR.RegisterRequest("username", "password", "email");
        CSR.RegisterResult result = user.registerUser(r);
        assertNotNull(result);
        assertInstanceOf(String.class, result.username());
        assertInstanceOf(String.class, result.authToken());
    }

    @Test
    public void returnsPassedUsername() {
        String username = "elki";
        CSR.RegisterRequest r = new CSR.RegisterRequest(username, "password", "email");
        CSR.RegisterResult result = user.registerUser(r);
        assertEquals(username, result.username());
    }

    @Test
    @DisplayName("Inserts Username into DB")
    public void insertsUser() {
        String username = "gamer";
        CSR.RegisterRequest r = new CSR.RegisterRequest(username, "pw", "email");
        CSR.RegisterResult result = user.registerUser(r);

    }
}
