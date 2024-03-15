package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.*;
import service.Service;

import java.io.*;
import java.net.*;
import java.util.Collection;

public class ServerFacade {

    private final ClientCommunicator communicator;

    public ServerFacade(String url){
        communicator = new ClientCommunicator(url);
    }

    public UserResult registerUser(UserData user) throws DataAccessException {
        var path = "/user";
        return communicator.makeRequest("POST", path, user, null, UserResult.class);
    }

    public UserResult loginUser(LoginData loginData) throws DataAccessException {
        var path = "/session";
        return communicator.makeRequest("POST", path, loginData, null, UserResult.class);
    }

    public void logoutUser(String authToken) throws DataAccessException {
        var path = "/session";
        communicator.makeRequest("DELETE", path, null, authToken, null);
    }

    public void deleteAll() throws DataAccessException {
        var path = "/db";
        communicator.makeRequest("DELETE", path, null, null, null);
    }

    public ListGameResult listGames(String authToken) throws DataAccessException {
        var path = "/game";
        return communicator.makeRequest("GET", path, null, authToken, ListGameResult.class);
    }

    public int createGame(String authToken, String gameName) throws DataAccessException {
        var path = "/game";
        return communicator.makeRequest("POST", path, gameName, authToken, int.class);
    }

    public void joinGame(String authToken, JoinGameData joinGameData) throws DataAccessException {
        var path = "/game";
        communicator.makeRequest("PUT", path, joinGameData, authToken, null);
    }
}
