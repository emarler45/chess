package dataaccess;

import model.AuthData;

public interface AuthDAO {
    public void insertData(AuthData auth);
    public AuthData getAuthData(String authToken);
}
