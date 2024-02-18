package dataAccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Collection;

public interface UserDAO{

    public void insertUser(UserData u) throws DataAccessException;

    public UserData getUser(UserData u) throws DataAccessException;
}
