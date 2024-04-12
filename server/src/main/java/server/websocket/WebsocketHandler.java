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

import java.io.IOException;

@WebSocket
public class WebsocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final Service service = new Service();

    public WebsocketHandler() throws DataAccessException {
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws DataAccessException, IOException {
        var command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> joinGamePlayer(session, message);
            case JOIN_OBSERVER -> joinGameObserver(session, message);
            case MAKE_MOVE -> makeMove(session, message);
            case LEAVE -> leaveGame(session, message);
            case RESIGN -> resignGame(session, message);
        }
    }

    private void joinGamePlayer(Session session, String message) throws DataAccessException, IOException {
        var command = new Gson().fromJson(message, JoinPlayer.class);
        String authToken = command.getAuthString();
        try {
            String username = service.getUsername(authToken);
            ChessGame.TeamColor playerColor = command.getTeamColor();

            GameData gameData = service.getGame(command.getGameID());
            sendGame(gameData, session, authToken);
            var notification = new Notification(username + " has joined as the " + playerColor.toString() + " player.");
            connections.broadcast(authToken, new Gson().toJson(notification));
        } catch (Exception e) {
            error(session, authToken, e);
        }
    }

    private void sendGame(GameData gameData, Session session, String authToken) throws IOException {
        ChessGame game = gameData.game();

        var loadGame = new LoadGame(new Gson().toJson(game));
        Connection playerConnection = new Connection(authToken, session);
        connections.notifyPlayer(playerConnection, new Gson().toJson(loadGame));
        connections.add(authToken, session);
    }

    private void joinGameObserver(Session session, String message) throws IOException {
        var command = new Gson().fromJson(message, JoinObserver.class);
        String authToken = command.getAuthString();
        try {

            String username = service.getUsername(authToken);
            GameData gameData = service.getGame(command.getGameID());

            sendGame(gameData, session, authToken);
            var notification = new Notification(username + " has joined as an observer.");
            connections.broadcast(authToken, new Gson().toJson(notification));
        } catch (Exception e) {
            error(session, authToken, e);
        }
    }

    private void makeMove(Session session, String message) throws IOException, DataAccessException {
        var command = new Gson().fromJson(message, MakeMove.class);
        String authToken = command.getAuthString();
        if(service.checkIsResigned(command.getGameID())){
            error(session, authToken, new Exception("Game has been resigned"));
            return;
        }
        try {
            GameData gameData = service.getGame(command.getGameID());
            ChessGame game = gameData.game();
            game.makeMove(command.getMove());
            var gameString = new Gson().toJson(game);
            service.setGame(gameString, command.getGameID());

            var loadGame = new LoadGame(gameString);
            connections.broadcast(authToken, new Gson().toJson(loadGame));
            sendGame(gameData, session, authToken);
            var notification = new Notification("A move has been made.");
            connections.broadcast(authToken, new Gson().toJson(notification));
        } catch (Exception e) {
            error(session, authToken, e);
        }
    }

    public void leaveGame(Session session, String message) throws IOException {
        var command = new Gson().fromJson(message, Leave.class);
        String authToken = command.getAuthString();
        try {
            String username = service.getUsername(authToken);
            connections.remove(authToken);
            var notification = new Notification(username + " has left the game.");
            connections.broadcast(authToken, new Gson().toJson(notification));
        } catch (Exception e) {
            error(session, authToken, e);
        }
    }

    public void resignGame(Session session, String message) throws IOException {
        var command = new Gson().fromJson(message, Resign.class);
        String authToken = command.getAuthString();
        try {
            String username = service.getUsername(authToken);
            service.setGameResign(command.getGameID(), username);
            connections.remove(authToken);
            var notification = new Notification(username + " has resigned.");
            Connection connection = new Connection(authToken, session);
            connections.notifyPlayer(connection, new Gson().toJson(notification));
            connections.broadcast(authToken, new Gson().toJson(notification));
        } catch (Exception e) {
            error(session, authToken, e);
        }
    }

    public void error(Session session, String authToken, Exception exception) throws IOException {
        var error = new ErrorMessage("Error: " + exception.toString());
        Connection playerConnection = new Connection(authToken, session);
        connections.notifyPlayer(playerConnection, new Gson().toJson(error));
    }

}
