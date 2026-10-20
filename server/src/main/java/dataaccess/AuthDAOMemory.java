package dataaccess;

import model.AuthData;

import java.util.ArrayList;

public class AuthDAOMemory implements AuthDAO{

    private final ArrayList<AuthData> dataList = new ArrayList<>();

    public AuthDAOMemory(){}

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
    public boolean verify(AuthData data){
        return dataList.contains(data);
    }

    public int getNumOfAuths() {
        return dataList.size();
    }
}
