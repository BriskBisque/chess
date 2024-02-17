package server;

import spark.*;

public class Handler {

    public Handler(){

    }

    public String handleRequest(Request req, Response res){
        String authToken = req.queryParams("authToken");



        return authToken;
    }
}
