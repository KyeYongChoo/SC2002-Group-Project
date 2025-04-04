package program;

import java.util.Scanner;

import program.security.Password;

public class User{
    private Scanner sc = new Scanner(System.in);
    private boolean isLoggedIn = false; 
    private String name;
    private String userId;
    private Password password = new Password(); 
    private int age;
    private MARITAL_STATUS maritalStatus;
    private HousingReqList reqList = new HousingReqList();
    private EnquiryList enquiryList = new EnquiryList();

    public EnquiryList getEnquiryList(){
        return enquiryList;
    }

    public boolean see3Rooms(){
        return age >= 21 && maritalStatus == User.MARITAL_STATUS.Married;
    }

    public boolean see2Rooms(){
        return age >= 35 && maritalStatus == User.MARITAL_STATUS.Single;
    }

    public static enum MARITAL_STATUS {
        Married,
        Single
    }

    public boolean hasActiveApplication(){
        return HousingReqList.activeReq(this) != null;
    }

    public User(String NRIC, String name, int age, String maritalStatus) throws Exception{
        this.userId = validateNRIC(NRIC);
        this.name = name;
        this.age = validateAge(age);
        this.maritalStatus = validateMaritalStatus(maritalStatus);
        
    }

    public User(String NRIC, String name, int age, String maritalStatus, String password) throws Exception{
        this(NRIC, name, age, maritalStatus);
        this.password = new Password(password);
        
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Password getPassword(){
        return password;
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


    // In case I'm forgetful and forgot which class to call printPastReq on
    public void printPastReq(){
        HousingReqList.printPast(this);
    }

    public static void printPastReq(User client){
        HousingReqList.printPast(client);
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

    public void setReqList(HousingReqList reqList){
        this.reqList = reqList;
    }
    public HousingReqList getReqList(){
        return reqList;
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