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
        System.out.print(SET_TEXT_COLOR_WHITE + client.inputUI());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            int line = client.parseInput(scanner);

            try {
                result = client.eval(line);
                System.out.print(SET_TEXT_COLOR_LIGHT_GREY + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
            System.out.print(SET_TEXT_COLOR_WHITE + client.inputUI());
        }
    }

    public void notify(Notification notification) {
        System.out.println(SET_TEXT_COLOR_RED + notification.getMessage());
        printPrompt();
    }

    public void loadGame(ChessGame game) {
        client.setBoard(game);
        client.redrawGameUI();
    }

    public void error(ErrorMessage error){
        System.out.println(SET_TEXT_COLOR_RED + error.getErrorMessage());
    }

    private void printPrompt() {
        System.out.print("\n" + ">>> " + SET_BG_COLOR_GREEN);
    }
}
