package Program;

import java.util.Scanner;

public class User{
    private Scanner sc = new Scanner(System.in);
    private boolean isLoggedIn = false; 

    private boolean flatVisibility = true;
    private String name;
    private String userId;
    private String password = "password";
    private int age;
    private MARITAL_STATUS marital_status;

    public boolean getVisibility(){
        return flatVisibility;
    }
    public void toggleVisibility(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Do you want to view flats you have not applied for: (Y/N)");
        System.out.println("Current: " + (flatVisibility?"Y":"N"));
        String userInput = null;
        do { 
            userInput = sc.nextLine().toUpperCase();
            if ("Y".equals(userInput) || "N".equals(userInput)){
                break;
            }
            System.out.println("Please enter Y or N only");
        } while (true);
        flatVisibility = "Y".equals(userInput);
    }

    public static enum MARITAL_STATUS {
        Married,
        Single
    }

    // we want the constructor to trigger for the first time to generate everything for initialisation, then later on never call the constructor ever again 

    public User(String NRIC, String name, int age, String marital_status, String password) throws Exception{
        this.userId = validateNRIC(NRIC);
        this.name = name;
        this.age = validateAge(age);
        this.marital_status = validateMaritalStatus(marital_status);
        this.password = password; 
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPassword(String newPassword){
        password = newPassword;
    }

    public boolean verifyPassword(String password){
        if (password.equals(this.password)) return true;
        else return false;
    }


    public static String inputNRIC(){
        Scanner sc = new Scanner (System.in);
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

    public final int validateAge(int age) throws Exception{
        if (age < 0 || age > 150) {
            throw new Exception("Age should be between 0 and 150 inclusive");
        }
        return age;
    }

    public final MARITAL_STATUS validateMaritalStatus(String marital_status) throws Exception{
        switch (marital_status.toUpperCase().trim()){
            case "MARRIED": 
                return MARITAL_STATUS.Married;
            case "SINGLE": 
                return MARITAL_STATUS.Single;
            default:
                throw new Exception("Marital Status for NRIC No." + this.userId + "should be either Married or Single\nReceived marital status: " + marital_status);
        }
    }
    
}