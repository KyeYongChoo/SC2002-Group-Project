package program.boundary.projectIO;

import java.util.Scanner;

import program.boundary.console.AppScanner;
import program.boundary.menuTemplate.MenuAction;
import program.control.Main;
import program.entity.project.Project;
import program.entity.users.User;

public class SetUpProject implements MenuAction{
    User user;
    private static Scanner sc = AppScanner.getInstance();
    public SetUpProject(User user){
        this.user = user;
    }
    @Override
    public void execute(){
        try {
            System.out.println("Enter Project Name:");
            String name = sc.nextLine().trim();
            if (name.isEmpty()) throw new Exception("Project Name cannot be empty.");
    
            System.out.println("Enter Neighborhood:");
            String neighborhood = sc.nextLine().trim();
            if (neighborhood.isEmpty()) throw new Exception("Neighborhood cannot be empty.");
    
            System.out.println("Enter number of 2-room units:");
            String units2room = sc.nextLine().trim();
            if (!units2room.matches("\\d+")) throw new Exception("Invalid number of 2-room units.");
    
            System.out.println("Enter price for 2-room units:");
            String units2roomPrice = sc.nextLine().trim();
            if (!units2roomPrice.matches("\\d+")) throw new Exception("Invalid price for 2-room units.");
    
            System.out.println("Enter number of 3-room units:");
            String units3room = sc.nextLine().trim();
            if (!units3room.matches("\\d+")) throw new Exception("Invalid number of 3-room units.");
    
            System.out.println("Enter price for 3-room units:");
            String units3roomPrice = sc.nextLine().trim();
            if (!units3roomPrice.matches("\\d+")) throw new Exception("Invalid price for 3-room units.");
    
            System.out.println("Enter application opening date (dd/MM/yyyy):");
            String openDate = sc.nextLine().trim();
            if (!openDate.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) throw new Exception("Invalid opening date format.");
    
            System.out.println("Enter application closing date (dd/MM/yyyy):");
            String closeDate = sc.nextLine().trim();
            if (!closeDate.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) throw new Exception("Invalid closing date format.");
    
            System.out.println("Enter number of officer slots:");
            String officerSlots = sc.nextLine().trim();
            if (!officerSlots.matches("\\d+")) throw new Exception("Invalid number of officer slots.");
    
            System.out.println("Enter comma-separated list of officer names (leave blank if none):");
            String officerList = sc.nextLine().trim();
    
            // Create the project
            Project newProject = new Project(
                name,
                neighborhood,
                units2room,
                units2roomPrice,
                units3room,
                units3roomPrice,
                openDate,
                closeDate,
                user.getName(), // Assuming `user` is the Manager creating the project
                officerSlots,
                officerList
            );
    
            Main.projectList.add(newProject);
            System.out.println("Project created successfully!");
    
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
