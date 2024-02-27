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
        try {
            userReq = (UserData) new Gson().fromJson(req.body(), UserData.class);
        } catch(Exception e) {
            FailureResponse response_500 = new FailureResponse("Error: description");
            return new Gson().toJson(response_500);
        }

        String authToken = null;

        try {
            UserService service = new UserService();
            authToken = service.register(userReq);
        } catch (Exception e){
            FailureResponse response_400 = new FailureResponse("Error: bad request");
            return new Gson().toJson(response_400);
        }

        if (authToken == null){
            FailureResponse response_403 = new FailureResponse("Error: already taken");
            return new Gson().toJson(response_403);
        } else {
            UserResponse response_200 = new UserResponse(userReq.username(), authToken);
            return new Gson().toJson(response_200);
        }
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
            FailureResponse response_401 = new FailureResponse("Error: unauthorized");
            return new Gson().toJson(response_401);
        }

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
            FailureResponse response_401 = new FailureResponse("Error: unauthorized");
            return new Gson().toJson(response_401);
        }

        int gameID = 0;

        try {
            gameID = service.createGame(gameName);
        } catch (DataAccessException e){
            FailureResponse response_400 = new FailureResponse("Error: bad request");
            return new Gson().toJson(response_400);
        }

        res.status(200);
        return new Gson().toJson(new GameIDResponse(gameID));
    }

    public Object joinGame(Request req, Response res) {
        String authToken = null;
        JoinGameData gameReqData;
        try {
            authToken = req.headers("authorization");
            gameReqData = (JoinGameData) new Gson().fromJson(req.body(), JoinGameData.class);
        } catch(JsonSyntaxException e) {
            FailureResponse response_500 = new FailureResponse("Error: description");
            return new Gson().toJson(response_500);
        }

        try {
            service.joinGame(gameReqData, authToken);
        } catch (DataAccessException e){
            FailureResponse fail_response = new FailureResponse("Error: unauthorized");
            return new Gson().toJson(fail_response);
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
            FailureResponse fail_response = new FailureResponse("Error: unauthorized");
            return new Gson().toJson(fail_response);
        }

        res.status(200);
        return new Gson().toJson(new ListGameResponse(games));
    }
}
