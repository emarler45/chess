package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;


public class UserDAOdb implements UserDAO {

    public UserDAOdb() throws DataAccessException {
        configureDatabase();
    }


    @Override
    public void insertUser(UserData data) throws DataAccessException {
        if (sanitized(data.username(), data.password(), data.email())){
            String hashedPass = BCrypt.hashpw(data.password(), BCrypt.gensalt());
            String statement = "INSERT INTO users VALUES (?, ?, ?)";
            try (var conn = DatabaseManager.getConnection()){
                try (var prepStatement = conn.prepareStatement(statement)){
                    prepStatement.setString(1, data.username());
                    prepStatement.setString(2, hashedPass);
                    prepStatement.setString(3, data.email());
                    prepStatement.executeUpdate();
                }
            } catch (SQLException sqle) {
                throw new DataAccessException("Failed to insert into database: %s".formatted(sqle.getMessage()));
            }
        }

    }

    @Override
    public UserData getUserData(String username) throws DataAccessException {
        if (sanitized(username, null, null)){
            String statement = "SELECT * FROM users WHERE username = ?";
            try (var conn = DatabaseManager.getConnection()) {
                try (var prepStatement = conn.prepareStatement(statement)) {
                    prepStatement.setString(1, username);
                    prepStatement.executeQuery();
                    try (ResultSet resSet = prepStatement.executeQuery()) {
                        if (resSet.next()){
                            String hashedPassword = resSet.getString("password");
                            String email = resSet.getString("email");
                            return new UserData(username, hashedPassword, email);
                        } else {
                            return null;
                        }
                    }
                }
            } catch (SQLException sqle) {
                throw new DataAccessException("Failed to update database: %s".formatted(sqle.getMessage()));
            }
        }
        return null;
    }

    @Override
    public boolean verifyPassword(String username, String password) throws DataAccessException {
        UserData foundData = getUserData(username);
        return BCrypt.checkpw(password, foundData.password());
    }


    @Override
    public void clearAll() throws DataAccessException {
        String statement = "TRUNCATE users";
        try (var conn = DatabaseManager.getConnection()) {
            try (var prepStatement = conn.prepareStatement(statement)) {
                prepStatement.executeUpdate();
            }
        } catch (SQLException sqle) {
            throw new DataAccessException("Failed to update database: %s".formatted(sqle.getMessage()));
        }

    }

    private boolean sanitized(String username, String password, String email) throws IllegalArgumentException {
        if (username != null){
            if (!username.matches("\\w+")) throw new IllegalArgumentException("Invalid Username.");
        }
        if (password != null){
            if (!password.matches("[\\w!@#$%^&*()+<>?]+")) throw new IllegalArgumentException("Invalid password.");
        }
        if (email != null){
            if (!email.matches("[\\w.]+@\\w+\\.\\w+")) throw new IllegalArgumentException("Invalid email.");
        }
        return true;
    }

    public void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        DatabaseManager.createTables();
    }
}
