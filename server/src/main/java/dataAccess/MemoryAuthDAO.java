package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{

    static Collection<AuthData> auths = new ArrayList<>();

    public MemoryAuthDAO(){
    }

    public void clear(){
        auths.clear();
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

    public String createAuth(UserData u) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, (String) u.getUsername());
        this.insertAuth(authData);
        return authToken;
    }
}
