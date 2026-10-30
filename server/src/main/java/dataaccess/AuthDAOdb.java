package dataaccess;

import model.AuthData;
import service.UnauthorizedException;

import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AuthDAOdb implements AuthDAO {

    public AuthDAOdb() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void insertData(AuthData auth) throws DataAccessException {
        if (sanitized(auth.authToken(), auth.username())){
            String statement = "INSERT INTO auths (token, username) VALUES (?, ?)";
            executeStatement(statement, auth.authToken(), auth.username());
        }
    }

    @Override
    public AuthData getAuthData(String authToken) throws Exception {
        if (!sanitized(authToken, null)) throw new IllegalArgumentException("Invalid Token.");
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT * FROM auths WHERE token = ?";
            try (var prepStatement = conn.prepareStatement(statement)){
                prepStatement.setString(1, authToken);
                try (ResultSet resSet = prepStatement.executeQuery()) {
                    if (resSet.next()){
                        String username = resSet.getString("username");
                        return new AuthData(authToken, username);
                    } else {
                        throw new DataNotFoundException("Invalid token.");
                    }
                }
            }
        } catch (SQLException sqle) {
            throw new DataAccessException("Error retrieving from database.");
        }
    }

    @Override
    public boolean verify(String token) throws Exception {
        try {
            AuthData found = getAuthData(token);
            return true;
        } catch (DataNotFoundException e) {
            throw new UnauthorizedException("Invalid Token.");
        }
    }

    @Override
    public void deleteData(String token) throws DataAccessException {
        if (sanitized(token, null)){
            String statement = "DELETE FROM auths WHERE token = ?";
            executeStatement(statement, token);
        }
    }

    @Override
    public void clearAll() throws DataAccessException {
        String statement = "TRUNCATE auths";
        executeStatement(statement);
    }

    private boolean sanitized(String token, String username) throws IllegalArgumentException {
        if (token != null){
            if (!token.matches("([a-z0-9]+-?)+")) throw new IllegalArgumentException("Invalid Token.");
        }
        if (username != null){
            if (!username.matches("\\w+")) throw new IllegalArgumentException("Invalid Username.");
        }
        return true;
    }

    public void executeStatement(String statement, String... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var prepStatement = conn.prepareStatement(statement)) {
                for (int i = 0; i < params.length; i++){
                    String param = params[i];
                    prepStatement.setString(i+1, param);
                }
                prepStatement.executeUpdate();
            }
        } catch (SQLException sqle){
            throw new DataAccessException("Unable to update database: %s".formatted(sqle.getMessage()));
        }
    }

    public void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        DatabaseManager.createTables();
    }
}
