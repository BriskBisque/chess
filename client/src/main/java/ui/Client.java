package ui;

import java.util.Scanner;

public class Client {

    private Service service;
    public void preLoginUI() {
        System.out.println("Enter a number: \n" +
                "1. Register \n" +
                "2. Login \n" +
                "3. Quit \n" +
                "4. Help");

        prelogInput();
    }

    private void prelogInput() {
        Scanner scanner = new Scanner(System.in);
        int userInput = scanner.nextInt();
        switch (userInput) {
            case 1 -> registerUI();
            case 2 -> loginUI();
            case 3 -> System.out.println("quitted");
            default -> preLogHelpUI();
        }
    }

    private void registerUI() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please give a username:");
        String username = scanner.nextLine();
        System.out.println("Please give a password:");
        String password = scanner.nextLine();
        System.out.println("Please give an email:");
        String email = scanner.nextLine();


    }

    private void loginUI() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please give a username:");
        String username = scanner.nextLine();
        System.out.println("Please give a password:");
        String password = scanner.nextLine();


    }

    private void preLogHelpUI(){
        System.out.println("Enter just the number of the option you want to pick. \n" +
                "1. Register will register you as a user. \n" +
                "2. Login will log you in. \n" +
                "3. Quits \n" +
                "Anything else, you will get a help menu.");
        prelogInput();
    }

    public void postLoginUI() {
        System.out.println("Enter a number: \n" +
                "1. Create Game \n" +
                "2. Join Game \n" +
                "3. Join as Observer \n" +
                "4. List Games \n" +
                "5. Logout \n" +
                "6. Help");

        postLogInput();
    }

    private void postLogInput(){
        Scanner scanner = new Scanner(System.in);
        int userInput = scanner.nextInt();
        switch (userInput) {
            case 1 -> registerUI();
            case 2 -> loginUI();
            case 3 -> System.out.println("quitted");
            default -> preLogHelpUI();
        }
    }

    private void createGameUI() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please give a game name:");
        String gameName = scanner.nextLine();


    }

    private void joinGameUI() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please give a game number:");
        int gameID = scanner.nextInt();
        System.out.println("Please give a team color (WHITE/BLACK):");
        String gameColor = scanner.nextLine();


    }

    private void joinObserverUI() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please give a game number:");
        int gameID = scanner.nextInt();
    }

    private void listGamesUI(){

    }
    private void postLogHelpUI() {
        System.out.println("Enter just the number of the option you want to pick. \n" +
                "1. Creates a new game with the name given. \n" +
                "2. Joins a game of the number given in the color specified. \n" +
                "3. Joins a game of the number given as a non-player. \n" +
                "4. Lists the games in the system, numbered by ID. \n" +
                "5. Logs the user out. \n" +
                "Anything else, you will get a help menu.");
        prelogInput();
    }
}
