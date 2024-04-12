package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
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
import java.util.Objects;

import static java.lang.Character.getNumericValue;

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

    private void joinGamePlayer(Session session, String message) throws IOException {
        var command = new Gson().fromJson(message, JoinPlayer.class);
        String authToken = command.getAuthString();
        try {
            service.testAuth(authToken);
            String username = service.getUsername(authToken);
            ChessGame.TeamColor playerColor = command.getPlayerColor();
            GameData gameData = service.getGame(command.getGameID());
            String playerString = "null";

            if (playerColor == ChessGame.TeamColor.WHITE ){
                playerString = "WHITE";
                if (!Objects.equals(gameData.whiteUsername(), username)) {
                    throw new Exception("White color taken.");
                } else if (Objects.equals(gameData.blackUsername(), username)) {
                    throw new Exception("You already joined as the black user.");
                }
            } else if (playerColor == ChessGame.TeamColor.BLACK) {
                playerString = "BLACK";
                if (!Objects.equals(gameData.blackUsername(), username)) {
                    throw new Exception("White color taken.");
                } else if (Objects.equals(gameData.whiteUsername(), username)) {
                    throw new Exception("You already joined as the black user.");
                }
            }

            sendGame(gameData, session, authToken);
            var notification = new Notification(username + " has joined as the " + playerString + " player.");
            connections.broadcast(authToken, new Gson().toJson(notification), gameData.gameID());
        } catch (Exception e) {
            error(session, authToken, e, command.getGameID());
        }
    }

    private void sendGame(GameData gameData, Session session, String authToken) throws IOException {
        ChessGame game = gameData.game();

        var loadGame = new LoadGame(new Gson().toJson(game));
        Connection playerConnection = new Connection(authToken, session, gameData.gameID());
        connections.notifyPlayer(playerConnection, new Gson().toJson(loadGame));
        connections.add(authToken, session, gameData.gameID());
    }

    private void joinGameObserver(Session session, String message) throws IOException {
        var command = new Gson().fromJson(message, JoinObserver.class);
        String authToken = command.getAuthString();
        try {
            service.testAuth(authToken);
            String username = service.getUsername(authToken);
            GameData gameData = service.getGame(command.getGameID());

            sendGame(gameData, session, authToken);
            var notification = new Notification(username + " has joined as an observer.");
            connections.broadcast(authToken, new Gson().toJson(notification), gameData.gameID());
        } catch (Exception e) {
            error(session, authToken, e, command.getGameID());
        }
    }

    private void makeMove(Session session, String message) throws IOException, DataAccessException {
        var command = new Gson().fromJson(message, MakeMove.class);
        String authToken = command.getAuthString();
        try {
            String username = service.getUsername(authToken);
            GameData gameData = service.getGame(command.getGameID());
            ChessGame game = gameData.game();
            ChessGame.TeamColor playerColor = service.checkUserColor(username, gameData);
            ChessGame.TeamColor pieceColor = game.getBoard().getPiece(command.getMove().startPos).getTeamColor();
            if (pieceColor != playerColor) {
                throw new DataAccessException("You can only move your pieces.");
            }
            ChessMove move = command.getMove();
            game.makeMove(move);
            var gameString = new Gson().toJson(game);
            service.setGame(gameString, command.getGameID());

            char startCol = (char)(move.startPos.col+96);
            char endCol = (char)(move.endPos.col+96);

            if (playerColor == ChessGame.TeamColor.BLACK){

            }

            var loadGame = new LoadGame(gameString);
            connections.broadcast(authToken, new Gson().toJson(loadGame), gameData.gameID());
            sendGame(gameData, session, authToken);
            var notification = new Notification("A move has been made: " + startCol + (9-move.startPos.row) + " to " + endCol + (9-move.endPos.row) + ".");
            connections.broadcast(authToken, new Gson().toJson(notification), gameData.gameID());
        } catch (Exception e) {
            error(session, authToken, e, command.getGameID());
        }
    }

    public void leaveGame(Session session, String message) throws IOException {
        var command = new Gson().fromJson(message, Leave.class);
        String authToken = command.getAuthString();
        try {
            String username = service.getUsername(authToken);
            connections.remove(authToken);
            var notification = new Notification(username + " has left the game.");
            connections.broadcast(authToken, new Gson().toJson(notification), command.getGameID());
        } catch (Exception e) {
            error(session, authToken, e, command.getGameID());
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
            Connection connection = new Connection(authToken, session, command.getGameID());
            connections.notifyPlayer(connection, new Gson().toJson(notification));
            connections.broadcast(authToken, new Gson().toJson(notification), connection.gameID);
        } catch (Exception e) {
            error(session, authToken, e, command.getGameID());
        }
    }

    public void error(Session session, String authToken, Exception exception, int gameID) throws IOException {
        var error = new ErrorMessage("Error: " + exception.toString());
        Connection playerConnection = new Connection(authToken, session, gameID);
        connections.notifyPlayer(playerConnection, new Gson().toJson(error));
    }

}
