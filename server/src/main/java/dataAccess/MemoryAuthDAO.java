package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

public class MemoryAuthDAO implements AuthDAO{

    private static MemoryAuthDAO instance;
    private Collection<AuthData> auths = new ArrayList<>();

    public MemoryAuthDAO(){}

    public static synchronized AuthDAO getInstance(){
        if (instance == null){
            instance = new MemoryAuthDAO();
        }
        return instance;
    }

    public Collection<AuthData> getAuths() {
        return auths;
    }

    public void clear(){
        auths = new ArrayList<>();
    }

    @Override
    public boolean authExists(String authToken) throws DataAccessException {
        return false;
    }

    public void insertAuth(AuthData authToken){
        auths.add(authToken);
    }

    public String getAuth(String authToken){
        for (AuthData auth: auths){
            if (auth.authToken().equals(authToken)){
                return auth.authToken();
            }
        }
        return null;
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        Predicate<AuthData> isAuthToken = data -> Objects.equals(data.authToken(), authToken);
        if (!auths.removeIf(isAuthToken)){
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public String createAuth(String username){
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, username);
        this.insertAuth(authData);
        return authToken;
    }

    public String getUser(String authToken) throws DataAccessException{
        for (AuthData auth: auths){
            if (auth.authToken().equals(authToken)){
                return auth.username();
            }
        }
        throw new DataAccessException("Error: unauthorized");
    }
}
