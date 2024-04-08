package client.websocket;

import chess.ChessGame;
import webSocketMessages.ErrorMessage;
import webSocketMessages.Notification;

public interface MessageHandler {
    void notify(Notification message);
    void loadGame(ChessGame game);
    void error(ErrorMessage error);
}