package Program;

import Program.User.VISIBILITY;
import java.util.ArrayList;
import java.util.List;
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

    // theres gonna be a whole lot of polymorphism up next folks for buckle up 
    public static UserList userList = new UserList();
    public static List <Project> projectList = new ArrayList<>();
    public static UserList ManagerList = new UserList();
    public static UserList OfficerList = new UserList();
    
    public static void main(String[] args) throws Exception{

        // initialise();
        // User client = LogIn();
        // client.toggleVisibility();
        User client = quickInitialise();
        projectList.forEach(project->project.printVisible(client));


        // uncomment out above code if need to test input 
        // User client = new User("John", "S1234567A", 35, "Single", "password");

        // Scanner sc = new Scanner(System.in);
        
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        do { 
            System.out.println("\nWhat do you want to do: ");
            System.out.println("\n1. View list of projects open to your user group");
            System.out.println("2. Apply for a project");
            System.out.println("3. View project you have applied for, including application status");
            System.out.println("4. Request application withdrawal");
            System.out.println("5. Create, view, delete enquiries");
            System.out.println("6. Toggle visibility of flats you have not applied for");
            System.out.println("7. Quit");

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
                    // if client toggled visibility off
                    if (client.getVisibility() == VISIBILITY.RoomNone){
                        Project.printApplied(client);
                        // Print applied flats only
                        break;
                    }
                    System.out.printf(formatTableRef,(Object[])tableHeaders);
                    projectList.forEach(project->project.printVisible(client));
                    break;
                case (2):
                    break;
                case (3): 
                    break;
                case (4):
                    break;
                case (5): 
                    break;
                case (6): 
                    client.toggleVisibility();
                    break;
            }
            System.out.println("Press enter to continue");
            sc.nextLine();
        } while (choice != 7);

        
        sc.close();
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
    

    /**
     * Format string for table alignment.
     * Columns:
     * 1. Project Name (20 chars)
     * 2. Neighbourhood (20 chars)
     * 3. Number of 2 Room Units (15 chars)
     * 4. Selling Price of 2 Room Units (15 chars)
     * 5. (If married client) Number of 3 Room Units (15 chars)
     * 6. Selling Price of 3 Room Units (15 chars)
     * 7. Application Opening Date (30 chars)
     * 8. Manager (10 chars)
     * 9. Officer Slot (15 chars)
     * 10. Officer (30 chars)
     */
    public static String formatTableRef;
    /**
     * List of Strings for table headers. Relies on formatTableRef
     */
    public static String[] tableHeaders = null;
    public static void updateTableRef(VISIBILITY flat_visibility){
        if (flat_visibility == VISIBILITY.Room2){
            formatTableRef = "%-20s %-20s %-15s %-15s %-30s %-10s %-15s %-30s\n";
            tableHeaders = new String[]{"Project Name","Neighbourhood","No. 2-Room","Price","Application Opening Date","Manager","Officer Slot","Officer"};
        }
        if (flat_visibility == VISIBILITY.RoomAll){
            formatTableRef = "%-20s %-20s %-15s %-15s %-15s %-15s %-30s %-10s %-15s %-30s\n";
            tableHeaders = new String[]{"Project Name","Neighbourhood","No. 2 Room","Price","No. 3 Room","Price","Application Opening Date","Manager","Officer Slot","Officer"};
        }
        if (flat_visibility == VISIBILITY.RoomNone){
            formatTableRef = "";
            tableHeaders = null;
        }
    }

    public static void initialise() throws Exception{
        System.out.println("\nInitialization: ");
        System.out.println("Enter user data");
        System.out.println("\n1. Please access the Excel file");
        System.out.println("2. Press Ctrl+A then Ctrl+C");
        System.out.println("3. Navigate back to this java terminal");
        System.out.println("4. Press Ctrl+V");
        System.out.println("5a. (If on Windows) Press Ctrl+Z then Enter");
        System.out.println("5b. (If on Linux/MacOS) Press Ctrl+D");

        Scanner sc = new Scanner(System.in).useDelimiter("\\A");
        String allInput = sc.hasNext() ? sc.next() : "";

        for (String line : allInput.split("\n")) {
            String[] fields = line.split("\t");
            if (fields[0].equals("Name") || fields[0].trim().isEmpty()){ 
                // If in the first row full of column names or empty, skip
                continue;
            }
            if (fields.length != 5) throw new Exception ("Please enter all 5 fields");
            userList.add(new User(fields[1].trim(), fields[0].trim(), Integer.parseInt(fields[2].trim()), fields[3].trim(), fields[4].trim()));
        }

        clearConsole();

        System.out.println("Enter Officer data");
        System.out.println("\n1. Please access the Excel file");
        System.out.println("2. Press Ctrl+A then Ctrl+C");
        System.out.println("3. Navigate back to this java terminal");
        System.out.println("4. Press Ctrl+V");
        System.out.println("5a. (If on Windows) Press Ctrl+Z then Enter");
        System.out.println("5b. (If on Linux/MacOS) Press Ctrl+D");

        sc = new Scanner(System.in).useDelimiter("\\A");
        allInput = sc.hasNext() ? sc.next() : "";

        for (String line : allInput.split("\n")) {
            String[] fields = line.split("\t");
            if (fields[0].equals("Name") || fields[0].trim().isEmpty()){ 
                // If in the first row full of column names or empty, skip
                continue;
            }
            if (fields.length != 5) throw new Exception ("Please enter all 5 fields");
            OfficerList.add(new Officer(fields[1].trim(), fields[0].trim(), Integer.parseInt(fields[2].trim()), fields[3].trim(), fields[4].trim()));
        }

        clearConsole();

        System.out.println("Enter Manager data");
        System.out.println("\n1. Please access the Excel file");
        System.out.println("2. Press Ctrl+A then Ctrl+C");
        System.out.println("3. Navigate back to this java terminal");
        System.out.println("4. Press Ctrl+V");
        System.out.println("5a. (If on Windows) Press Ctrl+Z then Enter");
        System.out.println("5b. (If on Linux/MacOS) Press Ctrl+D");

        sc = new Scanner(System.in).useDelimiter("\\A");
        allInput = sc.hasNext() ? sc.next() : "";

        for (String line : allInput.split("\n")) {
            String[] fields = line.split("\t");
            if (fields[0].equals("Name") || fields[0].trim().isEmpty()){ 
                // If in the first row full of column names or empty, skip
                continue;
            }
            if (fields.length != 5) throw new Exception ("Please enter all 5 fields");
            ManagerList.add(new Manager(fields[1].trim(), fields[0].trim(), Integer.parseInt(fields[2].trim()), fields[3].trim(), fields[4].trim()));
        }

        clearConsole();
        
        System.out.println("Enter project data");
        System.out.println("\n1. Please access the Excel file");
        System.out.println("2. Press Ctrl+A then Ctrl+C");
        System.out.println("3. Navigate back to this java terminal");
        System.out.println("4. Press Ctrl+V");
        System.out.println("5a. (If on Windows) Press Ctrl+Z then Enter");
        System.out.println("5b. (If on Linux/MacOS) Press Ctrl+D");

        sc = new Scanner(System.in).useDelimiter("\\A");
        allInput = sc.hasNext() ? sc.next() : "";

        for (String line : allInput.split("\n")) {
            String[] fields = line.split("\t");
            if (fields[0].equals("Project Name") || fields[0].trim().isEmpty()){ 
                // If in the first row full of column names or empty, skip
                continue;
            }
            if (fields.length != 13) throw new Exception ("Please enter all 5 fields");
            for (int i = 0; i < fields.length; i++) {
                fields[i] = fields[i].trim();
            }

            if ("".equals(fields[2]) && "2-Room".equals(fields[5])) projectList.add(new Project(fields[0],fields[1],fields[6],fields[7],fields[4],fields[5],fields[8],fields[9],fields[10],fields[11],fields[12]));
            if ("2-Room".equals(fields[2]) && "".equals(fields[5])) projectList.add(new Project(fields[0],fields[1],fields[3],fields[4],fields[6],fields[7],fields[8],fields[9],fields[10],fields[11],fields[12]));
            if ("".equals(fields[2]) && "3-Room".equals(fields[5])) projectList.add(new Project(fields[0],fields[1],fields[3],fields[4],fields[6],fields[7],fields[8],fields[9],fields[10],fields[11],fields[12]));
            if ("3-Room".equals(fields[2]) && "".equals(fields[5])) projectList.add(new Project(fields[0],fields[1],fields[6],fields[7],fields[4],fields[5],fields[8],fields[9],fields[10],fields[11],fields[12]));
            if ("2-Room".equals(fields[2]) && "3-Room".equals(fields[5])) projectList.add(new Project(fields[0],fields[1],fields[3],fields[4],fields[6],fields[7],fields[8],fields[9],fields[10],fields[11],fields[12]));
            if ("3-Room".equals(fields[2]) && "2-Room".equals(fields[5])) projectList.add(new Project(fields[0],fields[1],fields[6],fields[7],fields[4],fields[5],fields[8],fields[9],fields[10],fields[11],fields[12]));
            if (fields[2].equals(fields[5])) throw new Exception("Overlapping Room types");
            
        }
        clearConsole();
        }

    public static User quickInitialise() throws Exception{
        // Applicants
        userList.add(new User("S1234567A","John",35,"Single","password"));
        userList.add(new User("T7654321B","Sarah",40,"Married","password"));
        userList.add(new User("S9876543C","Grace",37,"Married","password"));
        userList.add(new User("T2345678D","James",30,"Married","password"));
        userList.add(new User("S3456789E","Rachel",25,"Single","password"));

        // Officers
        OfficerList.add(new Officer("T2109876H","Daniel",36,"Single","password"));
        OfficerList.add(new Officer("S6543210I","Emily",28,"Single","password"));
        OfficerList.add(new Officer("T1234567J","David",29,"Married","password"));

        // Managers
        ManagerList.add(new Manager("T8765432F","Michael",36,"Single","password"));
        ManagerList.add(new Manager("S5678901G","Jessica",26,"Married","password"));

        // Projects
        projectList.add(new Project("Acacia Breeze","Yishun","2","350000","3","450000","15/2/2025","20/3/2025","Jessica","3","Daniel,Emily"));

        //Login
        User client = userList.get(0);
        client.DEBUG_SetVisibility(VISIBILITY.RoomAll);
        return client;

        //Initialise Visibilities

    }
}
