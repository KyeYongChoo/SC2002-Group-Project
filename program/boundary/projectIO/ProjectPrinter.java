package program.boundary.projectIO;

import program.control.Main;
import program.entity.project.Project;
import program.entity.users.Manager;
import program.entity.users.Officer;
import program.entity.users.User;

public class ProjectPrinter {
    // Overloaded method: Print all visible projects for a client
    public static void printVisible(User client) {
        Main.projectList.stream()
            .filter(project -> project.isVisibleTo(client)) // Use Project's isVisibleTo method
            .filter(project -> ProjectFilter.filterByFlatType(project, client.getFilterSetting())) // Additional filtering for flat types
            .sorted((p1, p2) -> ProjectFilter.compareProjects(p1, p2, client.getFilterSetting())) // Sort based on filter setting
            .forEach(project -> printVisible(client, project)); // Delegate to the overloaded method
    }

    // Overloaded method: Print a specific project for a client
    public static void printVisible(User client, Project project) {
        if (client == null || project == null || !project.isVisibleTo(client)) {
            System.out.println("The project is not visible to the user or invalid input.");
            return;
        }

        if (client instanceof Manager) {
            printForManager((Manager) client, project);
        } else if (client instanceof Officer) {
            printForOfficer((Officer) client, project);
        } else {
            printForUser(client, project);
        }
    }

    public static void printTableHeader(boolean show3Room) {
        updateTableRef(show3Room);
        System.out.printf(formatTableRef, (Object[]) tableHeaders);
    }

    public static void printProjectDetails(Project project, boolean show3Room) {
        boolean firstLoop = true;
        for (User officer : project.getOfficers()) {
            Officer projOfficer = (Officer) officer;

            if (firstLoop) {
                firstLoop = false;
                printProjectRow(project, show3Room, projOfficer);
            } else {
                printOfficerRow(projOfficer);
            }
        }
    }

    private static void printForManager(Manager manager, Project project) {
        printTableHeader(true);
        printProjectDetails(project, true);
    }

    private static void printForOfficer(Officer officer, Project project) {
        boolean show3Room = officer.see3Rooms();
        printTableHeader(show3Room);
        printProjectDetails(project, show3Room);
    }

    private static void printForUser(User client, Project project) {
        boolean show3Room = client.see3Rooms();
        printTableHeader(show3Room);
        printProjectDetails(project, show3Room);
    }

    private static void printProjectRow(Project project, boolean show3Room, Officer projOfficer) {
        if (show3Room) {
            System.out.printf(
                formatTableRef,
                project.getName(),
                project.getNeighbourhood(),
                project.getUnits2Room(),
                project.getUnits2RoomPrice(),
                project.getUnits3Room(),
                project.getUnits3RoomPrice(),
                project.getOpenDate(),
                project.getCloseDate(),
                project.getManager(),
                project.getOfficerSlots(),
                projOfficer
            );
        } else {
            System.out.printf(
                formatTableRef,
                project.getName(),
                project.getNeighbourhood(),
                project.getUnits2Room(),
                project.getUnits2RoomPrice(),
                "", // No 3-room details
                "",
                project.getOpenDate(),
                project.getCloseDate(),
                project.getManager(),
                project.getOfficerSlots(),
                projOfficer
            );
        }
    }

    private static void printOfficerRow(Officer projOfficer) {
        System.out.printf(
            formatTableRef,
            "", "", "", "", "", "", "", "", "", "", projOfficer
        );
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