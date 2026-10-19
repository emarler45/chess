package dataaccess;

import model.UserData;

public interface UserDAO {
    void insertUser(UserData data);
    UserData getUserData(String username);
}
