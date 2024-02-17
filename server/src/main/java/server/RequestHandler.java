package server;

import server.Handler;
import service.RegisterService;
import spark.Request;
import spark.Response;

public class RequestHandler extends Handler {

    static RegisterService service;

    public RequestHandler(){
        service = new RegisterService();
    }

    public String handleRequest(Request req, Response res){
        String username = req.queryParams("username");
        String password = req.queryParams("password");
        String email = req.queryParams("email");

        String

        return "Hello, " + username + "!";
    }
}
