package dataAccess;

import chess.ChessGame;
import model.GameData;
import model.ObserversData;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryGameDAO implements GameDAO{

    private Collection<GameData> games = new ArrayList<>();
    private static MemoryGameDAO instance;
    private Collection<ObserversData> observersData = new ArrayList<>();
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
        observersData = new ArrayList<>();
        numOfGames = 0;
    }

    @Override
    public void insertGame(GameData game){
        games.add(game);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException{
        if (games.isEmpty()){
            throw new DataAccessException("Error: bad request");
        }
        for (GameData game: games){
            if (game.gameID() == (gameID)){
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
    public void updateGame(GameData game) throws DataAccessException {
        if (games.isEmpty()){
            throw new DataAccessException("Error: bad request");
        }
        for (GameData databaseGame: games){
            if (databaseGame.gameID() == (game.gameID())){
                games.remove(databaseGame);
                games.add(game);
                break;
            }
        }
    }

    public void addObserver(GameData game, String authToken){
        if (observersData.isEmpty()){
            Collection<String> observersList = new ArrayList<>();
            observersList.add(authToken);
            ObserversData newObserver = new ObserversData(game, observersList);
            observersData.add(newObserver);
        } else {
            for (ObserversData data: observersData){
                if (data.gameData().gameID() == game.gameID()){
                    Collection<String> observerList = data.observers();
                    observerList.add(authToken);
                    ObserversData newObserver = new ObserversData(data.gameData(), observerList);
                    observersData.remove(data);
                    observersData.add(newObserver);
                    break;
                }
            }
        }
    }
}
