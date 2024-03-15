import dataAccess.DataAccessException;
import model.UserData;
import model.UserResult;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void registerUser() throws Exception {
        UserResult authData = facade.registerUser(new UserData("player1", "password", "p1@email.com"));
        assertTrue(authData.authToken().length() > 10);
    }
}