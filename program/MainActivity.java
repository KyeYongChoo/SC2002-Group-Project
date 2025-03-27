package program;

import program.Project.ROOM_TYPE;
import java.util.Scanner;
// Hey guys do learn how to javadoc here:
// https://www.oracle.com/sg/technical-resources/articles/java/javadoc-tool.html

// Hey guys usually we name the Main class MainActivity cuz autocomplete keeps changing class Main to another Main generated by java engine
public class MainActivity {

    // theres gonna be a whole lot of polymorphism up next folks for buckle up 
    public static UserList applicantList = new UserList();
    public static UserList managerList = new UserList();
    public static UserList officerList = new UserList();
    public static ProjectList projectList = new ProjectList();
    public static HousingReqList reqList = new HousingReqList();
    public static EnquiryList enquiryList = new EnquiryList();
    public static AssignReqList assignReqList = new AssignReqList();
    
    public static void main(String[] args) throws Exception{

        // PRODUCTION CODE 
        // initialise();
        // User client = LogIn();
        // Remember to allow user to log out and log in many times

        // TEST MAIN LOOP
        User client = quickInitialise();

        if (client instanceof Applicant){
            applicantChoices((Applicant) client);
        }
        else if (client instanceof Officer){
            officerChoices((Officer) client);
        }
        else if (client instanceof Manager){
            managerChoices((Manager) client);
        }
    }

    public static void officerChoices(Officer officer) throws Exception{
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        do { 
            if (officer.getProject() != null){
                System.out.println("\nYou are currently handling: " + officer.getProject());
            }
            System.out.println("\nWhat do you want to do: ");
            System.out.println("\n1. View list of projects open to your user group");
            System.out.println("2. Apply for a project");
            System.out.println("3. View project you have applied for, including application status");
            System.out.println("4. Request application withdrawal");
            System.out.println("5. Create, view, delete enquiries");
            System.out.println("6. Register to join a project");
            System.out.println("7. Reply to enquiries regarding project " + officer.getProject());
            System.out.println("8. Log out");

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

            String userInput;
            Project targetProject;
            switch (choice){
                case (1): 
                    ProjectList.printVisible(officer);
                    break;
                case (2):
                    if (!officer.see2Rooms() && officer.see3Rooms()){
                        System.out.println("Sorry, you are not eligible to apply for any kind of HDB");
                        break;
                    }
                    ProjectList.printVisible(officer);
                    do { 
                        System.out.println("Please enter name of project: ");
                        userInput = sc.nextLine();
                        targetProject = projectList.get(userInput);
                    } while (targetProject == null);
                    
                    if (officer.getProject().equals(targetProject)) {
                        System.out.println("Sorry, you are already in charge of this HDB");
                        
                    }
                    if ((officer.see3Rooms() && targetProject.getUnits3Room() == 0 && targetProject.getUnits3Room() == 0)
                    || (officer.see2Rooms() && targetProject.getUnits2Room() == 0)){
                        System.out.println("Sorry. No more vacant rooms");
                        break;
                    }

                    ROOM_TYPE targetRoomType = null;
                    if (officer.see3Rooms()){
                        if (targetProject.getUnits3Room() == 0) targetRoomType = ROOM_TYPE.room2;
                        else if (targetProject.getUnits2Room() == 0) targetRoomType = ROOM_TYPE.room3;
                        else {
                            do { 
                                System.out.println("Please select room type (Enter 1 or 2):");
                                System.out.println("1. 2-Room");
                                System.out.println("2. 3-Room");
                                userInput = sc.nextLine();
                            } while (!"1".equals(userInput) && !"2".equals(userInput));
                            if ("1".equals(userInput)) targetRoomType = ROOM_TYPE.room2;
                            if ("2".equals(userInput)) targetRoomType = ROOM_TYPE.room3;
                        }
                    }
                    else if (officer.see2Rooms()) targetRoomType = ROOM_TYPE.room2;
                    else throw new Exception("Check out the input part of MainActivity this case should never be reachable");
                    if (!reqList.add(officer,targetProject,targetRoomType)){
                        System.out.println("System did not add application.");
                    }
                    
                    break;
                case (3): 
                    officer.printPastReq();
                    break;
                case (4):
                    reqList.reqWithdrawal(officer);
                    break;
                case (5): 
                    userInput="";
                    do { 
                        System.out.println("Which do you want to do? ");
                        System.out.println("1. Create enquiry");
                        System.out.println("2. View enquiry");
                        System.out.println("3. Delete enquiry");
                        userInput = sc.nextLine();
                    } while (!"1".equals(userInput) && !"2".equals(userInput) && !"3".equals(userInput));
                    clearConsole();
                    switch (userInput){
                        case ("1"):
                            System.out.println("Please choose project to enquire about: ");
                            ProjectList.printVisible(officer);
                            targetProject = null;
                            while(targetProject == null){
                                targetProject = projectList.get(sc.nextLine());
                            } 
                            System.out.println("Please enter your enquiry");
                            Enquiry newEnquiry = new Enquiry(officer,sc.nextLine(),targetProject);
                            enquiryList.add(newEnquiry);
                            System.out.println("\nEnquiry saved. \nTime: " + newEnquiry.get(0).getTimeStamp() + "\nMessage: " + newEnquiry.get(0).getText());
                            break;
                        case ("2"):
                            EnquiryList.printPastEnq(officer);
                            break;
                        case ("3"):
                            Enquiry targetEnquiry = EnquiryList.selectEnquiry(officer);
                            EnquiryList.delete(targetEnquiry);
                            break;
                    }
                    break;
                    
                case (6):
                    targetProject = null;
                    ProjectList.printVisible(officer);

                    do { 
                        System.out.println("Please enter name of project: ");
                        userInput = sc.nextLine();
                        targetProject = projectList.get(userInput);
                    } while (targetProject == null);

                    assignReqList.add(officer,targetProject);
                    break;
            }
        }while (choice !=8);
        sc.close();
    }


