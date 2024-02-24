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
        Spark.post("/user", this::register);
        Spark.delete("/db", this::clear);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object register(Request req, Response res) throws DataAccessException {
        boolean descriptionCheck = false;
        UserData userReq;
        try {
            userReq = (UserData) new Gson().fromJson(req.body(), UserData.class);
        } catch(JsonSyntaxException e) {
            descriptionCheck = true;
            userReq = null;
        }

        if (descriptionCheck){
            FailureResponse response_500 = new FailureResponse("Error: description");
            return new Gson().toJson(response_500);
        }

        boolean badRequestCheck = false;
        String authToken = null;

        try {
            UserService service = new UserService();
            authToken = service.register(userReq);
        } catch (DataAccessException e){
            badRequestCheck = true;
        }

        if(badRequestCheck){
            FailureResponse response_400 = new FailureResponse("Error: bad request");
            return new Gson().toJson(response_400);
        }

        if (authToken == null){
            FailureResponse response_403 = new FailureResponse("Error: already taken");
            return new Gson().toJson(response_403);
        } else {
            UserResponse response_200 = new UserResponse((String) userReq.getUsername(), authToken);
            return new Gson().toJson(response_200);
        }
    }

    public Object clear(Request req, Response res){
        service.clear();
        res.status(200);
        return new Gson().toJson("");
    }

    public int port() {
        return Spark.port();
    }
}
