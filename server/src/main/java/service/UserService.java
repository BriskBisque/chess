package service;

import dataAccess.*;
import model.UserData;
import server.RegisterResponse;
import server.Response;

public class UserService {
    static UserDAO userDao;
    static AuthDAO authDao;

    public UserService(){
        userDao = new MemoryUserDAO();
        authDao = new MemoryAuthDAO();
    }

    public  register(UserData user) throws DataAccessException {
        UserData databaseUser = userDao.getUser(user);
        if (databaseUser == null) {
            userDao.insertUser(user);
            String authToken = authDao.createAuth(user);
            return new RegisterResponse(authToken, true, user.getUsername());
        } else {
            return new Response(false, "Error: already taken");
        }
    }


}
