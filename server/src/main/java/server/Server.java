package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataAccess.DataAccessException;
import model.UserData;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import service.UserService;
import spark.*;

public class Server {

    private final UserService service;

    public Server(UserService service) {
        this.service = service;
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (req, res) -> (new Handler()).register(req, res));
        Spark.delete("/db", (req, res) -> (new Handler()).clear(req,res));
        Spark.post("/session", (req, res) -> (new Handler()).login(req, res));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public int port() {
        return Spark.port();
    }
}
