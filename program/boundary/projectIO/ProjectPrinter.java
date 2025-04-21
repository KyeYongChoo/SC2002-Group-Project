package program.boundary.projectIO;

import program.control.Main;
import program.entity.project.Project;
import program.entity.users.Manager;
import program.entity.users.User;

/**
 * {@code ProjectPrinter} provides utility methods for displaying project information to users.
 * This class focuses on presenting project details in a user-friendly format,
 * taking into account the user's preferences and visibility permissions.
 */
public class ProjectPrinter {

    /**
     * Overloaded method: Prints all projects that are visible to a specific client.
     * This method retrieves a stream of projects, filters it to include only those
     * that the given client has permission to see (using the {@code isVisibleTo} method
     * of the {@link Project} class), and then prints the details of each visible project.
     * It delegates the actual printing of each project's details to the overloaded
     * {@link #printVisible(User, Project)} method.
     *
     * @param client The {@link User} object representing the client who is viewing the projects.
     *               This parameter is essential for determining which projects are visible to the client.
     */
    public static void printVisible(User client) {
        UserPrefSorting.userFilterStream(client, Main.projectList)
            .filter(project -> project.isVisibleTo(client)) // Use Project's isVisibleTo method
            .forEach(project -> printVisible(client, project)); // Delegate to the overloaded method
    }

    /**
     * Overloaded method: Prints the details of a specific project, but only if it is visible to the given client.
     * This method first performs checks to ensure that both the {@code client} and the {@code project}
     * objects are not {@code null}. It then verifies if the client has the necessary permission
     * to view the project using the {@code isVisibleTo} method of the {@link Project} class.
     * If all these checks pass, it proceeds to print the project details.
     *
     * @param client  The {@link User} object representing the client attempting to view the project.
     * @param project The specific {@link Project} object whose details are to be printed.
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
            System.out.println("ProjectPrinter.printVisible: client can't see project. Refer Project.isVisible()");
            return;
        }

        printProjectDetails(project, client.see3Rooms());
    }

    /**
     * Returns the details of a project as a formatted string.
     * This method constructs a string containing key information about the project,
     * such as its name, neighborhood, availability and price of 2-room units, and optionally
     * the availability and price of 3-room units (depending on the {@code show3Room} parameter).
     * It also includes the application open and close dates, the assigned manager, the number of
     * officer slots, and the names of the currently assigned officers.
     *
     * @param project   The {@link Project} object for which to retrieve the details.
     * @param show3Room A boolean value indicating whether to include details about 3-room units.
     *                  If {@code true}, the availability and price of 3-room units will be included in the string.
     * @return A {@link String} containing the formatted details of the project.
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
     * Prints the details of a project to the console in a line-by-line format.
     * This method utilizes the {@link #getProjectDetailsString(Project, boolean)} method
     *                  in the printed output.
     */
    public static void printProjectDetails(Project project, boolean show3Room) {
        System.out.print(getProjectDetailsString(project, show3Room));
    }
}
