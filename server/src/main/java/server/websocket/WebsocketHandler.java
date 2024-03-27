package server.websocket;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataaccess.DataAccess;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import webSocketMessages.Action;
import webSocketMessages.Notification;
import webSocketMessages.ServerMessage;

import java.io.IOException;

public class WebsocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws DataAccessException {
        ServerMessage action = new Gson().fromJson(message, ServerMessage.class);
        switch (action.getServerMessageType()) {
            case LOAD_GAME -> load(action.visitorName(), session);
            case ERROR -> error(action.visitorName());
            case NOTIFICATION -> notification();
        }
    }

    private void load(String visitorName, Session session) throws DataAccessException {
        connections.add(visitorName, session);
        var message = String.format("%s is in the shop", visitorName);
        var notification = new Notification(Notification.Type.ARRIVAL, message);
        connections.broadcast(visitorName, notification);
    }

    private void error(String visitorName) throws IOException {
        connections.remove(visitorName);
        var message = String.format("%s left the shop", visitorName);
        var notification = new Notification(Notification.Type.DEPARTURE, message);
        connections.broadcast(visitorName, notification);
    }

    public void notification(String petName, String sound) throws DataAccessException {
        try {
            var message = String.format("%s says %s", petName, sound);
            var notification = new Notification(Notification.Type.NOISE, message);
            connections.broadcast("", notification);
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
}
