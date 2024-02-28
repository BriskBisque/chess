package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataAccess.DataAccessException;
import model.*;
import service.Service;
import spark.Request;
import spark.Response;

import java.util.Collection;
import java.util.Objects;

public class Handler {

    private final Service service = Service.getInstance();
    private static Handler instance;

    public Handler() {}

    public static synchronized Handler getInstance(){
        if (instance == null){
            return new Handler();
        }
        return instance;
    }

    public Object register(Request req, Response res) throws DataAccessException {
        UserData userReq = null;
        userReq = (UserData) new Gson().fromJson(req.body(), UserData.class);

        if (userReq.username() == null || userReq.password() == null || userReq.email() == null){
            res.status(400);
            FailureResult response_400 = new FailureResult("Error: bad request");
            return new Gson().toJson(response_400);
        }

        String authToken = null;

        try {
            Service service = new Service();
            authToken = service.register(userReq);
        } catch (DataAccessException e){
            res.status(403);
            FailureResult response_403 = new FailureResult("Error: already taken");
            return new Gson().toJson(response_403);
        }

        UserResult response_200 = new UserResult(userReq.username(), authToken);
        return new Gson().toJson(response_200);

    }

    public Object clear(Request req, Response res){
        try {
            service.clear();
        } catch (Exception e){
            FailureResult response_500 = new FailureResult("Error: description");
            return new Gson().toJson(response_500);
        }
        res.status(200);
        return new Gson().toJson(new Result());
    }

    public Object login(Request req, Response res){
        LoginData userReq;
        try {
            userReq = (LoginData) new Gson().fromJson(req.body(), LoginData.class);
        } catch(JsonSyntaxException e) {
            FailureResult response_500 = new FailureResult("Error: description");
            return new Gson().toJson(response_500);
        }

        String authToken = null;

        try {
            authToken = service.login(userReq);
        } catch (DataAccessException e){
            res.status(401);
            FailureResult response_401 = new FailureResult("Error: unauthorized");
            return new Gson().toJson(response_401);
        }

        res.status(200);
        UserResult response_200 = new UserResult(userReq.username(), authToken);
        return new Gson().toJson(response_200);
    }

    public Object logout(Request req, Response res){
        String authToken = null;
        try {
            authToken = req.headers("authorization");
        } catch(JsonSyntaxException e) {
            FailureResult response_500 = new FailureResult("Error: description");
            return new Gson().toJson(response_500);
        }

        try {
            service.logout(authToken);
        } catch (DataAccessException e){
            res.status(401);
            FailureResult response_401 = new FailureResult("Error: unauthorized");
            return new Gson().toJson(response_401);
        }

        res.status(200);
        return new Gson().toJson(new Result());
    }

    public Object createGame(Request req, Response res) {
        String authToken = null;
        String gameName = null;
        try {
            authToken = req.headers("authorization");
            gameName = req.queryParams("gameName");
        } catch(JsonSyntaxException e) {
            FailureResult response_500 = new FailureResult("Error: description");
            return new Gson().toJson(response_500);
        }

        try {
            service.testAuth(authToken);
        } catch (DataAccessException e){
            res.status(401);
            FailureResult response_401 = new FailureResult("Error: unauthorized");
            return new Gson().toJson(response_401);
        }

        int gameID = 0;

        try {
            gameID = service.createGame(gameName);
        } catch (DataAccessException e){
            res.status(400);
            FailureResult response_400 = new FailureResult("Error: bad request");
            return new Gson().toJson(response_400);
        }

        res.status(200);
        return new Gson().toJson(new GameIDResult(gameID));
    }

    public Object joinGame(Request req, Response res) {
        JoinGameData gameReqData;

        String authToken = req.headers("authorization");
        gameReqData = new Gson().fromJson(req.body(), JoinGameData.class);

        if ((!Objects.equals(gameReqData.playerColor(), "WHITE") && !Objects.equals(gameReqData.playerColor(), "BLACK") && !Objects.equals(gameReqData.playerColor(), null)) || Objects.equals(gameReqData.gameID(), 0)){
            res.status(400);
            FailureResult response_400 = new FailureResult("Error: bad request");
            return new Gson().toJson(response_400);
        }

        try {
            service.testAuth(authToken);
        } catch (DataAccessException e){
            res.status(401);
            FailureResult response_401 = new FailureResult("Error: unauthorized");
            return new Gson().toJson(response_401);
        }

        try {
            service.joinGame(gameReqData, authToken);
        } catch (DataAccessException e){
            res.status(403);
            FailureResult response_403 = new FailureResult("Error: already taken");
            return new Gson().toJson(response_403);
        }

        res.status(200);
        return new Gson().toJson(new Result());
    }

    public Object listGames(Request req, Response res) {
        String authToken = null;
        try {
            authToken = req.headers("authorization");
        } catch(JsonSyntaxException e) {
            FailureResult response_500 = new FailureResult("Error: description");
            return new Gson().toJson(response_500);
        }

        Collection<GameResult> games;

        try {
            games = service.listGames(authToken);
        } catch (DataAccessException e){
            res.status(401);
            FailureResult fail_response = new FailureResult("Error: unauthorized");
            return new Gson().toJson(fail_response);
        }

        res.status(200);
        return new Gson().toJson(new ListGameResult(games));
    }
}
