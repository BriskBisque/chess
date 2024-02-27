package dataAccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {

    public void clear();
    public GameData insertGame(GameData g) throws DataAccessException;
    public GameData getGame(String gameName) throws DataAccessException;
    public Collection<GameData> listGames() throws DataAccessException;
    public void updateGame(GameData g) throws DataAccessException;

}
