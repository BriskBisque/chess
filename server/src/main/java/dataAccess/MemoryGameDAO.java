package dataAccess;

import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryGameDAO implements GameDAO{

    private Collection<GameData> games = new ArrayList<>();
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
        games = new ArrayList<>();
    }

    @Override
    public GameData insertGame(GameData game){
        games.add(game);
    }

    @Override
    public GameData getGame(String gameName){
        if (games.isEmpty()){
            return null;
        }
        for (GameData game: games){
            if (game.gameName().equals(gameName)){
                return game;
            }
        }
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
