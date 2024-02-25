package Test;

import com.google.gson.Gson;
import model.LoginData;
import model.UserData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Handler;
import server.Server;
import server.ServerFacade;
import server.UserResponse;
import service.UserService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestServer {
    static private Server chessServer;
    private static ServerFacade facade;

    @BeforeAll
    static void startServer() {
        chessServer = new Server(new UserService());
        int port = chessServer.run(8080);
        facade = new ServerFacade("http://localhost:"+port);
    }

    @AfterAll
    static void stopServer() {
        chessServer.stop();
    }

    @BeforeEach
    public void clear() {
        assertDoesNotThrow(() -> facade.clearAll());
    }

    @Test
    void registerUser(){
        UserData user = new UserData("username", "password", "email");
        UserData result = assertDoesNotThrow(() -> facade.registerUser(user));
        assertEquals(user.username(), result.username());
    }

    @Test
    void loginUser(){
        UserData user = new UserData("username", "password", "email");
        UserData result = assertDoesNotThrow(() -> facade.registerUser(user));
        LoginData login = new LoginData("username", "password");
        result = assertDoesNotThrow(() -> facade.loginUser(login));
        assertEquals(login.username(), result.username());
    }
}
