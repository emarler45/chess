package dataaccess;

import model.UserData;

public interface UserDAO {
    // Inserts a user into where the records are being kept
    void insertUser(UserData data);
    // Retrieves the UserData for a given username
    UserData getUserData(String username);
}
