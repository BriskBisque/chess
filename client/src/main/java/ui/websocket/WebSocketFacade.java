package ui.websocket;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import exception.ResponseException;
import org.glassfish.tyrus.core.wsadl.model.Endpoint;
import server.Server;
import webSocketMessages.Action;
import webSocketMessages.Notification;
import webSocketMessages.ServerMessage;
import webSocketMessages.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    MessageHandler messageHandler;


    public WebSocketFacade(String url) throws DataAccessException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
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

    public void joinPlayer(String authToken) throws DataAccessException {
        try {
            var command = new UserGameCommand(authToken);
            command.setCommandType(UserGameCommand.CommandType.JOIN_PLAYER);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void joinObserver(String authToken) throws DataAccessException {
        try {
            var command = new UserGameCommand(authToken);
            command.setCommandType(UserGameCommand.CommandType.JOIN_OBSERVER);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void makeMove(String authToken) throws DataAccessException {
        try {
            var command = new UserGameCommand(authToken);
            command.setCommandType(UserGameCommand.CommandType.MAKE_MOVE);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void resignGame(String authToken) throws DataAccessException {
        try {
            var command = new UserGameCommand(authToken);
            command.setCommandType(UserGameCommand.CommandType.RESIGN);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void leaveGame(String authToken) throws DataAccessException {
        try {
            var command = new UserGameCommand(authToken);
            command.setCommandType(UserGameCommand.CommandType.LEAVE);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

}

