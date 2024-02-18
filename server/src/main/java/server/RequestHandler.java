package server;

import dataAccess.DataAccessException;
import model.UserData;
import server.Handler;
import service.UserService;
import spark.Request;
import spark.Response;

public class RequestHandler extends Handler {

    public RequestHandler(){

    }

    public String handleRequest(Request req, Response res) throws DataAccessException {
        UserData userReq = (UserData)gson.fromJson(req.body(), UserData.class);

        UserService service = new UserService();
        RegisterResponse result = service.register(userReq);

        return gson.toJson(result);
    }
}

