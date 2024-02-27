package dataAccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryGameDAO implements GameDAO{

    private Collection<GameData> games = new ArrayList<>();
    private static MemoryGameDAO instance;
    private int numOfGames = 0;

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
        numOfGames = 0;
    }

    @Override
    public void insertGame(GameData game){
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

    public GameData createGame(String gameName){
        GameData newGame = new GameData(numOfGames, null, null, gameName, new ChessGame());
        numOfGames++;
        insertGame(newGame);
        return newGame;
    }

    @Override
    public Collection<GameData> listGames(){
        return games;
    }

    @Override
    public void updateGame(GameData g){

    }
}
