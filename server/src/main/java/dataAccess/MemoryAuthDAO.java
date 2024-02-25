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

    public static synchronized AuthDAO getInstance(){
        if (instance == null){
            instance = new MemoryAuthDAO();
        }
        return instance;
    }


    public void clear(){
        auths = new ArrayList<>();
    }

    public void insertAuth(AuthData a){
        auths.add(a);
    }

    public AuthData getAuth(AuthData a){
        for (AuthData auth: auths){
            if (auth.equals(a)){
                return auth;
            }
        }
        return null;
    }

    public void deleteAuth(AuthData a){
        auths.remove(a);
    }

    public String createAuth(String username){
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, username);
        this.insertAuth(authData);
        return authToken;
    }
}
