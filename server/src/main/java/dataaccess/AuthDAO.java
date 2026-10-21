package dataaccess;

import model.AuthData;

public interface AuthDAO {
    // insert AuthData into db
    public void insertData(AuthData auth);
    // retrieve AuthData from db
    public AuthData getAuthData(String authToken);
    // check if authToken is present in db
    public boolean verify(AuthData data);
    // clear all records
    void clearAll();
}
