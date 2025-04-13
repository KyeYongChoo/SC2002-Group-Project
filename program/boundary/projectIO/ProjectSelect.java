package program.boundary.projectIO;

import program.control.Main;
import program.entity.project.Project;
import program.entity.users.Officer;
import program.entity.users.User;

public class ProjectSelect {

    public static void printVisible(User client) {
        ProjectPrinter.printVisible(client); // Delegate to ProjectPrinter
    }

    public static void printVisible(User client, Project project) {
        ProjectPrinter.printVisible(client, project); // Delegate to ProjectPrinter
    }

    public static Project chooseVisibleProject(User client) {
        return ProjectSelector.chooseVisibleProject(client, Main.projectList);
    }

    public static Project chooseVisibleProjectWithoutConflict(User client) {
        return ProjectSelector.chooseVisibleProjectWithoutConflict(client, Main.projectList);
    }

    /**
     * Allows an officer to choose a project based on specific filtering rules.
     * @param officer The officer selecting the project.
     * @return The selected project, or null if no valid selection is made.
     */
    public static Project chooseProjectForOfficer(Officer officer) {
        return ProjectSelector.chooseProjectForOfficer(officer, Main.projectList);
    }
}
