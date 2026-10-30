package passoff.server;

import dataaccess.AuthDAOdb;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.Test;

public class AuthDAODBTests {

    private final AuthDAOdb authdao = new AuthDAOdb();

    public AuthDAODBTests() throws DataAccessException {
    }

    @Test
    public void checkDBConfigured() {

    }

}
