package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataAccess.DataAccessException;
import model.JoinGameData;
import model.LoginData;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;

import java.util.Collection;
import java.util.Objects;

public class Handler {

    private final UserService service = UserService.getInstance();
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
            FailureResponse response_400 = new FailureResponse("Error: bad request");
            return new Gson().toJson(response_400);
        }

        String authToken = null;

        try {
            UserService service = new UserService();
            authToken = service.register(userReq);
        } catch (DataAccessException e){
            res.status(403);
            FailureResponse response_403 = new FailureResponse("Error: already taken");
            return new Gson().toJson(response_403);
        }

        UserResponse response_200 = new UserResponse(userReq.username(), authToken);
        return new Gson().toJson(response_200);

    }

    public Object clear(Request req, Response res){
        try {
            service.clear();
        } catch (Exception e){
            FailureResponse response_500 = new FailureResponse("Error: description");
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
            FailureResponse response_500 = new FailureResponse("Error: description");
            return new Gson().toJson(response_500);
        }

        String authToken = null;

        try {
            authToken = service.login(userReq);
        } catch (DataAccessException e){
            res.status(401);
            FailureResponse response_401 = new FailureResponse("Error: unauthorized");
            return new Gson().toJson(response_401);
        }

        res.status(200);
        UserResponse response_200 = new UserResponse(userReq.username(), authToken);
        return new Gson().toJson(response_200);
    }

    public Object logout(Request req, Response res){
        String authToken = null;
        try {
            authToken = req.headers("authorization");
        } catch(JsonSyntaxException e) {
            FailureResponse response_500 = new FailureResponse("Error: description");
            return new Gson().toJson(response_500);
        }

        try {
            service.logout(authToken);
        } catch (DataAccessException e){
            res.status(401);
            FailureResponse response_401 = new FailureResponse("Error: unauthorized");
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
            FailureResponse response_500 = new FailureResponse("Error: description");
            return new Gson().toJson(response_500);
        }

        try {
            service.testAuth(authToken);
        } catch (DataAccessException e){
            res.status(401);
            FailureResponse response_401 = new FailureResponse("Error: unauthorized");
            return new Gson().toJson(response_401);
        }

        int gameID = 0;

        try {
            gameID = service.createGame(gameName);
        } catch (DataAccessException e){
            res.status(400);
            FailureResponse response_400 = new FailureResponse("Error: bad request");
            return new Gson().toJson(response_400);
        }

        res.status(200);
        return new Gson().toJson(new GameIDResponse(gameID));
    }

    public Object joinGame(Request req, Response res) {
        JoinGameData gameReqData;

        String authToken = req.headers("authorization");
        gameReqData = new Gson().fromJson(req.body(), JoinGameData.class);

        if ((!Objects.equals(gameReqData.playerColor(), "WHITE") && !Objects.equals(gameReqData.playerColor(), "BLACK") && !Objects.equals(gameReqData.playerColor(), null)) || Objects.equals(gameReqData.gameID(), 0)){
            res.status(400);
            FailureResponse response_400 = new FailureResponse("Error: bad request");
            return new Gson().toJson(response_400);
        }

        try {
            service.testAuth(authToken);
        } catch (DataAccessException e){
            res.status(401);
            FailureResponse response_401 = new FailureResponse("Error: unauthorized");
            return new Gson().toJson(response_401);
        }

        try {
            service.joinGame(gameReqData, authToken);
        } catch (DataAccessException e){
            res.status(403);
            FailureResponse response_403 = new FailureResponse("Error: already taken");
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
            FailureResponse response_500 = new FailureResponse("Error: description");
            return new Gson().toJson(response_500);
        }

        Collection<GameResponseData> games;

        try {
            games = service.listGames(authToken);
        } catch (DataAccessException e){
            res.status(401);
            FailureResponse fail_response = new FailureResponse("Error: unauthorized");
            return new Gson().toJson(fail_response);
        }

        res.status(200);
        return new Gson().toJson(new ListGameResponse(games));
    }
}
