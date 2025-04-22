package program.boundary.menu;

import program.boundary.menuTemplate.MenuGroup;
import program.boundary.projectIO.ProjectSelect;
import program.boundary.projectIO.ProjectSelector;
import program.boundary.projectIO.RoomTypeSelector;
import program.control.Main;
import program.control.housingApply.HousingReqList;
import program.entity.project.Project;
import program.entity.project.Project.ROOM_TYPE;
import program.entity.users.Manager;
import program.entity.users.User;

/**
 * <p>
 * The {@code ProjectApplicationMenu} class provides applicants with a user-friendly
 * interface to manage their applications for Build-To-Order (BTO) projects. This menu
 * offers various options, including viewing available projects, applying for a project,
 * viewing past applications, and requesting withdrawals of applications.
 * </p>
 *
 * <p>
 * Access to this menu is granted to all users except those who are instances of
 * {@link Manager}. This restriction ensures that only eligible applicants can utilize
 * the functionalities provided in this menu.
 * </p>
 *
 * @see program.boundary.menuTemplate.MenuGroup
 */
public class ProjectApplicationMenu extends MenuGroup {

    /**
     * <p>
     * Constructs a {@code ProjectApplicationMenu} for the specified user. This menu
     * includes several options that allow users to manage their BTO applications effectively.
     * The available options are:
     * </p>
     * <ul>
     *     <li>Viewing available projects, enabling users to see which BTO projects are open for application.</li>
     *     <li>Submitting an application for a selected project, allowing users to express their interest in a specific project.</li>
     *     <li>Viewing past applications, providing users with a record of their previous submissions.</li>
     *     <li>Requesting to withdraw an application, enabling users to cancel their application if needed.</li>
     * </ul>
     *
     * <p>
     * The menu is initialized with a title that reflects its purpose and ensures that
     * only users who are not managers can access these functionalities.
     * </p>
     *
     * @param user the {@link User} accessing the menu; must not be a {@link Manager}.
     *             This parameter is essential for enforcing access control and ensuring
     *             that only eligible users can interact with the menu options.
     */
    public ProjectApplicationMenu(User user) {
        super("Manage your BTO Application",
                user_ -> !(user_ instanceof Manager));

        // Menu option to view available projects
        this.addMenuItem("View projects", () -> {
            ProjectSelect.printVisible(user);
        });

        // Menu option to apply to a project
        this.addMenuItem("Apply to a project", () -> {
                    Project project = ProjectSelector.chooseProjectsApplyAsApplicant(user, Main.projectList);
                    if (project == null) return; // Exit if no project is selected
                    ROOM_TYPE targetRoomType = RoomTypeSelector.selectRoomType(user, project);
                    if (targetRoomType == null){
                        return;
                    }
                    Main.housingReqList.add(user, project, targetRoomType); // Add the application to the request list
                    System.out.println("Application submitted successfully.");
                },
                user_ -> (user_.see2Rooms()) || (user_.see3Rooms())); // Conditional access based on room type visibility

        // Menu option to view past applications
        this.addMenuItem("View your applications",
                () -> HousingReqList.printPast(user)
        );

        // Menu option to request application withdrawal
        this.addMenuItem("Request application withdrawal",
                () -> Main.housingReqList.reqWithdrawal(user),
                user_ -> !(user_.hasActiveApplication()) // Restrict access if the user has an active application
        );
    }
}
