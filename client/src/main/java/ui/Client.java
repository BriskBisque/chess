package ui;

import dataAccess.DataAccessException;
import model.*;
import model.Results.GameResult;
import model.Results.ListGameResult;
import model.Results.UserResult;
import server.GameNameResponse;
import server.Server;
import server.ServerFacade;

import java.util.Collection;
import java.util.Scanner;

public class Client {

    private final ServerFacade facade;
    private final Scanner scanner;
    private String userAuthToken;

    public Client(){
        scanner = new Scanner(System.in);

        Server server = new Server();
        server.run(0);
        var url = "http://localhost:" + server.port();
        facade = new ServerFacade(url);
    }

    public void preLoginUI() {
        userAuthToken = null;

        System.out.println("""
                Enter a number:\s
                1. Register\s
                2. Login\s
                3. Quit\s
                4. Help""");

        prelogInput();
    }

    private void prelogInput() {
        int userInput = Integer.parseInt(scanner.nextLine());
        switch (userInput) {
            case 1 -> registerUI();
            case 2 -> loginUI();
            case 3 -> quit();
            default -> preLogHelpUI();
        }
    }

    private void quit() {
        System.out.println("quitted");
        try {
            facade.deleteAll();
        } catch (Exception e){
            System.out.println("An error has occured. Please contact support for assistance.");
            return;
        }
        System.exit(0);
    }

    private void registerUI() {
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

    private void loginUI() {
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

    private void preLogHelpUI() {
        System.out.println("""
                Enter just the number of the option you want to pick.\s
                1. Register will register you as a user.\s
                2. Login will log you in.\s
                3. Quits\s
                Anything else, you will get a help menu.""");
        prelogInput();
    }

    public void postLoginUI() {
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

    private void postLogInput() {
        int userInput = Integer.parseInt(scanner.nextLine());
        switch (userInput) {
            case 1 -> createGameUI();
            case 2 -> joinGameUI();
            case 3 -> joinObserverUI();
            case 4 -> listGamesUI();
            case 5 -> logoutUI();
            default -> postLogHelpUI();
        }
    }

    private void logoutUI() {
        try {
            facade.logoutUser(userAuthToken);
        } catch (Exception e){
            System.out.println("An error has occured. Please contact support for assistance.");
            return;
        }
        preLoginUI();
    }

    private void createGameUI() {
        System.out.println("Please give a game name:");
        String gameName = scanner.nextLine();

        try {
            int id = facade.createGame(userAuthToken, new GameNameResponse(gameName));
            System.out.println("Game created with id" + id);
        } catch (Exception e){
            System.out.println("An error has occured. Please contact support for assistance.");
        }

        postLoginUI();
    }

    private void joinGameUI() {
        System.out.println("Please give a game number:");
        int gameID = Integer.parseInt(scanner.nextLine());
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

    private void joinObserverUI() {
        System.out.println("Please give a game number:");
        int gameID = scanner.nextInt();

        try {
            facade.joinGame(userAuthToken, new JoinGameData(null, gameID));
        } catch (Exception e){
            System.out.println("An error has occured. Please contact support for assistance.");
        }

        Board.main(null);

        postLoginUI();
    }

    private void listGamesUI() {
        try {
            ListGameResult gameResult = facade.listGames(userAuthToken);
            Collection<GameResult> games = gameResult.games();

            int i = 1;
            for (GameResult game : games) {
                System.out.printf("%d. Name = %s, ID = %d White Player = %s, Black Player = %s", i, game.gameName(), game.gameID(), game.whiteUsername(), game.blackUsername());
                i++;
            }
        } catch (Exception e){
            System.out.println("An error has occured. Please contact support for assistance.");
        }

        postLoginUI();
    }

    private void postLogHelpUI() {
        System.out.println("""
                Enter just the number of the option you want to pick.\s
                1. Creates a new game with the name given.\s
                2. Joins a game of the number given in the color specified.\s
                3. Joins a game of the number given as a non-player.\s
                4. Lists the games in the system, numbered by ID.\s
                5. Logs the user out.\s
                Anything else, you will get a help menu.""");
        prelogInput();
    }
}
