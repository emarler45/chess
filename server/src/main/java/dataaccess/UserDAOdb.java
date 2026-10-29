package dataaccess;

import model.UserData;

public class UserDAOdb implements UserDAO{

    DatabaseManager dbManager = new DatabaseManager();

    @Override
    public void insertUser(UserData data) {

    }

    @Override
    public UserData getUserData(String username) {
        return null;
    }

    @Override
    public void clearAll() {

    }
}
