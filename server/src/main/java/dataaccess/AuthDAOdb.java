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
        String statement = "INSERT INTO auths (token, username) VALUES (?, ?)";
        executeStatement(statement, auth.authToken(), auth.username());
    }

    @Override
    public AuthData getAuthData(String authToken) throws Exception {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT * FROM auths WHERE token = ?";
            try (var prepStatement = conn.prepareStatement(statement)){
                prepStatement.setString(1, authToken);
                try (ResultSet resSet = prepStatement.executeQuery()) {
                    if (resSet.next()){
                        String username = resSet.getString("username");
                        return new AuthData(authToken, username);
                    } else {
                        throw new UnauthorizedException("Invalid token.");
                    }
                }
            }
        } catch (SQLException sqle) {
            throw new DataAccessException("Error retrieving from database.");
        }
    }

    @Override
    public boolean verify(String token) throws Exception {
        return getAuthData(token) != null;
    }

    @Override
    public void deleteData(String token) {

    }

    @Override
    public void clearAll() {


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
