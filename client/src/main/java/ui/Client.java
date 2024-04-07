package ui;

import dataAccess.DataAccessException;
import model.*;
import model.Results.GameResult;
import model.Results.ListGameResult;
import model.Results.UserResult;
import server.GameNameResponse;
import ui.websocket.WebSocketFacade;

import java.util.Collection;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Client {

    private final ServerFacade facade;
    private final Scanner scanner;
    private String username = null;
    private String authToken = null;
    private State state = State.SIGNEDOUT;
    private final String serverUrl;
    private final client.websocket.MessageHandler messageHandler;
    private WebSocketFacade ws;

    public Client(String serverUrl, client.websocket.MessageHandler messageHandler){
        scanner = new Scanner(System.in);
        facade = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.messageHandler = messageHandler;
    }

    public String inputUI() {
        if (this.state == State.SIGNEDOUT) {
            return """
                    Enter a number:\s
                    1. Register\s
                    2. Login\s
                    3. Quit\s
                    4. Help""";
        } else if (this.state == State.SIGNEDIN) {
            return """
                Enter a number:\s
                1. Create Game\s
                2. Join Game\s
                3. Join as Observer\s
                4. List Games\s
                5. Logout\s
                6. Help""";
        } else if (this.state == State.INGAME){
            return """
                Enter just the number of the option you want\s
                1. Make Move\s
                2. Highlight Legal Moves\s
                3. Redraw Chess Board\s
                4. Leave\s
                5. Resign\s
                6. Help
                """;
        } else if (this.state == State.OBSERVING){
            return """
                    You are currently observing a game.\s
                    Enter 1 to leave.""";
        }
        return "quit";
    }

    public int parseInput(Scanner scanner){
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (Exception e){
            System.out.println("Please enter a number.");
            return parseInput(scanner);
        }
    }

    public String eval(int userInput) throws DataAccessException {
        if (this.state == State.SIGNEDOUT){
            switch (userInput) {
                case 1 -> {return registerUI();}
                case 2 -> {return loginUI();}
                case 3 -> {
                    return "quit";
                }
                default -> {return """
                Enter just the number of the option you want to pick.\s
                1. Register will register you as a user.\s
                2. Login will log you in.\s
                3. Quits\s
                Anything else, you will get a help menu.""";}
            }
        } else if (this.state == State.SIGNEDIN){
            switch (userInput) {
                case 1 -> {return createGameUI();}
                case 2 -> {return joinGameUI();}
                case 3 -> {return joinObserverUI();}
                case 4 -> {return listGamesUI();}
                case 5 -> {return logoutUI();}
                default -> {return """
                Enter just the number of the option you want to pick.\s
                1. Creates a new game with the name given.\s
                2. Joins a game of the number given in the color specified.\s
                3. Joins a game of the number given as a non-player.\s
                4. Lists the games in the system, numbered by ID.\s
                5. Logs the user out.\s
                Anything else, you will get a help menu.""";}
            }
        } else if (this.state == State.INGAME){
            switch (userInput) {
                case 1 -> {return makeMoveUI();}
                case 2 -> {return highlightGameUI();}
                case 3 -> {return redrawGameUI();}
                case 4 -> {return leaveGameUI();}
                case 5 -> {return resignGameUI();}
                default -> {return """
                Enter just the number of the option you want to pick.\s
                1. Enter a start location and an end location to make a move.\s
                2. Enter a location to see what moves the piece can make.\s
                3. Redraws the board.\s
                4. Leaves the game without resigning.\s
                5. Forfeits the game.\s
                Anything else, you will get a help menu.""";}
            }
        }
        return "quit";
    }

    private String registerUI() throws DataAccessException {
        System.out.println(SET_TEXT_COLOR_WHITE + "Please give a username:");
        System.out.print("\n" + ">>> " + SET_TEXT_COLOR_GREEN);
        String username = scanner.nextLine();
        System.out.println(SET_TEXT_COLOR_WHITE + "Please give a password:");
        System.out.print("\n" + ">>> " + SET_TEXT_COLOR_GREEN);
        String password = scanner.nextLine();
        System.out.println(SET_TEXT_COLOR_WHITE + "Please give an email:");
        System.out.print("\n" + ">>> " + SET_TEXT_COLOR_GREEN);
        String email = scanner.nextLine();

        facade.registerUser(new UserData(username, password, email));
        return login(new LoginData(username, password));
    }

    private String loginUI() throws DataAccessException {
        System.out.println(SET_TEXT_COLOR_WHITE + "Please give a username:");
        System.out.print("\n" + ">>> " + SET_TEXT_COLOR_GREEN);
        String username = scanner.nextLine();
        System.out.println(SET_TEXT_COLOR_WHITE + "Please give a password:");
        System.out.print("\n" + ">>> " + SET_TEXT_COLOR_GREEN);
        String password = scanner.nextLine();

        return login(new LoginData(username, password));
    }

    private String login(LoginData loginData) throws DataAccessException {
        UserResult user = facade.loginUser(loginData);

        this.authToken = user.authToken();
        this.username = user.username();
        this.state = State.SIGNEDIN;

        return "Logged in as: " + user.username();
    }

    private String logoutUI() throws DataAccessException {
        facade.logoutUser(this.authToken);
        return this.username + " has been logged out.";
    }

    private String createGameUI() throws DataAccessException {
        assertSignedIn();
        System.out.println(SET_TEXT_COLOR_WHITE + "Please give a game name:");
        System.out.print("\n" + ">>> " + SET_TEXT_COLOR_GREEN);
        String gameName = scanner.nextLine();

        int id = facade.createGame(this.authToken, new GameNameResponse(gameName));

        return "Game created with id " + id;
    }

    private String joinGameUI() throws DataAccessException {
        assertSignedIn();

        System.out.println(SET_TEXT_COLOR_WHITE + "Please give a game number:");
        System.out.print("\n" + ">>> " + SET_TEXT_COLOR_GREEN);
        int gameID;
        try {
            gameID = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println(SET_TEXT_COLOR_WHITE + "The game ID has to be a number.");
            joinGameUI();
            return null;
        }
        System.out.println(SET_TEXT_COLOR_WHITE + "Please give a team color (WHITE/BLACK):");
        System.out.print("\n" + ">>> " + SET_TEXT_COLOR_GREEN);
        String gameColor = scanner.nextLine();

        facade.joinGame(this.authToken, new JoinGameData(gameColor, gameID));

        Board.main(null);

        return "Joined game as player.";
    }

    private String joinObserverUI() throws DataAccessException {
        assertSignedIn();

        System.out.println(SET_TEXT_COLOR_WHITE + "Please give a game number:");
        System.out.print("\n" + ">>> " + SET_TEXT_COLOR_GREEN);
        int gameID;
        try {
            gameID = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println(SET_TEXT_COLOR_WHITE + "The game ID has to be a number.");
            joinObserverUI();
            return null;
        }

        facade.joinGame(this.authToken, new JoinGameData(null, gameID));

        Board.main(null);

        return "Joined game as observer.";
    }

    private String listGamesUI() throws DataAccessException {
        assertSignedIn();

        ListGameResult gameResult = facade.listGames(this.authToken);
        Collection<GameResult> games = gameResult.games();

        StringBuilder result = new StringBuilder();

        int i = 1;
        for (GameResult game : games) {
            result.append(String.format("%d. Name = %s, ID = %d, White Player = %s, Black Player = %s\n", i, game.gameName(), game.gameID(), game.whiteUsername(), game.blackUsername()));
            i++;
        }

        return result.toString();
    }

    private void assertSignedIn() throws DataAccessException {
        if (this.state == State.SIGNEDOUT) {
            throw new DataAccessException(SET_TEXT_COLOR_RED + "You must sign in");
        }
    }

    private void makeMoveUI(){
        System.out.println(SET_TEXT_COLOR_WHITE + "Please enter the start location: ");
        System.out.print("\n" + ">>> " + SET_TEXT_COLOR_GREEN);
        String start = scanner.nextLine();
        System.out.println(SET_TEXT_COLOR_WHITE + "Please enter the end location: ");
        System.out.print("\n" + ">>> " + SET_TEXT_COLOR_GREEN);
        String end = scanner.nextLine();

        ChessMove =


    }

    private void highlightGameUI(){

    }

    private void redrawGameUI(){

    }

    private void leaveGameUI(){

    }

    private void resignGameUI(){

    }
}
