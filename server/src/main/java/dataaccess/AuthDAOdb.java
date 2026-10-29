package dataaccess;

import model.AuthData;

public class AuthDAOdb implements AuthDAO {
    @Override
    public void insertData(AuthData auth) {

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
}
