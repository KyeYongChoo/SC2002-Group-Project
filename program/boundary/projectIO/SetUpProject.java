package program.boundary.projectIO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import program.boundary.console.AppScanner;
import program.boundary.console.DateTimeFormat;
import program.boundary.menuTemplate.MenuAction;
import program.control.Main;
import program.control.TimeCompare;
import program.entity.project.Project;
import program.entity.users.Manager;
import program.entity.users.User;

public class SetUpProject implements MenuAction{
    User user;
    private static Scanner sc = AppScanner.getInstance();
    // the reason why this takes a user argument rather than a Manager argument is due to Lazy Instantiation problems when applicant is called, this quick fix is far easier than the more principled way to solve it
    public SetUpProject(User user){
        this.user = user;
    }
    @Override
    public void execute() {
        try {
            Manager manager = (Manager) user;

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

            System.out.println("Take note you must not have a project assigned to you when you are busy");
            System.out.println("Times when you are busy: ");
            Main.projectList.stream().filter(project -> project.isManager(manager)).forEach(
                project -> System.out.println("Start Date: " + project.getOpenDate().format(DateTimeFormat.getDateFormatter()) + 
                                               "\nClose Date: " + project.getCloseDate().format(DateTimeFormat.getDateFormatter()))
            );

            System.out.println("Enter application opening date (dd-MM-yyyy):");
            String openDate = sc.nextLine().trim();

            System.out.println("Enter application closing date (dd-MM-yyyy):");
            String closeDate = sc.nextLine().trim();

            LocalDate parsedOpenDate;
            LocalDate parsedCloseDate;
            try {
                parsedOpenDate = LocalDate.parse(openDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                parsedCloseDate = LocalDate.parse(closeDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            } catch (DateTimeParseException e) {
                throw new Exception("Invalid date format. Please use dd-MM-yyyy.");
            }

            // Check if Open Date is after Close Date
            if (parsedOpenDate.isAfter(parsedCloseDate)) {
                throw new Exception("Open Date (" + parsedOpenDate.format(DateTimeFormat.getDateFormatter()) +
                    ") cannot be after Close Date (" + parsedCloseDate.format(DateTimeFormat.getDateFormatter()) + ").");
            }

            // Check for overlapping dates with other projects
            if (Main.projectList.stream()
                .anyMatch(project -> project.isManager(manager) &&
                    !(TimeCompare.timeSeparate(parsedOpenDate, parsedCloseDate, project.getOpenDate(), project.getCloseDate())))) {
                throw new Exception("You have another project assigned to you at that moment.");
            }

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
                manager.getName(), 
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
