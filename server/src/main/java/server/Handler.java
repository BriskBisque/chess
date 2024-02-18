package server;

import spark.*;
import com.google.gson.Gson;

public class Handler {
    static Gson gson;

    public Handler() {
        gson = new Gson();
    }

    public String handleRequest(Request req, Response res) {
        String authToken = req.queryParams("authToken");


        return authToken;
    }
}
