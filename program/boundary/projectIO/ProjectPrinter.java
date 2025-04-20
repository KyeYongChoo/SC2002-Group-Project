package program.boundary.projectIO;

import program.control.Main;
import program.entity.project.Project;
import program.entity.users.Manager;
import program.entity.users.User;

public class ProjectPrinter {

    // Overloaded method: Print all visible projects for a client
    public static void printVisible(User client) {
        UserPrefSorting.userFilterStream(client, Main.projectList)
            .filter(project -> project.isVisibleTo(client)) // Use Project's isVisibleTo method
            .forEach(project -> printVisible(client, project)); // Delegate to the overloaded method
    }

    /*
     * manager can see all rooms but for a lot of purposes isVisibleTo will filter away choices the Manager cant use. 
     * This method prints all, userFilters is the ones the manager puts on themselves. 
     */ 
    public static void printManagerViewAll(Manager manager){
        UserPrefSorting.userFilterStream(manager, Main.projectList)
            .forEach(project -> printProjectDetails(project, true));
    }

    // Overloaded method: Print a specific project for a client
    public static void printVisible(User client, Project project) {
        if (client == null){
            System.out.println("ProjectPrinter.printVisible: Client null");
            return;
        }
        if (project == null){
            System.out.println("ProjectPrinter.printVisible: Project null");
            return;
        }
        if (!project.isVisibleTo(client)) {
            System.out.println("ProjectPrinter.printVisible: client cant see project. Refer Project.isVisible()");
            return;
        }

        printProjectDetails(project, client.see3Rooms());
    }

    /**
     * Returns the details of a project as a string.
     * @param project The project to get details for.
     * @param show3Room Whether to include 3-room details.
     * @return A string containing the project details.
     */
    public static String getProjectDetailsString(Project project, boolean show3Room) {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("Project Name       : ").append(project.getName()).append("\n");
        sb.append("Neighborhood       : ").append(project.getNeighbourhood()).append("\n");
        sb.append("Available 2-Room   : ").append(project.getUnits2Room()).append("\n");
        sb.append("Price (2-Room)     : ").append(project.getUnits2RoomPrice()).append("\n");
        if (show3Room) {
            sb.append("Available 3-Room   : ").append(project.getUnits3Room()).append("\n");
            sb.append("Price (3-Room)     : ").append(project.getUnits3RoomPrice()).append("\n");
        }
        sb.append("Application Open   : ").append(project.getOpenDate()).append("\n");
        sb.append("Application Close  : ").append(project.getCloseDate()).append("\n");
        sb.append("Manager            : ").append(project.getManager()).append("\n");
        sb.append("Officer Slots      : ").append(project.getOfficerSlots()).append("\n");
        sb.append("Assigned Officers  :\n");
        for (User officer : project.getOfficers()) {
            sb.append("  - ").append(officer.getName()).append("\n");
        }
        sb.append("========================================\n");
        return sb.toString();
    }

    /**
     * Prints the details of a project in a line-by-line format.
     * @param project The project to print.
     * @param show3Room Whether to include 3-room details.
     */
    public static void printProjectDetails(Project project, boolean show3Room) {
        System.out.print(getProjectDetailsString(project, show3Room));
    }
}