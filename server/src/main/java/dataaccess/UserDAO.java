package dataaccess;

import model.UserData;

public interface UserDAO {
    // Inserts a user into where the records are being kept
    void insertUser(UserData data) throws DataAccessException;
    // Retrieves the UserData for a given username
    UserData getUserData(String username) throws DataAccessException;
    boolean verifyPassword(String username, String password) throws DataAccessException;
    // removes all UserData instances
    void clearAll() throws DataAccessException;
}
