package service;

import chess.ChessGame;
import dataAccess.*;
import model.*;
import model.Results.GameResult;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.Objects;

public class Service {
    private static Service instance;
    private final AuthDAO authDao = SQLAuthDAO.getInstance();
    private final UserDAO userDao = SQLUserDAO.getInstance();
    private final GameDAO gameDao = SQLGameDAO.getInstance();

    public Service() throws DataAccessException {}

    public static synchronized Service getInstance() throws DataAccessException {
        if (instance == null){
            return new Service();
        } else {
            return instance;
        }
    }

    public String register(UserData user) throws DataAccessException {
        UserData databaseUser = userDao.getUser(user.username());
        if (databaseUser == null) {
            userDao.insertUser(user);
            AuthData auth = authDao.createAuth(user.username());
            authDao.insertAuth(auth);
            return auth.authToken();
        } else {
            throw new DataAccessException("Error: already taken");
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
        String hashedPassword = userDao.selectPassword(loginData.username());
        if (verifyUser(loginData.password(), hashedPassword)){
            AuthData auth = authDao.createAuth(loginData.username());
            authDao.insertAuth(auth);
            return auth.authToken();
        } else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    private boolean verifyUser(String providedClearTextPassword, String hashedPassword){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(providedClearTextPassword, hashedPassword);
    }

    public void logout(String authToken) throws DataAccessException {
        if (authDao.authExists(authToken)) {
            authDao.deleteAuth(authToken);
        } else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public void testAuth(String authToken) throws DataAccessException {
        if (!authDao.authExists(authToken)) {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public int createGame(String gameName) throws DataAccessException{
        if (!Objects.equals(gameName, "")) {
            GameData game = gameDao.createGame(gameName);
            return game.gameID();
        }
        throw new DataAccessException("Error: bad request");
    }

    public GameData getGame(int gameID) throws DataAccessException {
        return gameDao.getGame(gameID);
    }

    public void setGame(String game, int gameID) throws DataAccessException {
        gameDao.updateGameString(game, gameID);
    }

    public String getUsername(String authToken) throws DataAccessException {
        return authDao.getUser(authToken);
    }

    public void joinGame(JoinGameData gameReqData, String authToken) throws DataAccessException {
        GameData game = gameDao.getGame(gameReqData.gameID());
        String username = authDao.getUser(authToken);
        if (Objects.equals(gameReqData.playerColor(), "BLACK")) {
            if (game.blackUsername() == null) {
                game = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
                gameDao.updateGame(game, "BLACK");
            } else {
                throw new DataAccessException("Error: already taken");
            }
        } else if (Objects.equals(gameReqData.playerColor(), "WHITE")) {
            if (game.whiteUsername() == null) {
                game = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
                gameDao.updateGame(game, "WHITE");
            } else {
                throw new DataAccessException("Error: already taken");
            }
        } else if (!Objects.equals(gameReqData.playerColor(), null)) {
            throw new DataAccessException("Error: already taken");
        }
    }

    public Collection<GameResult> listGames(String authToken) throws DataAccessException {
        if (!authDao.authExists(authToken)){
            throw new DataAccessException("Error: unauthorized");
        }
        return gameDao.listGames();
    }

    public boolean checkIsResigned(int gameID) throws DataAccessException {
        GameData gameData = gameDao.getGame(gameID);
        ChessGame game = gameData.game();
        return game.isResigned();
    }

    public void setGameResign(int gameID, String username) throws DataAccessException {
        GameData gameData = gameDao.getGame(gameID);
        ChessGame game = gameData.game();
        if ((!Objects.equals(gameData.whiteUsername(), username) && !Objects.equals(gameData.blackUsername(), username)) || checkIsResigned(gameID)) {
            throw new DataAccessException("You are not a player and can't resign the game.");
        }
        game.setResigned(true);
        gameDao.updateGameString(game.toString(), gameID);
    }

    public GameDAO getGameDao() {
        return gameDao;
    }
}
