package client.websocket;

import webSocketMessages.ServerMessage;

public interface MessageHandler {
    void notify(ServerMessage message);
}