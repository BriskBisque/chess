package dataAccess;

import model.AuthData;

public interface AuthDAO {
    public void insertAuth(AuthData authToken) throws DataAccessException;
    public String getAuth(String username) throws DataAccessException;
    public void deleteAuth(String authToken) throws DataAccessException;
    public AuthData createAuth(String username);
    public void clear() throws DataAccessException;
    public boolean authExists(String authToken) throws DataAccessException;
    public String getUser(String authToken) throws DataAccessException;
}
