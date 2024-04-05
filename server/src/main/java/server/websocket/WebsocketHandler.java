package server.websocket;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import webSocketMessages.ServerMessage;
import webSocketMessages.UserGameCommand;

import java.io.IOException;

public class WebsocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws DataAccessException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER, JOIN_OBSERVER -> joinGame(command.getAuthString(), session);
            case MAKE_MOVE -> makeMove(command.getAuthString());
            case LEAVE -> leaveGame(command.getAuthString());
            case RESIGN -> resignGame(command.getAuthString());
        }
    }

    private void joinGame(String authToken, Session session) throws DataAccessException {
        try {
            connections.add(authToken, session);
            var notification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
            connections.broadcast(authToken, notification);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private void makeMove(String authToken, Session session) throws DataAccessException {
        try {
            connections.add(authToken, session);
            var notification = new ;
            connections.broadcast(authToken, notification);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private void joinGame(String authToken, Session session) throws DataAccessException {
        try {
            connections.add(authToken, session);
            var notification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
            connections.broadcast(authToken, notification);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private void joinGame(String authToken, Session session) throws DataAccessException {
        try {
            connections.add(authToken, session);
            var notification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
            connections.broadcast(authToken, notification);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private void error(String authToken) throws IOException {
        connections.remove(authToken);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
        connections.broadcast(authToken, notification);
    }

    public void notification(String petName, String sound) throws DataAccessException {
        try {
            var message = String.format("%s says %s", petName, sound);
            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            connections.broadcast("", notification);
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
}
