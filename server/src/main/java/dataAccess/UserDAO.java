package dataAccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Collection;

public interface UserDAO{
    public void insertUser(UserData user) throws DataAccessException;
    public UserData getUser(UserData user);
    public void clear();
    String selectPassword(String username);
    Collection<UserData> getUsers();
}
