package dataAccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryGameDAO implements GameDAO{

    private Collection<GameData> gameData = new ArrayList<>();
    private static MemoryGameDAO instance;

    public MemoryGameDAO() {}

    public static synchronized GameDAO getInstance(){
        if (instance == null){
            instance = new MemoryGameDAO();
        }
        return instance;
    }

    @Override
    public void clear() {
        gameData = new ArrayList<>();
    }

    @Override
    public GameData insertGame(GameData g){
        return null;
    }

    @Override
    public GameData getGame(GameData g){
        return null;
    }

    @Override
    public Collection<GameData> listGames(){
        return null;
    }

    @Override
    public void updateGame(GameData g){

    }
}
