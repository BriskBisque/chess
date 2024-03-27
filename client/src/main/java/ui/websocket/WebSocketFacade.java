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
    NotificationHandler messageHandler;


    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws DataAccessException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                    switch (message){
                        case "Update" -> ;
                    }
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

    public void leaveGame() throws DataAccessException {
        try {
            var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            this.session.getBasicRemote().sendText(new Gson().toJson(serverMessage));
            this.session.close();
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

}

