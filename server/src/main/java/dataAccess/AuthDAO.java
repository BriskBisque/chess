package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.Collection;

public interface AuthDAO {

    public void insertAuth(AuthData a) throws DataAccessException;
    public AuthData getAuth(AuthData a) throws DataAccessException;
    public void deleteAuth(AuthData a) throws DataAccessException;
    public String createAuth(UserData u) throws DataAccessException;
    public void clear();

}
