package Test;

import org.eclipse.jetty.client.api.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import server.Server;
import service.UserService;

public class TestServer {
    static private Server chessServer;

    @BeforeAll
    static void startServer() {
        chessServer = new Server(new UserService());
        int port = chessServer.run(8080);
    }

    public void run(){
        this.clear();
    }

    @AfterAll
    static void stopServer() {
        chessServer.stop();
    }

    @BeforeEach
    void clear() {
        //assertDoesNotThrow(() -> chessServer.clear());
    }
}
