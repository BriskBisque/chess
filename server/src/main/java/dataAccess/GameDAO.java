package dataAccess;

import model.GameData;
import model.ObserversData;

import java.util.Collection;

public interface GameDAO {
    public void clear() throws DataAccessException;
    public void insertGame(GameData game);
    public GameData getGame(int gameID) throws DataAccessException;
    public Collection<GameData> listGames();
    public void updateGame(GameData game) throws DataAccessException;
    GameData createGame(String gameName);
    public void addObserver(GameData game, String authToken);
    Collection<GameData> getGames();
    Collection<ObserversData> getObserversData();
}
