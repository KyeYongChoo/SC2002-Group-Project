package program.boundary.menu;

import program.boundary.menuTemplate.MenuGroup;
import program.boundary.officerAssignIO.OfficerAssignPrinter;
import program.boundary.projectIO.ProjectSelector;
import program.control.Main;
import program.entity.project.Project;
import program.entity.users.Manager;
import program.entity.users.Officer;
import program.entity.users.User;

/**
 * <p>
 * The {@code OfficerApplyMenu} class provides a user interface specifically designed
 * for HDB Officers to manage their profiles, view their registration status, and apply
 * to join projects as officers. This menu facilitates the necessary functionalities
 * that officers require to engage with project assignments effectively.
 * </p>
 *
 * <p>
 * Access to this menu is restricted to users who are instances of {@link Officer} and
 * explicitly excludes users who are instances of {@link Manager}. This ensures that
 * only authorized personnel can access the functionalities provided in this menu.
 * </p>
 *
 * @see program.boundary.menuTemplate.MenuGroup
 */
public class OfficerApplyMenu extends MenuGroup {

    /**
     * <p>
     * Constructs an {@code OfficerApplyMenu} for the specified user. This menu includes
     * several options that allow the officer to manage their applications and view relevant
     * information. The available options are:
     * </p>
     * <ul>
     *     <li>Display the officer's profile greeting, providing a personalized welcome message.</li>
     *     <li>View the registration status for project assignments, allowing officers to check their current standing.</li>
     *     <li>Apply to join a project as an HDB Officer, enabling officers to express interest in specific projects.</li>
     * </ul>
     *
     * <p>
     * The menu is initialized with a greeting message tailored to the officer and ensures
     * that only users with the appropriate role can access these functionalities.
     * </p>
     *
     * @param user the {@link User} accessing this menu; must be an {@link Officer}
     *             and not a {@link Manager}. This parameter is crucial for ensuring
     *             that the menu is only available to the correct user type.
     */
    public OfficerApplyMenu(User user) {
        super("Manage your HDB Officer applications ",
                user_ -> user_ instanceof Officer && !(user instanceof Manager));

        // Menu option to display the officer's profile greeting
        this.addMenuItem("Check your Profile", () ->{
            System.out.println(user.getGreeting());
            OfficerAssignPrinter.printAssignReq((Officer) user);
        }); // Prints a greeting and the status of the officer's registration for the user

        // Menu option to check the registration status for project assignments
        this.addMenuItem("Check status of registration ", () ->
                OfficerAssignPrinter.printAssignReq((Officer) user));

        // Menu option to apply to join a project as an HDB Officer
        this.addMenuItem("Join project as HDB Officer", () -> {
            Project targetProject = ProjectSelector.chooseProjectsApplyAsOfficer((Officer) user, Main.projectList);
            if (targetProject == null) return; // Exit if no project is selected
            Main.assignReqList.add((Officer) user, targetProject); // Add the officer's application to the request list
        });
    }
}