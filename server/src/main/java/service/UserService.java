package service;

import dataAccess.*;
import model.GameData;
import model.JoinGameData;
import model.LoginData;
import model.UserData;
import server.GameResponseData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

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
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public void logout(String authToken) throws DataAccessException {
        authDao.deleteAuth(authToken);
    }

    public void testAuth(String authToken) throws DataAccessException{
        if (authDao.getAuth(authToken) == null){
            throw new DataAccessException("Error: unathorized");
        }
    }

    public int createGame(String gameName) throws DataAccessException{
        GameData game = gameDao.createGame(gameName);
        if (game != null){
            return game.gameID();
        } else {
            throw new DataAccessException("Error: bad request");
        }
    }

    public void joinGame(JoinGameData gameReqData, String authToken) throws DataAccessException {
        GameData game = gameDao.getGame(gameReqData.gameID());
        String username = authDao.getUser(authToken);
        if (Objects.equals(gameReqData.playerColor(), "BLACK")) {
            if (game.blackUsername() == null) {
                game = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
                gameDao.updateGame(game);
            } else {
                throw new DataAccessException("Error: already taken");
            }
        } else if (Objects.equals(gameReqData.playerColor(), "WHITE")) {
            if (game.whiteUsername() == null) {
                game = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
                gameDao.updateGame(game);
            } else {
                throw new DataAccessException("Error: already taken");
            }
        } else {
            gameDao.addObserver(game, authToken);
        }
    }

    public Collection<GameResponseData> listGames(String authToken) throws DataAccessException {
        testAuth(authToken);
        Collection<GameData> games = gameDao.listGames();
        Collection<GameResponseData> resultGames = new ArrayList<>();
        GameResponseData toAdd;
        for (GameData game: games){
            toAdd = new GameResponseData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName());
            resultGames.add(toAdd);
        }
        return resultGames;
    }
}
