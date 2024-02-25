package service;

import dataAccess.*;
import model.LoginData;
import model.UserData;

public class UserService {
    private static UserService instance;
    private final AuthDAO authDao = MemoryAuthDAO.getInstance();
    private final UserDAO userDao = MemoryUserDAO.getInstance();
    private final GameDAO gameDao = MemoryGameDAO.getInstance();

    public UserService(){}

    public static synchronized UserService getInstance(){
        if (instance == null){
            return new UserService();
        } else {
            return instance;
        }
    }

    public String register(UserData user) throws DataAccessException {
        UserData databaseUser = userDao.getUser(user);
        if (databaseUser == null) {
            userDao.insertUser(user);
            return authDao.createAuth(user.username());
        } else {
            return null;
        }
    }

    public void clear() throws DataAccessException {
        try {
            userDao.clear();;
            authDao.clear();
            gameDao.clear();
        } catch (Exception e){
            throw new DataAccessException("Error: description");
        }
    }

    public String login(LoginData loginData) throws DataAccessException{
        String databasePassword = userDao.selectPassword(loginData.username());
        if (loginData.password().equals(databasePassword)){
            return authDao.createAuth(loginData.username());
        } else {
            return null;
        }
    }
}
