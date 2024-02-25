package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryUserDAO implements UserDAO{

    private Collection<UserData> users = new ArrayList<>();
    private static MemoryUserDAO instance;

    public MemoryUserDAO(){}

    public static UserDAO getInstance(){
        if (instance == null){
            return new MemoryUserDAO();
        } else {
            return instance;
        }
    }

    public void clear(){
        users = new ArrayList<>();
    }

    public void insertUser(UserData u) throws DataAccessException{
        users.add(u);
    }

    public UserData getUser(UserData u) throws DataAccessException{
        if (users.isEmpty()){
            return null;
        }
        for (UserData user: users){
            if (user.equals(u)){
                return user;
            }
        }
        return null;
    }

    public void checkEmpty() throws DataAccessException{
        if (users.isEmpty()){
            throw new DataAccessException("Error: no users");
        }
    }

    public String selectPassword(String username) throws DataAccessException {
        checkEmpty();
        for (UserData user: users){
            if (user.username().equals(username)){
                return user.password();
            }
        }
        return null;
    }
}
