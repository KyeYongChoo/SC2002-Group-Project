package Program;

import java.util.HashMap;
import java.util.Scanner;

/*
 * Example Text:
 * Name	NRIC	Age	Marital Status	Password
John	S1234567A	35	Single	password
Sarah	T7654321B	40	Married	password
Grace	S9876543C	37	Married	password
James	T2345678D	30	Married	password
Rachel	S3456789E	25	Single	password
 */
public class MainActivity {

    public static HashMap <String, User> userList = new HashMap<>();
    public static HashMap <String, Project> projectList = new HashMap<>();

    public static void main(String[] args) throws Exception{
        System.out.println("\nInitialization: ");
        System.out.println("Enter user data");
        System.out.println("\n1. Please access the Excel file");
        System.out.println("2. Press Ctrl+A then Ctrl+C");
        System.out.println("3. Navigate back to this java terminal");
        System.out.println("4. Press Ctrl+V");
        System.out.println("5a. (If on Windows) Press Ctrl+Z then Enter");
        System.out.println("5b. (If on Linux/MacOS) Press Ctrl+D");

        // Scanner sc = new Scanner(System.in).useDelimiter("\\A");
        // String allInput = sc.hasNext() ? sc.next() : "";

        // for (String line : allInput.split("\n")) {
        //     String[] fields = line.split("\t");
        //     if (fields[0].equals("Name") || fields[0].trim().isEmpty()){ 
        //         // If in the first row full of column names or empty, skip
        //         continue;
        //     }
        //     if (fields.length != 5) throw new Exception ("Please enter all 5 fields");
        //     userList.put(fields[1],new User(fields[1].trim(), fields[0].trim(), Integer.parseInt(fields[2].trim()), fields[3].trim(), fields[4].trim()));
        // }

        // clearConsole();

        // User client = LogIn();


        // uncomment out above code if need to test input 
        User client = new User("John", "S1234567A", 35, "Single", "password");

        Scanner sc = new Scanner(System.in);
        String userInput = null;
        int choice = 0;
        do { 
            System.out.println("\nWhat do you want to do: ");
            System.out.println("\n1. View list of projects open to your user group");
            System.out.println("2. Apply for a project");
            System.out.println("3. view project you have applied for, including application status");
            System.out.println("4. Request application withdrawal");
            System.out.println("5. Create, view, delete enquiries");
            System.out.println("6. Quit");
            try {
                choice = Integer.parseInt(sc.nextLine());
                clearConsole();
                if (choice < 1 || choice > 6){
                    throw new Exception("Choice not in range");
                }
            } catch (Exception e) {
                System.out.println("Please enter a valid integer between 1 and 6 inclusive");
                continue;
            }

            switch (choice){
                case (1): 
                    System.out.printf("%-20 %-20 %-15 %-15 %-30 %-10 %-15 %-30","Project Name","Neighbourhood","Number of Units","Selling Price","Application Opening Date","Manager","Officer Slot","Officer");
                    projectList.forEach((projectName,project)->project.printVisible(client));
                    
                    break;
                case (2):
                    break;
                case (3): 
                    break;
                case (4):
                    break;
                case (5): 
                    break;
            }
            
        } while (choice != 6);

        
        
    }
    
    // Open up a new page by clearing Console
    public static void clearConsole() {
        System.out.print("\033[H\033[2J\033[3J");
        System.out.flush();        
    }
    
    public static User LogIn(){
        Scanner sc = new Scanner(System.in);
        System.out.println("\nPlease Log In:");
        for (int attemptsLeft =  4; attemptsLeft >= 0; attemptsLeft--){
            String NRIC = User.inputNRIC();
            System.out.println("Please enter User Password: ");
            String password = sc.nextLine();
            User client = userList.get(NRIC);
            //if client does not exist or client's password incorrect
            if (client != null && client.verifyPassword(password)){
                return client;
            }
            System.out.println("Wrong Username or Password. Number of tries left: " + attemptsLeft);
        }
        clearConsole();
        System.out.println("Too many login attempts. \nExiting...");
        System.exit(0);
        return null;
    }
}
