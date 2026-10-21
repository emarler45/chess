package dataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class UserDAOMemory implements UserDAO {

    private ArrayList<UserData> dataList = new ArrayList<>();

    public UserDAOMemory() {
        dataList.add(new UserData("username", "pw", "email"));
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

    public void clearAll() {
        dataList = new ArrayList<>();
    }
}
