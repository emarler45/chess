package dataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class UserDAOMemory implements UserDAO {

    private final ArrayList<UserData> dataList = new ArrayList<>();

    public UserDAOMemory() {
    }

    @Override
    public void insertUser(UserData data) {
        dataList.add(data);
    }

    @Override
    public UserData getUserData(String username) {
        for (UserData currData : dataList) {
            if (Objects.equals(currData.username(), username)) {
                return currData;
            }
        }
        return null;
    }

    public int getNumOfUsers(){
        return dataList.size();
    }
}