    public static void managerChoices(Manager manager) throws Exception{

    }
    public static void applicantChoices(Applicant client) throws Exception{
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        do { 
            System.out.println("\nWhat do you want to do: ");
            System.out.println("\n1. View list of projects open to your user group");
            System.out.println("2. Apply for a project");
            System.out.println("3. View project you have applied for, including application status");
            System.out.println("4. Request application withdrawal");
            System.out.println("5. Create, view, delete enquiries");
            System.out.println("6. Log out");

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

            String userInput;
            Project targetProject;
            switch (choice){
                case (1): 
                    ProjectList.printVisible(client);
                    break;
                case (2):
                    if (!client.see2Rooms() && client.see3Rooms()){
                        System.out.println("Sorry, you are not eligible to apply for any kind of HDB");
                        break;
                    }
                    ProjectList.printVisible(client);
                    do { 
                        System.out.println("Please enter name of project: ");
                        userInput = sc.nextLine();
                        targetProject = projectList.get(userInput);
                    } while (targetProject == null);
                    
                    if ((client.see3Rooms() && targetProject.getUnits3Room() == 0 && targetProject.getUnits3Room() == 0)
                    || (client.see2Rooms() && targetProject.getUnits2Room() == 0)){
                        System.out.println("Sorry. No more vacant rooms");
                        break;
                    }

                    ROOM_TYPE targetRoomType = null;
                    if (client.see3Rooms()){
                        if (targetProject.getUnits3Room() == 0) targetRoomType = ROOM_TYPE.room2;
                        else if (targetProject.getUnits2Room() == 0) targetRoomType = ROOM_TYPE.room3;
                        else {
                            do { 
                                System.out.println("Please select room type (Enter 1 or 2):");
                                System.out.println("1. 2-Room");
                                System.out.println("2. 3-Room");
                                userInput = sc.nextLine();
                            } while (!"1".equals(userInput) && !"2".equals(userInput));
                            if ("1".equals(userInput)) targetRoomType = ROOM_TYPE.room2;
                            if ("2".equals(userInput)) targetRoomType = ROOM_TYPE.room3;
                        }
                    }
                    else if (client.see2Rooms()) targetRoomType = ROOM_TYPE.room2;
                    else throw new Exception("Check out the input part of MainActivity this case should never be reachable");
                    if (!reqList.add(client,targetProject,targetRoomType)){
                        System.out.println("System did not add application.");
                    }
                    
                    break;
                case (3): 
                    client.printPastReq();
                    break;
                case (4):
                    reqList.reqWithdrawal(client);
                    break;
                case (5): 
                    userInput="";
                    do { 
                        System.out.println("Which do you want to do? ");
                        System.out.println("1. Create enquiry");
                        System.out.println("2. View enquiry");
                        System.out.println("3. Delete enquiry");
                        userInput = sc.nextLine();
                    } while (!"1".equals(userInput) && !"2".equals(userInput) && !"3".equals(userInput));
                    clearConsole();
                    switch (userInput){
                        case ("1"):
                            System.out.println("Please choose project to enquire about: ");
                            ProjectList.printVisible(client);
                            targetProject = null;
                            while(targetProject == null){
                                targetProject = projectList.get(sc.nextLine());
                            } 
                            System.out.println("Please enter your enquiry");
                            Enquiry newEnquiry = new Enquiry(client,sc.nextLine(),targetProject);
                            enquiryList.add(newEnquiry);
                            System.out.println("\nEnquiry saved. \nTime: " + newEnquiry.get(0).getTimeStamp() + "\nMessage: " + newEnquiry.get(0).getText());
                            break;
                        case ("2"):
                            EnquiryList.printPastEnq(client);
                            break;
                        case ("3"):
                            Enquiry targetEnquiry = EnquiryList.selectEnquiry(client);
                            EnquiryList.delete(targetEnquiry);
                            break;
                    }
                    break;
                case (6): 
                    break;
            }
            System.out.println("Press enter to continue");
            sc.nextLine();
        } while (choice != 6);

        
        sc.close();
    }
    
