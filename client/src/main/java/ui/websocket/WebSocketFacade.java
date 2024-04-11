package ui.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    client.websocket.MessageHandler messageHandler;


    public WebSocketFacade(String url, client.websocket.MessageHandler messageHandler) throws DataAccessException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.messageHandler = messageHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    switch (serverMessage.getServerMessageType()){
                        case LOAD_GAME -> loadGame(message);
                        case ERROR -> error(message);
                        case NOTIFICATION -> notification(message);
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void loadGame(String serverMessage){
        LoadGame message = new Gson().fromJson(serverMessage, LoadGame.class);
        ChessGame game = new Gson().fromJson(message.game, ChessGame.class);
        messageHandler.loadGame(game);
    }

    public void error(String serverMessage){
        ErrorMessage error = new Gson().fromJson(serverMessage, ErrorMessage.class);
        messageHandler.error(error);
    }

    public void notification(String serverMessage){
        Notification notification = new Gson().fromJson(serverMessage, Notification.class);
        messageHandler.notify(notification);
    }

    public void joinPlayer(String authToken, int gameID, ChessGame.TeamColor teamColor) throws DataAccessException {
        try {
            var command = new JoinPlayer(authToken, gameID, teamColor);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
            System.out.println("TESTING FACADE");
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void joinObserver(String authToken, int gameID) throws DataAccessException {
        try {
            var command = new JoinObserver(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws DataAccessException {
        try {
            var command = new MakeMove(authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void resignGame(String authToken, int gameID) throws DataAccessException {
        try {
            var command = new Resign(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void leaveGame(String authToken, int gameID) throws DataAccessException {
        try {
            var command = new Leave(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}

