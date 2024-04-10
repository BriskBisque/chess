package ui;

import chess.ChessGame;
import webSocketMessages.ErrorMessage;
import webSocketMessages.Notification;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl implements client.websocket.MessageHandler {
    private final Client client;

    public Repl(String serverUrl) {
        client = new Client(serverUrl, this);
    }

    public void run() {
        System.out.print(SET_BG_COLOR_BLACK);
        System.out.println("Chess :D");
        System.out.println(SET_TEXT_COLOR_WHITE + client.inputUI());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            client.printPrompt();
            int line = client.parseInput(scanner);

            try {
                result = client.eval(line);
                System.out.print(SET_TEXT_COLOR_LIGHT_GREY + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.println(SET_TEXT_COLOR_BLUE + SET_BG_COLOR_BLACK + msg);
            }
            if (!result.equals("quit")) {
                client.setUIColor();
                System.out.println(client.inputUI());
            }
        }
    }

    public void notify(Notification notification) {
        System.out.println(SET_TEXT_COLOR_RED + notification.getMessage());
        client.printPrompt();
    }

    public void loadGame(ChessGame game) {
        client.setBoard(game);
        client.redrawGameUI();
    }

    public void error(ErrorMessage error){
        System.out.println(SET_TEXT_COLOR_RED + error.getErrorMessage());
    }
}
