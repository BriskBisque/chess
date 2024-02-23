package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataAccess.DataAccessException;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;

public class RequestHandler {

    static Gson gson;
    static UserService service;

    public RequestHandler() {
        gson = new Gson();
        service = new UserService();
    }

    public String handleRegister(Request req, Response res) throws DataAccessException {
        boolean descriptionCheck = false;
        UserData userReq;
        try {
            userReq = (UserData) gson.fromJson(req.body(), UserData.class);
        } catch(JsonSyntaxException e) {
            descriptionCheck = true;
            userReq = null;
        }

        if (descriptionCheck){
            FailureResponse response_500 = new FailureResponse("Error: description");
            return gson.toJson(response_500);
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
            return gson.toJson(response_400);
        }

        if (authToken == null){
            FailureResponse response_403 = new FailureResponse("Error: already taken");
            return gson.toJson(response_403);
        } else {
            UserResponse response_200 = new UserResponse((String) userReq.getUsername(), authToken);
            return gson.toJson(response_200);
        }
    }

    public String handleClear(Request req, Response res){
        service.clear();
        res.status(200);
        return "";
    }
}

