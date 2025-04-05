package program;

import program.Project.ROOM_TYPE;
import program.security.LoginHandler;
import program.security.PasswordResetHandler;

import java.util.Scanner;

public class Main {
    private static Scanner sc = new Scanner(System.in);

    // theres gonna be a whole lot of polymorphism up next folks for buckle up 
    public static UserList applicantList = new UserList();
    public static UserList managerList = new UserList();
    public static UserList officerList = new UserList();
    public static ProjectList projectList = new ProjectList();
    public static HousingReqList reqList = new HousingReqList();
    public static EnquiryList enquiryList = new EnquiryList();
    public static AssignReqList assignReqList = new AssignReqList();
    
    public static void main(String[] args) throws Exception{
        DataInitializer.initialise();
        User client = LoginHandler.loginUser();
        // Remember to allow user to log out and log in many times

        if (client instanceof Applicant){
            applicantChoices((Applicant) client);
        }
        else if (client instanceof Officer){
            officerChoices((Officer) client);
        }
        else if (client instanceof Manager){
            managerChoices((Manager) client);
        }

        RecordSaver.save();
    }

    public static void officerChoices(Officer officer) throws Exception{
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
            System.out.println("8. Change password");
            System.out.println("9. Log out");

            try {
                choice = Integer.parseInt(sc.nextLine());
                clearConsole();
                if (choice < 1 || choice > 9){
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
                case (8):
                    System.out.println("Please enter your new password: ");
                    userInput = sc.nextLine();
                    PasswordResetHandler.resetPassword(officer, userInput);
                    break;
            }
        }while (choice !=9);
    }


    public static void managerChoices(Manager manager) throws Exception{

    }
    public static void applicantChoices(Applicant client) throws Exception{
        int choice = 0;
        do { 
            System.out.println("\nWhat do you want to do: ");
            System.out.println("\n1. View list of projects open to your user group");
            System.out.println("2. Apply for a project");
            System.out.println("3. View project you have applied for, including application status");
            System.out.println("4. Request application withdrawal");
            System.out.println("5. Create, view, delete enquiries");
            System.out.println("6. Change password");
            System.out.println("7. Log out");

            try {
                choice = Integer.parseInt(sc.nextLine());
                clearConsole();
                if (choice < 1 || choice > 7){
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
                    System.out.println("Please enter your new password: ");
                    userInput = sc.nextLine();
                    PasswordResetHandler.resetPassword(client, userInput);
                    break;
                case (7): 
                    break;
            }
            System.out.println("Press enter to continue");
            sc.nextLine();
        } while (choice != 7);
    }
    
    // Open up a new page by clearing Console
    public static void clearConsole() {
        System.out.print("\033[H\033[2J\033[3J");
        System.out.flush();        
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
}