    // Open up a new page by clearing Console
    public static void clearConsole() {
        System.out.print("\033[H\033[2J\033[3J");
        System.out.flush();        
    }


    
    public static User LogIn(){
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("\nPlease Log In:");
            for (int attemptsLeft =  4; attemptsLeft >= 0; attemptsLeft--){
                String NRIC = User.inputNRIC();
                System.out.println("Please enter User Password: ");
                String password = sc.nextLine();
                User client = applicantList.get(NRIC);
                if (client == null) client = officerList.get(NRIC);
                if (client == null) client = managerList.get(NRIC);
                //if client does not exist or client's password incorrect
                if (client != null && client.verifyPassword(password)){
                    return client;
                }
                System.out.println("Wrong Username or Password. Number of tries left: " + attemptsLeft);
            }
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
    public static void updateTableRef(boolean table3Roomformatting){
        if (table3Roomformatting){
            formatTableRef = "%-20s %-20s %-12s %-8s %-12s %-8s %-26s %-13s %-10s %-15s %-10s\n";
            tableHeaders = new String[]{"Project Name","Neighbourhood","No. 2 Room","Price","No. 3 Room","Price","Application Opening Date","Closing Date","Manager","Officer Slot","Officer"};
        }
        else{
            formatTableRef = "%-20s %-20s %-12s %-8s %-26s %-13s %-10s %-15s %-10s\n";
            tableHeaders = new String[]{"Project Name","Neighbourhood","No. 2-Room","Price","Application Opening Date","Closing Date","Manager","Officer Slot","Officer"};
        }
    }

    @SuppressWarnings("resource")
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
        sc.close();

        for (String line : allInput.split("\n")) {
            String[] fields = line.split("\t");
            if (fields[0].equals("Name") || fields[0].trim().isEmpty()){ 
                // If in the first row full of column names or empty, skip
                continue;
            }
            if (fields.length != 5) throw new Exception ("Please enter all 5 fields");
            applicantList.add(new Applicant(fields[1].trim(), fields[0].trim(), Integer.parseInt(fields[2].trim()), fields[3].trim(), fields[4].trim()));
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
        sc.close();

        for (String line : allInput.split("\n")) {
            String[] fields = line.split("\t");
            if (fields[0].equals("Name") || fields[0].trim().isEmpty()){ 
                // If in the first row full of column names or empty, skip
                continue;
            }
            if (fields.length != 5) {
                throw new Exception ("Please enter all 5 fields");
            }
            officerList.add(new Officer(fields[1].trim(), fields[0].trim(), Integer.parseInt(fields[2].trim()), fields[3].trim(), fields[4].trim()));
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
        sc.close();

        for (String line : allInput.split("\n")) {
            String[] fields = line.split("\t");
            if (fields[0].equals("Name") || fields[0].trim().isEmpty()){ 
                // If in the first row full of column names or empty, skip
                continue;
            }
            if (fields.length != 5) throw new Exception ("Please enter all 5 fields");
            managerList.add(new Manager(fields[1].trim(), fields[0].trim(), Integer.parseInt(fields[2].trim()), fields[3].trim(), fields[4].trim()));
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
        sc.close();
        for (String line : allInput.split("\n")) {
            String[] fields = line.split("\t");
            if (fields[0].equals("Project Name") || fields[0].trim().isEmpty()){ 
                // If in the first row full of column names or empty, skip
                continue;
            }
            if (fields.length != 13) {
                throw new Exception ("Please enter all 5 fields");
            }
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
        applicantList.add(new Applicant("S1234567A","John",35,"Single","password"));
        applicantList.add(new Applicant("T7654321B","Sarah",40,"Married","password"));
        applicantList.add(new Applicant("S9876543C","Grace",37,"Married","password"));
        applicantList.add(new Applicant("T2345678D","James",30,"Married","password"));
        applicantList.add(new Applicant("S3456789E","Rachel",25,"Single","password"));

        // Officers
        officerList.add(new Officer("T2109876H","Daniel",36,"Single","password"));
        officerList.add(new Officer("S6543210I","Emily",28,"Single","password"));
        officerList.add(new Officer("T1234567J","David",29,"Married","password"));

        // Managers
        managerList.add(new Manager("T8765432F","Michael",36,"Single","password"));
        managerList.add(new Manager("S5678901G","Jessica",26,"Married","password"));

        // Projects
        projectList.add(new Project("Acacia Breeze","Yishun","2","350000","3","450000","15/2/2025","20/3/2025","Jessica","3","Daniel,Emily"));
        projectList.add(new Project("Beta Breeze","Sembahwang","2","350000","3","450000","15/2/2025","20/3/2025","Michael","3","Daniel,Emily"));

        //Login
        User client = officerList.get(2);
        return client;

    }
}
