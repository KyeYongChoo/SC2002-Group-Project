package program.boundary.security;

import java.util.Scanner;

import program.boundary.console.AppScanner;
import program.control.security.UserFetcher;
import program.entity.users.User;

/**
 * <p>
 * The {@code LoginHandler} class manages the user login authentication process. It prompts the user to enter their NRIC and password,
 * validates the credentials against the system's user database, and returns the authenticated {@link User} object if the login is successful.
 * </p>
 * <p>
 * If the authentication fails, the class provides feedback on the incorrect credentials and limits the number of login attempts.
 * If the maximum number of attempts is exceeded, the application is terminated.
 * </p>
 */
public class LoginHandler {

    /**
     * <p>
     * A static and final instance of the {@link Scanner} class, used for reading user input from the console.
     * This instance is provided by {@link AppScanner} to ensure consistent input handling.
     * </p>
     */
    private static Scanner sc = AppScanner.getInstance();

    /**
     * <p>
     * Initiates the login process by prompting the user for their NRIC and password.
     * It allows a limited number of login attempts. If authentication succeeds, it returns the corresponding
     * {@link User} object. If the maximum number of failed attempts is reached, the application is exited.
     * </p>
     *
     * @return the authenticated {@link User} object if login is successful, or {@code null} if the maximum number of login attempts is exceeded,
     *         at which point the application will terminate.
     */
    public static User loginUser(){
        System.out.println("\nPlease Log In:");

        // <p>Loop through a set number of login attempts.</p>
        for (int attemptsLeft =  4; attemptsLeft >= 0; attemptsLeft--){

            // <p>Prompt the user to enter their NRIC and validate its format.</p>
            String NRIC = UserValidator.inputNRIC();
            System.out.println("Please enter User Password: ");

            // <p>Capture the user's password input.</p>
            String userInput = sc.nextLine();

            // <p>Attempt to retrieve the {@link User} object corresponding to the provided NRIC and password.</p>
            User user = UserFetcher.fetch(NRIC, userInput);

            // <p>If a matching user is found, authentication is successful, and the {@link User} object is returned.</p>
            if (user != null){
                return user;
            }

            // <p>If login fails, inform the user of incorrect credentials and display remaining attempts.</p>
            System.out.println("Wrong Username or Password. Number of tries left " + attemptsLeft);
        }

        // <p>If the maximum number of attempts is exceeded, notify the user and exit the application.</p>
        System.out.println("Too many login attempts. \nExiting...");
        System.exit(0);  // <p>Exit the application after failed login attempts.</p>
        return null;  // <p>Technically unreachable due to the System.exit(0), but included for completeness.</p>
    }
}
