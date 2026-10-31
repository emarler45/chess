package dataaccess;

import model.AuthData;

public interface AuthDAO {
    // insert AuthData into db
    void insertData(AuthData auth) throws Exception;
    // retrieve AuthData from db
    AuthData getAuthData(String authToken) throws Exception;
    // check if authToken is present in db
    boolean verify(String token) throws Exception;
    // delete given token from db
    void deleteData(String token) throws Exception;
    // clear all records
    void clearAll() throws DataAccessException;
}
