package program.boundary.projectIO;

import program.control.Main;
import program.entity.project.Project;
import program.entity.users.User;

/**
 * <p>
 * The {@code ProjectSelect} class acts as a facade, offering simplified methods for displaying
 * and selecting {@link Project} objects that are visible to a specific {@link User}.
 * </p>
 *
 * <p>
 * It delegates the actual logic for printing and selecting projects to specialized classes such as
 * {@link ProjectPrinter} and {@link ProjectSelector}.
 * </p>
 */
public class ProjectSelect {

    /**
     * <p>
     * Prints all projects that are visible to a specified client.
     * This method delegates the task of printing visible projects to the
     * {@link ProjectPrinter#printVisible(User)} method.
     * </p>
     *
     * @param client the {@link User} representing the client viewing the projects.
     *               The visibility of projects depends on this client.
     */
    public static void printVisible(User client) {
        ProjectPrinter.printVisible(client); // Delegate printing to ProjectPrinter
    }

    /**
     * <p>
     * Prints the details of a specific project, if it is visible to the given client.
     * This method delegates the printing of the project details to
     * {@link ProjectPrinter#printProjectDetails(Project, boolean)}.
     * It also considers whether the client prefers to view 3-room details.
     * </p>
     *
     * @param client  the {@link User} viewing the project.
     * @param project the {@link Project} whose details are to be printed.
     */
    public static void printVisible(User client, Project project) {
        ProjectPrinter.printProjectDetails(project, client.see3Rooms()); // Delegate to ProjectPrinter with client's preference
    }

    /**
     * <p>
     * Allows a client to choose a project from the list of projects visible to them.
     * The selection logic is delegated to
     * {@link ProjectSelector#chooseVisibleProject(User, java.util.List)}, passing the client
     * and the main list of projects.
     * </p>
     *
     * @param client the {@link User} choosing a project.
     * @return the selected {@link Project} from the visible projects, or {@code null} if no project is chosen or visible.
     */
    public static Project chooseVisibleProject(User client) {
        return ProjectSelector.chooseVisibleProject(client, Main.projectList); // Delegate project selection to ProjectSelector
    }

    /**
     * <p>
     * Allows a client to select a project to apply for, ensuring that no conflicts occur with their
     * existing applications. The selection logic is delegated to
     * {@link ProjectSelector#chooseProjectsApplyAsApplicant(User, java.util.List)}, passing the client
     * and the main list of projects.
     * </p>
     *
     * @param client the {@link User} applying for a project.
     * @return the {@link Project} chosen by the client for application, or {@code null} if no suitable project is found.
     */
    public static Project chooseVisibleProjectWithoutConflict(User client) {
        return ProjectSelector.chooseProjectsApplyAsApplicant(client, Main.projectList); // Delegate conflict-free project selection
    }
}
