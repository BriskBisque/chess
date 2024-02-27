package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.Collection;

public interface AuthDAO {

    public void insertAuth(AuthData a) throws DataAccessException;
    public AuthData getAuth(String authToken) throws DataAccessException;
    public void deleteAuth(String authToken) throws DataAccessException;
    public String createAuth(String username) throws DataAccessException;
    public void clear();
}
