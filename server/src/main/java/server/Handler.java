package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataAccess.DataAccessException;
import model.LoginData;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;

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
            FailureResponse response_500 = new FailureResponse(false, "Error: description");
            return new Gson().toJson(response_500);
        }

        String authToken = null;

        try {
            UserService service = new UserService();
            authToken = service.register(userReq);
        } catch (Exception e){
            FailureResponse response_400 = new FailureResponse(false, "Error: bad request");
            return new Gson().toJson(response_400);
        }

        if (authToken == null){
            FailureResponse response_403 = new FailureResponse(false, "Error: already taken");
            return new Gson().toJson(response_403);
        } else {
            UserResponse response_200 = new UserResponse(true, userReq.username(), authToken);
            return new Gson().toJson(response_200);
        }
    }

    public Object clear(Request req, Response res){
        try {
            service.clear();
        } catch (Exception e){
            FailureResponse response_500 = new FailureResponse(false, "Error: description");
            return new Gson().toJson(response_500);
        }
        res.status(200);
        return new Gson().toJson(new Result(true));
    }

    public Object login(Request req, Response res) throws DataAccessException {
        LoginData userReq;
        try {
            userReq = (LoginData) new Gson().fromJson(req.body(), LoginData.class);
        } catch(JsonSyntaxException e) {
            FailureResponse response_500 = new FailureResponse(false, "Error: description");
            return new Gson().toJson(response_500);
        }

        String authToken = null;

        UserService service = new UserService();
        authToken = service.login(userReq);

        if (authToken == null){
            FailureResponse response_401 = new FailureResponse(false, "Error: unauthorized");
            return new Gson().toJson(response_401);
        } else {
            UserResponse response_200 = new UserResponse(true, userReq.username(), authToken);
            return new Gson().toJson(response_200);
        }
    }
}
