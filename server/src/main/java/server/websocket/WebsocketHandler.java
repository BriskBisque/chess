package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.Service;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.*;

@WebSocket
public class WebsocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final Service service = new Service();

    public WebsocketHandler() throws DataAccessException {
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws DataAccessException {
        var command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> joinGamePlayer(session, message);
            case JOIN_OBSERVER -> joinGameObserver(session, message);
            case MAKE_MOVE -> makeMove(message);
            case LEAVE -> leaveGame(session, message);
            case RESIGN -> resignGame(session, message);
        }
    }

    private void joinGamePlayer(Session session, String message) throws DataAccessException {
        var command = new Gson().fromJson(message, JoinPlayer.class);
        String authToken = command.getAuthString();
        System.out.print("TESTING HANDLER");
        try {
            String username = service.getUsername(authToken);
            ChessGame.TeamColor playerColor = command.getTeamColor();
            GameData game = service.getGame(command.getGameID());
            connections.add(authToken, session);
            var notification = new Notification(username + " has joined as the " + playerColor.toString() + " player.");
            connections.broadcast(authToken, notification);
        } catch (Exception e) {
            error(authToken, e);
        }
    }

    private void joinGameObserver(Session session, String message) throws DataAccessException {
        var command = new Gson().fromJson(message, JoinObserver.class);
        String authToken = command.getAuthString();
        try {
            String username = service.getUsername(authToken);
            GameData game = service.getGame(command.getGameID());
            connections.add(authToken, session);
            var notification = new Notification(username + " has joined " + game + "as an observer.");
            connections.broadcast(authToken, notification);
        } catch (Exception e) {
            error(authToken, e);
        }
    }

    private void makeMove(String message) {
        var command = new Gson().fromJson(message, MakeMove.class);
        String authToken = command.getAuthString();
        try {
            GameData game = service.getGame(command.getGameID());
            var loadGame = new LoadGame(game.toString());
            connections.sendGame(authToken, loadGame);
            var notification = new Notification("A move has been made.");
            connections.broadcast(authToken, notification);
        } catch (Exception e) {
            error(authToken, e);
        }
    }

    public void leaveGame(Session session, String message) {
        var command = new Gson().fromJson(message, Leave.class);
        String authToken = command.getAuthString();
        try {
            String username = service.getUsername(authToken);
            connections.remove(authToken);
            var notification = new Notification(username + " has left the game.");
            connections.broadcast(authToken, notification);
        } catch (Exception e) {
            error(authToken, e);
        }
    }

    public void resignGame(Session session, String message){
        var command = new Gson().fromJson(message, Resign.class);
        String authToken = command.getAuthString();
        try {
            String username = service.getUsername(authToken);
            connections.remove(authToken);
            var notification = new Notification(username + " has resigned.");
            connections.broadcast(authToken, notification);
        } catch (Exception e) {
            error(authToken, e);
        }
    }

    public void error(String authToken, Exception exception){
        try {
            var error = new ErrorMessage(exception.getMessage());
            connections.sendError(authToken, error);
        } catch (Exception ignored){
        }
    }

}
