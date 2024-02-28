package unitTests;

import dataAccess.DataAccessException;
import model.GameData;
import model.JoinGameData;
import model.LoginData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import model.GameResult;
import service.Service;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {

    static private Service service;

    @AfterEach
    void clear() {
        assertDoesNotThrow(() -> service.clear());
        assertEquals(service.getAuthDao().getAuths().size(), 0);
        assertEquals(service.getUserDao().getUsers().size(), 0);
        assertEquals(service.getGameDao().getGames().size(), 0);
    }

    @BeforeAll
    static void getInstance() {
        assertDoesNotThrow(() -> service = Service.getInstance());
    }

    @Test
    void registerPos() {
        UserData user = new UserData("username", "password", "email");
        String authToken = assertDoesNotThrow(() -> service.register(user));
        assertNotEquals(authToken, null);
    }

    @Test
    void registerNeg() {
        UserData user = new UserData("username", "password", "email");

        assertThrows(DataAccessException.class, () -> {
            service.register(user);
            service.register(user);
        });
    }

    @Test
    void loginNeg() {
        UserData user = new UserData("username", "password", "email");
        assertDoesNotThrow(() -> service.register(user));

        LoginData loginData = new LoginData("username", "pd");
        assertThrows(DataAccessException.class, () -> {
            service.login(loginData);
        });
    }

    @Test
    void loginPos() {
        UserData user = new UserData("username", "password", "email");
        assertDoesNotThrow(() -> service.register(user));

        LoginData loginData = new LoginData("username", "password");
        String authToken = assertDoesNotThrow(() -> service.login(loginData));
        assertNotEquals(authToken, null);
    }

    @Test
    void logoutPos() {
        UserData user = new UserData("username", "password", "email");
        assertDoesNotThrow(() -> service.register(user));

        LoginData loginData = new LoginData("username", "password");
        String authToken = assertDoesNotThrow(() -> service.login(loginData));
        assertDoesNotThrow(() -> service.logout(authToken));
    }

    @Test
    void logoutNeg(){
        assertThrows(DataAccessException.class, () -> {
            service.logout("aaa");
        });
    }

    @Test
    void testAuthPos() {
        UserData user = new UserData("username", "password", "email");
        assertDoesNotThrow(() -> service.register(user));

        LoginData loginData = new LoginData("username", "password");
        String authToken = assertDoesNotThrow(() -> service.login(loginData));
        assertDoesNotThrow(() -> service.testAuth(authToken));
    }

    @Test
    void testAuthNeg() {
        assertThrows(DataAccessException.class, () -> {
            service.logout(null);
        });
    }

    @Test
    void createGamePos() {
        int gameID = assertDoesNotThrow(() -> service.createGame("AAAAAA"));
        assertTrue(gameID > 0);
    }

    @Test
    void createGameNeg() {
        assertThrows(DataAccessException.class, () -> {
            service.createGame("");
        });
    }

    @Test
    void joinGamePos() {
        UserData user = new UserData("username", "password", "email");
        assertDoesNotThrow(() -> service.register(user));

        LoginData loginData = new LoginData("username", "password");
        String authToken = assertDoesNotThrow(() -> service.login(loginData));
        int gameID = assertDoesNotThrow(() -> service.createGame("AAAAAA"));
        JoinGameData joinGameData = new JoinGameData("WHITE", gameID);
        assertDoesNotThrow(() -> service.joinGame(joinGameData, authToken));
        GameData game = assertDoesNotThrow(() -> service.getGameDao().getGame(gameID));
        assertEquals(game.whiteUsername(), "username");
    }

    @Test
    void joinGameNeg() {
        UserData user = new UserData("username", "password", "email");
        assertDoesNotThrow(() -> service.register(user));

        LoginData loginData = new LoginData("username", "password");
        String authToken = assertDoesNotThrow(() -> service.login(loginData));
        int gameID = assertDoesNotThrow(() -> service.createGame("AAAAAA"));
        JoinGameData joinGameData = new JoinGameData("WHITE", gameID);
        assertDoesNotThrow(() -> service.joinGame(joinGameData, authToken));
        assertThrows(DataAccessException.class, () -> {
            service.joinGame(joinGameData, authToken);
        });
    }

    @Test
    void listGamesPos() {
        UserData user = new UserData("username", "password", "email");
        assertDoesNotThrow(() -> service.register(user));

        LoginData loginData = new LoginData("username", "password");
        String authToken = assertDoesNotThrow(() -> service.login(loginData));
        int gameID = assertDoesNotThrow(() -> service.createGame("AAAAAA"));
        JoinGameData joinGameData = new JoinGameData("WHITE", gameID);
        assertDoesNotThrow(() -> service.joinGame(joinGameData, authToken));
        Collection<GameResult> gameList = assertDoesNotThrow(() -> service.listGames(authToken));

        Collection<GameResult> expectedList = new ArrayList<>();
        GameResult game = new GameResult(gameID, "username", null, "AAAAAA");
        expectedList.add(game);

        assertEquals(gameList, expectedList);
    }

    @Test
    void listGamesNeg(){
        assertThrows(DataAccessException.class, () -> {
            service.listGames(null);
        });
    }
}