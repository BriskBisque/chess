package ui;

import dataAccess.DataAccessException;
import model.*;
import model.Results.GameIDResult;
import model.Results.ListGameResult;
import model.Results.UserResult;
import server.GameNameResponse;
import ui.websocket.WebSocketFacade;

import java.util.Objects;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public class ServerFacade {

    private final ClientCommunicator communicator;
    private final WebSocketFacade ws;

    public ServerFacade(String url, client.websocket.MessageHandler messageHandler) throws DataAccessException {
        communicator = new ClientCommunicator(url);
        ws = new WebSocketFacade(url, messageHandler);
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

    public int createGame(String authToken, GameNameResponse gameName) throws DataAccessException {
        var path = "/game";
        return communicator.makeRequest("POST", path, gameName, authToken, GameIDResult.class).gameID();
    }

    public void joinGame(String authToken, JoinGameData joinGameData) throws DataAccessException {
        var path = "/game";
        communicator.makeRequest("PUT", path, joinGameData, authToken, null);
        if (Objects.equals(joinGameData.playerColor(), "WHITE")) {
            ws.joinPlayer(authToken, joinGameData.gameID(), WHITE);
        } else if (Objects.equals(joinGameData.playerColor(), "BLACK")){
            ws.joinPlayer(authToken, joinGameData.gameID(), BLACK);
        } else {
            ws.joinObserver(authToken, joinGameData.gameID());
        }
    }
}
