package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryUserDAO implements UserDAO{

    Collection<UserData> users;

    public MemoryUserDAO(){
        users = new ArrayList<>();
    }

    public void insertUser(UserData u) throws DataAccessException{
        this.users.add(u);
    }

    public UserData getUser(UserData u) throws DataAccessException{
        for (UserData user: this.users){
            if (user.equals(u)){
                return user;
            }
        }
        return null;
    }
}
