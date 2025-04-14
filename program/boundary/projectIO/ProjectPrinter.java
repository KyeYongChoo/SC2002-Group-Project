package program.boundary.projectIO;

import program.control.Main;
import program.entity.project.Project;
import program.entity.users.Manager;
import program.entity.users.Officer;
import program.entity.users.User;

public class ProjectPrinter {

    // Overloaded method: Print all visible projects for a client
    public static void printVisible(User client) {
        UserPrefSorting.userFilterStream(client, Main.projectList)
            .filter(project -> project.isVisibleTo(client)) // Use Project's isVisibleTo method
            .forEach(project -> printVisible(client, project)); // Delegate to the overloaded method
    }

    // Overloaded method: Print a specific project for a client
    public static void printVisible(User client, Project project) {
        if (client == null || project == null || !project.isVisibleTo(client)) {
            System.out.println("The project is not visible to the user or invalid input.");
            return;
        }

        printProjectDetails(project, client.see3Rooms());
    }

    /**
     * Prints the details of a project in a line-by-line format.
     * @param project The project to print.
     * @param show3Room Whether to include 3-room details.
     */
    public static void printProjectDetails(Project project, boolean show3Room) {
        System.out.println("========================================");
        System.out.println("Project Name       : " + project.getName());
        System.out.println("Neighborhood       : " + project.getNeighbourhood());
        System.out.println("Available 2-Room   : " + project.getUnits2Room());
        System.out.println("Price (2-Room)     : " + project.getUnits2RoomPrice());
        if (show3Room) {
            System.out.println("Available 3-Room   : " + project.getUnits3Room());
            System.out.println("Price (3-Room)     : " + project.getUnits3RoomPrice());
        }
        System.out.println("Application Open   : " + project.getOpenDate());
        System.out.println("Application Close  : " + project.getCloseDate());
        System.out.println("Manager            : " + project.getManager());
        System.out.println("Officer Slots      : " + project.getOfficerSlots());
        System.out.println("Assigned Officers  :");
        for (User officer : project.getOfficers()) {
            System.out.println("  - " + officer.getName());
        }
        System.out.println("========================================");
    }
}