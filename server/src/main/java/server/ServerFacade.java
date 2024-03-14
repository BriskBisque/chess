package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import exception.ResponseException;
import model.GameResult;
import model.ListGameResult;
import model.LoginData;
import model.Pet;
import model.UserData;

import java.io.*;
import java.net.*;
import java.util.Collection;

public class ServerFacade {

    private final ClientCommunicator communicator = new ClientCommunicator();

    public UserData registerUser(UserData user) throws DataAccessException {
        var path = "/user";
        return communicator.makeRequest("POST", path, user, null, UserData.class);
    }

    public UserData loginUser(LoginData loginData) throws DataAccessException {
        var path = "/session";
        return communicator.makeRequest("POST", path, loginData, null, UserData.class);
    }

    public void logoutUser(String authToken) throws DataAccessException {
        var path = "/session";
        communicator.makeRequest("DELETE", path, null, authToken, null);
    }

    public void deleteAll() throws DataAccessException {
        var path = "/db";
        communicator.makeRequest("DELETE", path, null, null, null);
    }

    public Collection<GameResult> listGames(String authToken) throws DataAccessException {
        var path = "/game";
        return communicator.makeRequest("GET", path, null, authToken, ListGameResult.class).games();
    }

    public int createGame(String authToken, String gameName) throws DataAccessException {
        var path = "/game";
        return communicator.makeRequest("POST", path, gameName, authToken, int.class);
    }

    public void joinGame(String authToken, String playerColor) throws DataAccessException {
        var path = "/game";
        communicator.makeRequest("PUT", path, playerColor, authToken, null);
    }
}
