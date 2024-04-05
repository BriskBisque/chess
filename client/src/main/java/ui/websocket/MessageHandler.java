package client.websocket;

import webSocketMessages.Notification;

public interface MessageHandler {
    void notify(Notification message);
}