package ui.websocket;

import chess.ChessGame;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;

public interface NotificationHandler {
    void notify(Notification message);
    void loadGame(LoadGame loadGame);
    void error(ErrorMessage error);
}