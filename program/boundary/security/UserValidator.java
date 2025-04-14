package program.boundary.security;

import java.util.Scanner;

import program.boundary.console.AppScanner;

public class UserValidator {
    private static Scanner sc = AppScanner.getInstance();
    public static String inputNRIC(){
        String NRIC = null;
        boolean validNRIC = true;
        do { 
            validNRIC = true;
            System.out.println("Please enter User NRIC: ");
            try {
                NRIC = sc.nextLine();
                validateNRIC(NRIC);
            } catch (Exception e) {
                validNRIC = false; 
                System.out.println(e.getMessage());
            }
        } while (!validNRIC);

        return NRIC;
    }

    public static final String validateNRIC(String NRIC) throws Exception{
        if (NRIC.charAt(0) != 'S' && NRIC.charAt(0) != 'T'){
            throw new Exception("NRIC starts with S or T\nReceived NRIC: " + NRIC);
        }
        if (NRIC.length() != 9){
            throw new Exception("Length of NRIC should be 7\nReceived NRIC: " + NRIC);
        }
        if (NRIC.charAt(8) >= 'A' && NRIC.charAt(8) >= 'Z'){
            throw new Exception("NRIC should end in a capital letter\nReceived NRIC: " + NRIC);
        }
        try {
            // use parseInt not for the returned int but to check if its an int
            Integer.parseInt(NRIC.substring(1,8));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("NRIC should have 7 numbers in between\nReceived NRIC: " + NRIC);
        }
        return NRIC;
    }
}
