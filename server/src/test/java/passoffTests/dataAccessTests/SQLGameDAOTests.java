package passoffTests.dataAccessTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.SQLGameDAO;
import model.GameData;
import model.Results.GameResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class SQLGameDAOTests {
    static private GameDAO gameDao;

    @AfterEach
    void clear() {
        assertDoesNotThrow(() -> gameDao.clear());
    }

    @BeforeAll
    static void getInstance() {
        assertDoesNotThrow(() -> gameDao = SQLGameDAO.getInstance());
    }

    @Test
    void createGamePos(){
        String gameName = "AAAAA";
        GameData game = assertDoesNotThrow(() -> gameDao.createGame(gameName));
        assertEquals(game.gameID(), 1);
    }

    @Test
    void createGameNeg(){
        assertThrows(DataAccessException.class, () -> {
            assertNull(gameDao.createGame(""));
        });
    }

    @Test
    void getGamePos(){
        GameData expectedGameData = new GameData(1, null, null, "AAAAA", new ChessGame());
        GameData game = assertDoesNotThrow(() -> gameDao.createGame("AAAAA"));
        GameData actualGame = assertDoesNotThrow(() -> gameDao.getGame(game.gameID()));
        assertEquals(expectedGameData, actualGame);
    }

    @Test
    void getGameNeg(){
        GameData actualGame = assertDoesNotThrow(() -> gameDao.getGame(100));
        assertNull(actualGame);
    }

    @Test
    void listGamesPos(){
        GameResult insertGame1 = new GameResult(1, null, null, "AAAAA");
        GameResult insertGame2 = new GameResult(2, null, null, "BBBBB");
        Collection<GameResult> expectedGameData = new ArrayList<>();
        expectedGameData.add(insertGame1);
        expectedGameData.add(insertGame2);

        GameData game1 = assertDoesNotThrow(() -> gameDao.createGame("AAAAA"));
        GameData actualGame1 = assertDoesNotThrow(() -> gameDao.getGame(game1.gameID()));
        GameData game2 = assertDoesNotThrow(() -> gameDao.createGame("BBBBB"));
        GameData actualGame2 = assertDoesNotThrow(() -> gameDao.getGame(game2.gameID()));
        Collection<GameResult> actualGame = assertDoesNotThrow(() -> gameDao.listGames());

        assertEquals(expectedGameData, actualGame);
    }

    @Test
    void listGamesNeg(){
        Collection<GameResult> expectedGames = new ArrayList<>();
        Collection<GameResult> actualGames = assertDoesNotThrow(() -> gameDao.listGames());

        assertEquals(expectedGames, actualGames);
    }

    @Test
    void updateGamePos(){
        GameData game = assertDoesNotThrow(() -> gameDao.createGame("AAAAA"));
        GameData expected = new GameData(1, "your mom", null, "AAAAA", new ChessGame());
        assertDoesNotThrow(() -> gameDao.updateGame(expected, "WHITE"));
        GameData actual = assertDoesNotThrow(() -> gameDao.getGame(game.gameID()));
        assertEquals(expected, actual);
    }

    @Test
    void updateGameNeg(){
        assertThrows(DataAccessException.class, () -> {
            GameData expected = new GameData(1, "your mom", null, "AAAAA", new ChessGame());
            gameDao.updateGame(expected, "W");
        });
    }
}
