package server;

import dataAccess.DataAccessException;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;

public class RequestHandler extends Handler {

    public RequestHandler(){

    }

    public String handleRequest(Request req, Response res) throws DataAccessException {
        UserData userReq = (UserData)gson.fromJson(req.body(), UserData.class);

        UserService service = new UserService();
        String authToken = service.register(userReq);
        if (authToken == null){
            FailureResponse response_403 = new FailureResponse(403, "Error: already taken");
            return gson.toJson(response_403);
        } else {
            UserResponse response_200 = new UserResponse(200, userReq.getUsername(), authToken);
            return gson.toJson(response_200);
        }
    }
}

