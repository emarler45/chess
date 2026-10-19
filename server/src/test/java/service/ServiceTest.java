package service;

import org.junit.jupiter.api.Test;
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
}
