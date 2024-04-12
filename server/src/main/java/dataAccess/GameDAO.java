package dataAccess;

import model.GameData;
import model.Results.GameResult;

import java.util.Collection;

public interface GameDAO {
    public void clear() throws DataAccessException;
    public GameData createGame(String gameName) throws DataAccessException;
    public GameData getGame(int gameID) throws DataAccessException;
    public Collection<GameResult> listGames() throws DataAccessException;
    public void updateGame(GameData game, String color) throws DataAccessException;
    public void updateGameString(String game, int gameID) throws DataAccessException;
    public void destroy() throws DataAccessException;
}
