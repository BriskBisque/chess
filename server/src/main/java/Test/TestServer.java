package Test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import server.Server;
import server.ServerFacade;
import service.UserService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class TestServer {
    static private Server chessServer;
    static private ServerFacade server;

    @BeforeAll
    static void startServer() {
        chessServer = new Server(new UserService());
        chessServer.run(0);
        var url = "http://localhost:" + chessServer.port();
        server = new ServerFacade(url);
    }

    @AfterAll
    static void stopServer() {
        chessServer.stop();
    }

    @BeforeEach
    void clear() {
        assertDoesNotThrow(() -> server.clearAll());
    }
}
