package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{

    private static MemoryAuthDAO instance;
    private  Collection<AuthData> auths = new ArrayList<>();

    public MemoryAuthDAO(){}

    public static AuthDAO getInstance(){
        if (instance == null){
            return new MemoryAuthDAO();
        } else {
            return instance;
        }
    }


    public void clear(){
        auths = new ArrayList<>();
    }

    @Override
    public void insertAuth(AuthData a) throws DataAccessException {
        auths.add(a);
    }

    @Override
    public AuthData getAuth(AuthData a) throws DataAccessException {
        for (AuthData auth: auths){
            if (auth.equals(a)){
                return auth;
            }
        }
        return null;
    }

    @Override
    public void deleteAuth(AuthData a) throws DataAccessException {
        auths.remove(a);
    }

    public String createAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, username);
        this.insertAuth(authData);
        return authToken;
    }
}
