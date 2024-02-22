package service;

import dataAccess.*;
import model.UserData;

public class UserService {
    static UserDAO userDao;
    static AuthDAO authDao;

    public UserService(){
        userDao = new MemoryUserDAO();
        authDao = new MemoryAuthDAO();
    }

    public String register(UserData user) throws DataAccessException {
        UserData databaseUser = userDao.getUser(user);
        if (databaseUser == null) {
            userDao.insertUser(user);
            return authDao.createAuth(user);
        } else {
            return null;
        }
    }


}
