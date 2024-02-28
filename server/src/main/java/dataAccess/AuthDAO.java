package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;

public interface AuthDAO {
    public void insertAuth(AuthData authToken);
    public AuthData getAuth(String authToken);
    public void deleteAuth(String authToken) throws DataAccessException;
    public String createAuth(String username);
    public void clear();
    public String getUser(String authToken) throws DataAccessException;
    Collection<AuthData> getAuths();
}
