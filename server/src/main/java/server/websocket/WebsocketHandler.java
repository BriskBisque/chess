package server.websocket;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import webSocketMessages.*;
import webSocketMessages.ErrorMessage;

import java.io.IOException;

public class WebsocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws DataAccessException {
        var command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> joinGamePlayer(session, message);
            case JOIN_OBSERVER -> joinGameObserver(session, message);
            case MAKE_MOVE -> makeMove(session, message);
            case LEAVE -> leaveGame(session, message);
            case RESIGN -> resignGame(session, message);
        }
    }

    private void joinGamePlayer(Session session, String message) throws DataAccessException {
        var command = new Gson().fromJson(message, JoinPlayer.class);
        String authToken = command.getAuthString();
        int gameID = command.getGameID();

        try {
            connections.add(authToken, session);
            var notification = new Notification(message);
            connections.broadcast(authToken, notification);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private void joinGameObserver(Session session, String message) throws DataAccessException {
        var command = new Gson().fromJson(message, JoinObserver.class);
        String authToken = command.getAuthString();
        int gameID = command.getGameID();
        try {
            connections.add(authToken, session);
            var notification = new Notification(message);
            connections.broadcast(authToken, notification);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private void makeMove(Session session, String message) throws DataAccessException {
        var command = new Gson().fromJson(message, MakeMove.class);
        String authToken = command.getAuthString();
        int gameID = command.getGameID();
        try {
            var notification = new Notification(message);
            connections.broadcast(authToken, notification);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void leaveGame(Session session, String message) throws DataAccessException {
        var command = new Gson().fromJson(message, Leave.class);
        String authToken = command.getAuthString();
        int gameID = command.getGameID();
        try {
            connections.remove(authToken);
            var notification = new Notification(message);
            connections.broadcast(authToken, notification);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void resignGame(Session session, String message) throws DataAccessException {
        var command = new Gson().fromJson(message, Resign.class);
        String authToken = command.getAuthString();
        int gameID = command.getGameID();
        try {
            connections.remove(authToken);
            var notification = new Notification(message);
            connections.broadcast(authToken, notification);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private void error(String authToken) throws IOException {
        connections.remove(authToken);
        var notification = new ErrorMessage();
        connections.broadcast(authToken, notification);
    }
}
