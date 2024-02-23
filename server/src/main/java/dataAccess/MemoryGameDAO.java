package dataAccess;

import model.GameData;

import java.util.Collection;

public class MemoryGameDAO implements GameDAO{

    Collection<GameData> gameData;

    public MemoryGameDAO(Collection<GameData> gameData) {
        this.gameData = gameData;
    }

    @Override
    public void clear() {
        gameData.clear();
    }

    @Override
    public GameData insertGame(GameData g) throws DataAccessException {
        return null;
    }

    @Override
    public GameData getGame(GameData g) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(GameData g) throws DataAccessException {

    }
}
