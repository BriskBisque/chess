package server;

import spark.*;

public class Server {
    public Server() {

    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (req, res) -> (Handler.getInstance()).register(req, res));
        Spark.delete("/db", (req, res) -> (Handler.getInstance()).clear(req,res));
        Spark.post("/session", (req, res) -> (Handler.getInstance()).login(req, res));
        Spark.delete("/session", (req, res) -> (Handler.getInstance()).logout(req, res));
        Spark.post("/game", (req, res) -> (Handler.getInstance()).createGame(req, res));
        Spark.put("/game", (req, res) -> (Handler.getInstance()).joinGame(req, res));
        Spark.get("/game", (req, res) -> (Handler.getInstance()).listGames(req, res));

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
