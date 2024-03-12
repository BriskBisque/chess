package ui;

import java.util.Scanner;

public class Client {

    private
    public void preloginUI() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a number: \n" +
                "1. Register \n" +
                "2. Login \n" +
                "3. Help \n" +
                "4. Quit");

        int userInput = scanner.nextInt();
        if (userInput == 1){
            registerUI();
        } else if (userInput == 2){
            loginUI();
        } else if (userInput == 3){

        } else {

        }
    }

    public void registerUI() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please give a username:");
        String username = scanner.nextLine();
        System.out.println("Please give a password:");
        String password = scanner.nextLine();
        System.out.println("Please give an email:");
        String email = scanner.nextLine();


    }

    public void loginUI() {

    }
}
