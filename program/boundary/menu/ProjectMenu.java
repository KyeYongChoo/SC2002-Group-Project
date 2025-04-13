package program.boundary.menu;

import program.boundary.menuTemplate.MenuGroup;
import program.boundary.projectIO.ProjectSelect;
import program.boundary.projectIO.ProjectSelector;
import program.boundary.projectIO.RoomTypeSelector;
import program.control.Main;
import program.entity.project.Project;
import program.entity.project.Project.ROOM_TYPE;
import program.entity.users.Manager;
import program.entity.users.Officer;
import program.entity.users.User;

public class ProjectMenu extends MenuGroup {
    public ProjectMenu(User user) {
        super("Manage Projects");

        this.addMenuItem("View projects", () -> {
            ProjectSelect.printVisible(user);
        });

        this.addMenuItem("Apply to a project", 
            () -> {
                Project project = ProjectSelector.chooseVisibleProjectWithoutConflict(user, Main.projectList);
                ROOM_TYPE targetRoomType = RoomTypeSelector.selectRoomType(user, project);

                // Add the application to the request list
                Main.reqList.add(user, project, targetRoomType);
                System.out.println("Application submitted successfully.");
            }, 
            // If the user is not a manager and satisfies one of the BTO requirements, they can apply for projects
            dummyVar -> !(user instanceof Manager) && 
                        ((user.see2Rooms()) || (user.see3Rooms()))
        );

        this.addMenuItem("View your applications",
            () -> user.printPastReq(),
            dummyVar -> !(user instanceof Manager) // manager class has no applications
        );

        this.addMenuItem("Request application withdrawal",
            () -> Main.reqList.reqWithdrawal(user),
            dummyVar -> !(user instanceof Manager) || // manager class has no applications
                        !(user.hasActiveApplication())  // can't withdraw if you havent applied
        );

        this.addMenuItem("Join project as HDB Officer",
            () -> {
                
            },
            dummyVar -> user instanceof Officer // only officers may join projects
        );
    }
}
