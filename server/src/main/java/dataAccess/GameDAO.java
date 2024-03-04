package dataAccess;

import model.GameData;
import model.GameResult;
import model.ObserversData;

import java.util.Collection;

public interface GameDAO {
    public void clear() throws DataAccessException;
    public GameData insertGame(GameData game) throws DataAccessException;
    public GameData getGame(int gameID) throws DataAccessException;
    public Collection<GameResult> listGames() throws DataAccessException;
    public void updateGame(GameData game) throws DataAccessException;
    GameData createGame(String gameName) throws DataAccessException;
    public void addObserver(GameData game, String authToken);
    Collection<GameData> getGames();
    Collection<ObserversData> getObserversData();
}
