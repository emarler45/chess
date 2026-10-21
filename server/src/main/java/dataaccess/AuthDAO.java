package dataaccess;

import model.AuthData;

public interface AuthDAO {
    // insert AuthData into db
    void insertData(AuthData auth);
    // retrieve AuthData from db
    AuthData getAuthData(String authToken);
    // check if authToken is present in db
    boolean verify(String token);
    // delete given token from db
    void deleteData(String token);
    // clear all records
    void clearAll();
}
