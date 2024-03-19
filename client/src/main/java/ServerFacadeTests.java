import dataAccess.DataAccessException;
import model.JoinGameData;
import model.LoginData;
import model.Results.GameResult;
import model.Results.ListGameResult;
import model.UserData;
import model.Results.UserResult;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.GameNameResponse;
import server.Server;
import server.ServerFacade;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {
    static ServerFacade facade;
    private static Server server;


    @BeforeAll
    public static void init() {
        server = new Server();
        server.run(0);
        var url = "http://localhost:" + server.port();
        facade = new ServerFacade(url);
    }

    @AfterAll
    static void stopServer() {
        assertDoesNotThrow(() -> facade.deleteAll());
        server.stop();
    }

    @Test
    void registerUserPos(){
        UserResult authData = assertDoesNotThrow(() -> facade.registerUser(new UserData("player1", "password", "p1@email.com")));
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void registerUserNeg(){
        assertThrows(DataAccessException.class, () -> {
            UserResult authData = facade.registerUser(new UserData("player1", null, "p1@email.com"));
        });
    }

    @Test
    void loginUserPos(){
        UserData userData = new UserData("player1", "password", "p1@email.com");
        assertDoesNotThrow(() -> facade.registerUser(userData));
        UserResult authData = assertDoesNotThrow(() -> facade.loginUser(new LoginData(userData.username(), userData.password())));
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void loginUserNeg(){
        assertThrows(DataAccessException.class, () -> {
            UserData userData = new UserData("player1", "password", "p1@email.com");
            UserResult authData = facade.loginUser(new LoginData(userData.username(), userData.password()));
        });
    }

    @Test
    void logoutUserPos(){
        UserData userData = new UserData("player1", "password", "p1@email.com");
        assertDoesNotThrow(() -> facade.registerUser(userData));
        UserResult authData = assertDoesNotThrow(() -> facade.loginUser(new LoginData(userData.username(), userData.password())));
        assertDoesNotThrow(() -> facade.logoutUser(authData.authToken()));
    }

    @Test
    void logoutUserNeg(){
        assertThrows(DataAccessException.class, () -> {
            facade.logoutUser(null);
        });
    }

    @Test
    void createGamePos(){
        UserData userData = new UserData("player1", "password", "p1@email.com");
        assertDoesNotThrow(() -> facade.registerUser(userData));
        UserResult authData = assertDoesNotThrow(() -> facade.loginUser(new LoginData(userData.username(), userData.password())));
        int gameID = assertDoesNotThrow(() -> facade.createGame(authData.authToken(), new GameNameResponse("game name")));
        assertTrue(gameID >0);
    }

    @Test
    void createGameNeg(){
        assertThrows(DataAccessException.class, () -> {
            int gameID = facade.createGame(null, new GameNameResponse("game name"));
        });
    }

    @Test
    void joinGamePos(){
        UserData userData = new UserData("player1", "password", "p1@email.com");
        assertDoesNotThrow(() -> facade.registerUser(userData));
        UserResult authData = assertDoesNotThrow(() -> facade.loginUser(new LoginData(userData.username(), userData.password())));
        int gameID = assertDoesNotThrow(() -> facade.createGame(authData.authToken(), new GameNameResponse("game name")));
        assertDoesNotThrow(() -> facade.joinGame(authData.authToken(), new JoinGameData("WHITE", gameID)));
    }

    @Test
    void joinGameNeg(){
        assertThrows(DataAccessException.class, () -> {
            facade.joinGame(null, new JoinGameData(null, 1));
        });
    }

    @Test
    void ListGamesPos(){
        UserData userData = new UserData("player1", "password", "p1@email.com");
        assertDoesNotThrow(() -> facade.registerUser(userData));
        UserResult authData = assertDoesNotThrow(() -> facade.loginUser(new LoginData(userData.username(), userData.password())));
        int gameID = assertDoesNotThrow(() -> facade.createGame(authData.authToken(), new GameNameResponse("game name")));
        assertDoesNotThrow(() -> facade.joinGame(authData.authToken(), new JoinGameData("WHITE", gameID)));
        ListGameResult gamesResult = assertDoesNotThrow(() -> facade.listGames(authData.authToken()));
        Collection<GameResult> games = gamesResult.games();
        assert !games.isEmpty() : "No games in server";
    }
}