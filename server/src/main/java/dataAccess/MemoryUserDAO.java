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

    public static synchronized UserDAO getInstance(){
        if (instance == null){
            instance = new MemoryUserDAO();
        }
        return instance;
    }

    public void clear(){
        users = new ArrayList<>();
    }

    public void insertUser(UserData user) throws DataAccessException {
        if (user.password() != null && user.username() != null) {
            users.add(user);
        } else {
            throw new DataAccessException("Error: bad request");
        }
    }

    public UserData getUser(UserData u){
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

    public String selectPassword(String username){
        if (users.isEmpty()){
            return null;
        }
        for (UserData user: users){
            if (user.username().equals(username)){
                return user.password();
            }
        }
        return null;
    }
}
