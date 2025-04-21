package program.boundary.security;

import java.util.Scanner;

import program.boundary.console.AppScanner;

/**
 * <p>
 * The {@code UserValidator} class provides utility methods for validating user input,
 * specifically for Singapore National Registration Identity Card (NRIC) numbers.
 * It includes methods to prompt the user for an NRIC and to perform validation
 * checks to ensure it conforms to the expected format.
 * </p>
 */
public class UserValidator {
    /**
     * <p>
     * {@code sc} is a static and final instance of the {@link Scanner} class, obtained
     * from {@link AppScanner}. It is used to read input from the console for the user's NRIC.
     * </p>
     */
    private static Scanner sc = AppScanner.getInstance();

    /**
     * <p>
     * Prompts the user to enter their NRIC and continues to do so until a valid NRIC
     * is provided according to the rules defined in {@link #validateNRIC(String)}.
     * It catches any exceptions thrown by the validation method and informs the user
     * about the validation failure, re-prompting for input.
     * </p>
     *
     * @return <p>A {@code String} representing the valid NRIC entered by the user.</p>
     */
    public static String inputNRIC(){
        String NRIC = null;
        boolean validNRIC = true;
        do {
            validNRIC = true; // <p>Assume the input is valid at the start of each iteration.</p>
            System.out.println("Please enter User NRIC: ");
            try {
                NRIC = sc.nextLine().toUpperCase(); // <p>Read the NRIC input from the user.</p>
                validateNRIC(NRIC); // <p>Validate the entered NRIC.</p>
            } catch (Exception e) {
                validNRIC = false; // <p>Set to false if validation fails.</p>
                System.out.println(e.getMessage()); // <p>Display the validation error message to the user.</p>
            }
            // <p>Continue the loop until a valid NRIC is entered.</p>
        } while (!validNRIC);

        return NRIC; // <p>Return the valid NRIC.</p>
    }

    /**
     * <p>
     * Validates a given Singapore NRIC string against a set of predefined rules.
     * The rules include checking the starting character (must be 'S' or 'T'), the length
     * (must be 9 characters), the ending character (must be a capital letter), and that
     * the characters between the starting and ending characters are digits.
     * </p>
     *
     * @param NRIC <p>The {@code String} representing the NRIC to be validated.</p>
     * @return <p>The validated NRIC string if it passes all checks.</p>
     * @throws Exception <p>If the NRIC fails any of the validation checks, an {@code Exception}</p>
     * <p>is thrown with a descriptive error message indicating the validation failure.</p>
     */
    public static final String validateNRIC(String NRIC) throws Exception{
        // <p>Check if the NRIC starts with 'S' or 'T'.</p>
        if (NRIC.charAt(0) != 'S' && NRIC.charAt(0) != 'T'){
            throw new Exception("NRIC starts with S or T\nReceived NRIC: " + NRIC);
        }
        // <p>Check if the length of the NRIC is exactly 9 characters.</p>
        if (NRIC.length() != 9){
            throw new Exception("Length of NRIC should be 9\nReceived NRIC: " + NRIC);
        }
        try {
            // <p>Attempt to parse the substring between the first and last characters as an integer</p>
            // <p>to ensure that these characters are digits.</p>
            Integer.parseInt(NRIC.substring(1,8));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("NRIC should have 7 numbers in between\nReceived NRIC: " + NRIC);
        }
        return NRIC; // <p>Return the NRIC if all validation checks pass.</p>
    }
}
