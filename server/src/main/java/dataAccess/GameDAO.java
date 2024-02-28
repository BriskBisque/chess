package dataAccess;

import model.GameData;
import model.ObserversData;

import java.util.Collection;

public interface GameDAO {

    public void clear();
    public void insertGame(GameData game) throws DataAccessException;
    public GameData getGame(int gameID) throws DataAccessException;
    public Collection<GameData> listGames() throws DataAccessException;
    public void updateGame(GameData game) throws DataAccessException;

    GameData createGame(String gameName);
    public void addObserver(GameData game, String authToken) throws DataAccessException;
    Collection<GameData> getGames();
    Collection<ObserversData> getObserversData();
}
