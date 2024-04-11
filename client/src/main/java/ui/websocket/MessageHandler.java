package client.websocket;

import chess.ChessGame;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.Notification;

public interface MessageHandler {
    void notify(Notification message);
    void loadGame(ChessGame game);
    void error(ErrorMessage error);
}