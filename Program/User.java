package Program;

import Program.User.MARITAL_STATUS;
import Program.User.VISIBILITY;
import java.util.Scanner;

public class User{
    private Scanner sc = new Scanner(System.in);
    private boolean isLoggedIn = false; 

    private VISIBILITY flatVisibility;
    private String name;
    private String userId;
    private String password = "password";
    private int age;
    private MARITAL_STATUS maritalStatus;

    public static enum VISIBILITY{
        Room2,
        RoomAll,
        RoomNone
    }

    // DEBUG means should not be in final product
    public void DEBUG_SetVisibility(VISIBILITY visibility){
        flatVisibility = visibility;
        MainActivity.updateTableRef(flatVisibility);
    }

    public VISIBILITY getVisibility(){
        return flatVisibility;
    }

    public void toggleVisibility(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Do you want to view flats you have not applied for: (Y/N)");
        System.out.println("Current: " + (flatVisibility == VISIBILITY.RoomNone ? "N":"Y"));
        String userInput = null;
        do { 
            userInput = sc.nextLine().toUpperCase();
            if ("Y".equals(userInput) || "N".equals(userInput)){
                break;
            }
            System.out.println("Please enter Y or N only");
        } while (true);
        if ("N".equals(userInput)){
            flatVisibility = VISIBILITY.RoomNone;
            MainActivity.updateTableRef(flatVisibility);
        }
        else if (age >= 35 && maritalStatus == MARITAL_STATUS.Single){
            flatVisibility = VISIBILITY.Room2;
            MainActivity.updateTableRef(flatVisibility);
        }
        else if (age >= 21 && maritalStatus == MARITAL_STATUS.Married){
            flatVisibility= VISIBILITY.RoomAll;
            MainActivity.updateTableRef(flatVisibility);
        }
        else {
            flatVisibility = VISIBILITY.RoomNone;
            MainActivity.updateTableRef(flatVisibility);
        }
    }

    public static enum MARITAL_STATUS {
        Married,
        Single
    }

    public User(String NRIC, String name, int age, String maritalStatus, String password) throws Exception{
        this.userId = validateNRIC(NRIC);
        this.name = name;
        this.age = validateAge(age);
        this.maritalStatus = validateMaritalStatus(maritalStatus);
        this.password = password; 
        if (this.age >= 35 && this.maritalStatus == MARITAL_STATUS.Single){
            flatVisibility = VISIBILITY.Room2;
        }
        else if (this.age >= 21 && this.maritalStatus == MARITAL_STATUS.Married){
            flatVisibility= VISIBILITY.RoomAll;
        }
        else {
            flatVisibility = VISIBILITY.RoomNone;
        }
        
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

    public MARITAL_STATUS getMaritalStatus(){
        return maritalStatus;
    }

    public void setMaritalStatus(MARITAL_STATUS maritalStatus){
        this.maritalStatus = maritalStatus;
    }
    
    public void setMaritalStatus(String maritalStatus) throws Exception{
        this.maritalStatus = validateMaritalStatus(maritalStatus);
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

    public int getAge(){
        return age;
    }

    public final int validateAge(int age) throws Exception{
        if (age < 0 || age > 150) {
            throw new Exception("Age should be between 0 and 150 inclusive");
        }
        return age;
    }

    public String getUserId(){
        return userId;
    }

    @Override
    public String toString(){
        return name;
    }

    public final MARITAL_STATUS validateMaritalStatus(String maritalStatus) throws Exception{
        switch (maritalStatus.toUpperCase().trim()){
            case "MARRIED": 
                return MARITAL_STATUS.Married;
            case "SINGLE": 
                return MARITAL_STATUS.Single;
            default:
                throw new Exception("Marital Status for NRIC No." + this.userId + "should be either Married or Single\nReceived marital status: " + maritalStatus);
        }
    }
    
}