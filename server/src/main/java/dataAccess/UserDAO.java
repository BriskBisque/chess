package dataAccess;

import model.UserData;

import java.util.Collection;

public interface UserDAO {
    public void insertUser(UserData user) throws DataAccessException;
    public UserData getUser(UserData user) throws DataAccessException;
    public void clear() throws DataAccessException;
    String selectPassword(String username) throws DataAccessException;
    Collection<UserData> getUsers();
}
