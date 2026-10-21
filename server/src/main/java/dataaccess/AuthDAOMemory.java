package dataaccess;

import model.AuthData;

import java.util.ArrayList;

public class AuthDAOMemory implements AuthDAO{

    private ArrayList<AuthData> dataList = new ArrayList<>();

    public AuthDAOMemory(){
        // test entry for use with clear
        dataList.add(new AuthData("123e4567-e89b-12d3-a456-426614174000", "user"));
    }

    @Override
    public void insertData(AuthData auth) {
        dataList.add(auth);
    }

    @Override
    public AuthData getAuthData(String token) {
        for (AuthData currData : dataList) {
            if (currData.authToken().equals(token)) {
                return currData;
            }
        }
    return null;
    }

    @Override
    public boolean verify(String token){
        return (getAuthData(token) != null);
    }

    public void deleteData(String token){
        AuthData toDelete = getAuthData(token);
        dataList.remove(toDelete);
    }

    public void clearAll(){
        dataList = new ArrayList<>();
    }
}
