package program.boundary.projectIO;

import program.control.Main;
import program.entity.project.Project;
import program.entity.users.User;

// facade pattern :)
public class ProjectSelect {

    public static void printVisible(User client) {
        ProjectPrinter.printVisible(client); // Delegate to ProjectPrinter
    }

    public static void printVisible(User client, Project project) {
        ProjectPrinter.printProjectDetails(project, client.see3Rooms()); // Delegate to ProjectPrinter
    }

    public static Project chooseVisibleProject(User client) {
        return ProjectSelector.chooseVisibleProject(client, Main.projectList);
    }

    public static Project chooseVisibleProjectWithoutConflict(User client) {
        return ProjectSelector.chooseProjectsApplyAsApplicant(client, Main.projectList);
    }
}
