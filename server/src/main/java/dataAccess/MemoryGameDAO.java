package dataAccess;

import chess.ChessGame;
import model.GameData;
import model.GameResult;
import model.ObserversData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryGameDAO implements GameDAO{

    private Collection<GameData> games = new ArrayList<>();
    private static MemoryGameDAO instance;
    private Collection<ObserversData> observersData = new ArrayList<>();
    private int numOfGames = 1;

    public MemoryGameDAO() {}

    public static synchronized GameDAO getInstance(){
        if (instance == null){
            instance = new MemoryGameDAO();
        }
        return instance;
    }

    public Collection<GameData> getGames() {
        return games;
    }

    public Collection<ObserversData> getObserversData() {
        return observersData;
    }

    @Override
    public void clear() {
        games = new ArrayList<>();
        observersData = new ArrayList<>();
        numOfGames = 1;
    }

    @Override
    public GameData insertGame(GameData game){
        games.add(game);
        return game;
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
        throw new DataAccessException("Error: bad request");
    }

    public GameData createGame(String gameName){
        GameData newGame = new GameData(numOfGames, null, null, gameName, new ChessGame());
        numOfGames++;
        insertGame(newGame);
        return newGame;
    }

    @Override
    public Collection<GameResult> listGames(){
        return null;
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

    public void addObserver(GameData game, String authToken) {
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
