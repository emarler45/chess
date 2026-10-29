package dataaccess;

import model.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AuthDAOdb implements AuthDAO {

    public AuthDAOdb() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void insertData(AuthData auth) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var insertStatement = conn.prepareStatement("INSERT INTO auth (token, username) VALUES (?, ?)")){
                insertStatement.setString(1, auth.authToken());
                insertStatement.setString(2, auth.username());
                insertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error inserting into database: %s".formatted(e.getMessage()));
        }

    }

    @Override
    public AuthData getAuthData(String authToken) {
        return null;
    }

    @Override
    public boolean verify(String token) {
        return false;
    }

    @Override
    public void deleteData(String token) {

    }

    @Override
    public void clearAll() {

    }

    public void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        DatabaseManager.createTables();
    }
}
