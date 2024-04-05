package ui.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import org.glassfish.tyrus.core.wsadl.model.Endpoint;
import server.Server;
import webSocketMessages.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    MessageHandler messageHandler;


    public WebSocketFacade(String url, MessageHandler messageHandler) throws DataAccessException {
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
                    Notification notification = new Gson().fromJson(message, Notification.class);
                    messageHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void joinPlayer(String authToken, int gameID, ChessGame.TeamColor teamColor) throws DataAccessException {
        try {
            var command = new JoinPlayer(authToken, gameID, teamColor);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
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

}

