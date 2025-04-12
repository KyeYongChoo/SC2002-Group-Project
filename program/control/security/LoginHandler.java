package program.control.security;

import java.util.Scanner;

import program.boundary.AppScanner;
import program.control.UserFetcher;
import program.entity.users.User;

public class LoginHandler {
    private static Scanner sc = AppScanner.getInstance();

    public static User loginUser(){
        System.out.println("\nPlease Log In:");
        for (int attemptsLeft =  4; attemptsLeft >= 0; attemptsLeft--){
            String NRIC = User.inputNRIC();
            System.out.println("Please enter User Password: ");
            String userInput = sc.nextLine();
            
            User user = UserFetcher.fetch(NRIC, userInput);

            if (user != null){
                return user;
            }

            System.out.println("Wrong Username or Password. Number of tries left " + attemptsLeft);
        }

        System.out.println("Too many login attempts. \nExiting...");
        System.out.println("Too many login attempts. \nExiting...");
        System.exit(0);
        return null;
    }
}
