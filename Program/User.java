package Program;

import java.util.Scanner;

public class User{
    private Scanner sc = new Scanner(System.in);
    private boolean isLoggedIn = false; 

    private String userId;
    private String password = "password";
    private int Age;
    private MARITAL_STATUS marital_status;
    private enum MARITAL_STATUS {
        Married,
        Single,
        Separated,
    }

    public User(){
        userId = inputNRIC();
        System.out.println("Account created. Default password: \"password\"");
    }

    public void LogIn(){
        System.out.println("\nPlease Log In:");
        for (int i=0; i<5; i++){
            String NRIC = inputNRIC();
            System.out.println("Please enter User Password: ");
            String Password = sc.nextLine();
        }
    }

    public void setPassword(String newPassword){
        password = newPassword;
    }


    public String inputNRIC(){
        String NRIC = null;
        boolean validNRIC = true;
        do { 
            validNRIC = true;
            System.out.println("Please enter User NRIC: ");
            try {
                NRIC = sc.nextLine();
            } catch (Exception e) {
                validNRIC = false; 
                System.out.println(e.getMessage());
            }
        } while (!validNRIC);

        return NRIC;
    }

    public String validateNRIC(String NRIC) throws Exception{
        if (NRIC.charAt(0) != 'S' && NRIC.charAt(0) != 'T'){
            throw new Exception("NRIC starts with S or T");
        }
        if (NRIC.length() != 9){
            throw new Exception("Length of NRIC should be 7");
        }
        if (NRIC.charAt(-1) >= 'A' && NRIC.charAt(-1) >= 'Z'){
            throw new Exception("NRIC should end in a capital letter");
        }
        try {
            // use parseInt not for the returned int but to check if its an int
            Integer.parseInt(NRIC.substring(1,8));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("NRIC should have 7 numbers in between");
        }
        return NRIC;
    }
}