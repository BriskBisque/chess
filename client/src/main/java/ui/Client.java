package ui;

import dataAccess.DataAccessException;
import model.*;
import model.Results.GameResult;
import model.Results.ListGameResult;
import model.Results.UserResult;
import server.GameNameResponse;
import server.Server;

import java.util.Collection;
import java.util.Scanner;

public class Client {

    private final ServerFacade facade;
    private final Scanner scanner;
    private String userAuthToken;

    public Client(){
        scanner = new Scanner(System.in);
        var url = "http://localhost:" + 8080;
        facade = new ServerFacade(url);
    }

    public void preLoginUI() throws DataAccessException {
        userAuthToken = null;

        System.out.println("""
                Enter a number:\s
                1. Register\s
                2. Login\s
                3. Quit\s
                4. Help""");

        prelogInput();
    }

    private void prelogInput() throws DataAccessException {
        int userInput;
        try {
            userInput = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Please enter a number.");
            preLoginUI();
            return;
        }
        switch (userInput) {
            case 1 -> registerUI();
            case 2 -> loginUI();
            case 3 -> quit();
            default -> preLogHelpUI();
        }
    }

    private void quit() {
        System.out.println("quitted");
        System.exit(0);
    }

    private void registerUI() throws DataAccessException {
        System.out.println("Please give a username:");
        String username = scanner.nextLine();
        System.out.println("Please give a password:");
        String password = scanner.nextLine();
        System.out.println("Please give an email:");
        String email = scanner.nextLine();

        try {
            facade.registerUser(new UserData(username, password, email));
            UserResult user = facade.loginUser(new LoginData(username, password));
            System.out.println("Logged in as: " + user.username());
            userAuthToken = user.authToken();
        } catch (Exception e){
            System.out.println("There was an issue with the user information given.");
            preLoginUI();
            return;
        }

        postLoginUI();
    }

    private void loginUI() throws DataAccessException {
        System.out.println("Please give a username:");
        String username = scanner.nextLine();
        System.out.println("Please give a password:");
        String password = scanner.nextLine();

        try {
            UserResult user = facade.loginUser(new LoginData(username, password));
            System.out.println("Logged in as: " + user.username());
            userAuthToken = user.authToken();
        } catch (Exception E){
            System.out.println("There was an issue with the user information given.");
            preLoginUI();
            return;
        }

        postLoginUI();
    }

    private void preLogHelpUI() throws DataAccessException {
        System.out.println("""
                Enter just the number of the option you want to pick.\s
                1. Register will register you as a user.\s
                2. Login will log you in.\s
                3. Quits\s
                Anything else, you will get a help menu.""");
        prelogInput();
    }

    public void postLoginUI() throws DataAccessException {
        System.out.println("""
                Enter a number:\s
                1. Create Game\s
                2. Join Game\s
                3. Join as Observer\s
                4. List Games\s
                5. Logout\s
                6. Help""");

        postLogInput();
    }

    private void postLogInput() throws DataAccessException {
        int userInput;
        try {
            userInput = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Please enter a number.");
            postLoginUI();
            return;
        }
        switch (userInput) {
            case 1 -> createGameUI();
            case 2 -> joinGameUI();
            case 3 -> joinObserverUI();
            case 4 -> listGamesUI();
            case 5 -> logoutUI();
            default -> postLogHelpUI();
        }
    }

    private void logoutUI() throws DataAccessException {
        try {
            facade.logoutUser(userAuthToken);
        } catch (Exception e){
            System.out.println("An error has occured. Please contact support for assistance.");
            return;
        }
        preLoginUI();
    }

    private void createGameUI() throws DataAccessException {
        assertSignedIn();
        System.out.println("Please give a game name:");
        String gameName = scanner.nextLine();

        try {
            int id = facade.createGame(userAuthToken, new GameNameResponse(gameName));
            System.out.println("Game created with id " + id);
        } catch (Exception e){
            System.out.println("An error has occured. Please contact support for assistance.");
        }

        postLoginUI();
    }

    private void joinGameUI() throws DataAccessException {
        assertSignedIn();
        System.out.println("Please give a game number:");
        int gameID;
        try {
            gameID = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("The game ID has to be a number.");
            joinGameUI();
            return;
        }
        System.out.println("Please give a team color (WHITE/BLACK):");
        String gameColor = scanner.nextLine();

        try {
            facade.joinGame(userAuthToken, new JoinGameData(gameColor, gameID));
        } catch (Exception e){
            System.out.println("An error has occured. Please contact support for assistance.");
        }

        Board.main(null);

        postLoginUI();
    }

    private void joinObserverUI() throws DataAccessException {
        assertSignedIn();
        System.out.println("Please give a game number:");
        int gameID;
        try {
            gameID = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("The game ID has to be a number.");
            joinObserverUI();
            return;
        }

        try {
            facade.joinGame(userAuthToken, new JoinGameData(null, gameID));
        } catch (Exception e){
            System.out.println("An error has occured. Please contact support for assistance.");
        }

        Board.main(null);

        postLoginUI();
    }

    private void listGamesUI() throws DataAccessException {
        assertSignedIn();
        try {
            ListGameResult gameResult = facade.listGames(userAuthToken);
            Collection<GameResult> games = gameResult.games();

            int i = 1;
            for (GameResult game : games) {
                System.out.printf("%d. Name = %s, ID = %d, White Player = %s, Black Player = %s\n", i, game.gameName(), game.gameID(), game.whiteUsername(), game.blackUsername());
                i++;
            }
        } catch (Exception e){
            System.out.println("An error has occured. Please contact support for assistance.");
        }

        postLoginUI();
    }

    private void postLogHelpUI() throws DataAccessException {
        System.out.println("""
                Enter just the number of the option you want to pick.\s
                1. Creates a new game with the name given.\s
                2. Joins a game of the number given in the color specified.\s
                3. Joins a game of the number given as a non-player.\s
                4. Lists the games in the system, numbered by ID.\s
                5. Logs the user out.\s
                Anything else, you will get a help menu.""");
        postLogInput();
    }

    private void assertSignedIn() throws DataAccessException {
        if (userAuthToken == null) {
            throw new DataAccessException("You must sign in");
        }
    }

    private void gameplayUI() {
        System.out.println("""
                Enter just the number of the option you want\s
                1. Make Move\s
                2. Highlight Legal Moves\s
                3. Redraw Chess Board\s
                4. Leave\s
                5. Resign\s
                6. Help
                """);
        gameplayInput();
    }

    private void gameplayInput(){
        int userInput;
        try {
            userInput = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Please enter a number.");
            gameplayUI();
            return;
        }
        switch (userInput) {
            case 1 -> makeMoveUI();
            case 2 -> highlightGameUI();
            case 3 -> redrawGameUI();
            case 4 -> leaveGameUI();
            case 5 -> resignGameUI();
            default -> gameplayHelp();
        }
    }

    private void makeMoveUI(){

    }

    private void highlightGameUI(){

    }

    private void redrawGameUI(){

    }

    private void leaveGameUI(){

    }

    private void resignGameUI(){

    }

    private void gameplayHelp(){
        System.out.println("""
                Enter just the number of the option you want to pick.\s
                1. Enter a start location and an end location to make a move.\s
                2. Enter a location to see what moves the piece can make.\s
                3. Redraws the board.\s
                4. Leaves the game without resigning.\s
                5. Forfeits the game.\s
                Anything else, you will get a help menu.""");
        gameplayInput();
    }
}
