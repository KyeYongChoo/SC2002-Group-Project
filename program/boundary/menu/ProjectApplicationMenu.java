package program.boundary.menu;

import program.boundary.menuTemplate.MenuGroup;
import program.boundary.projectIO.ProjectSelect;
import program.boundary.projectIO.ProjectSelector;
import program.boundary.projectIO.RoomTypeSelector;
import program.control.Main;
import program.entity.project.Project;
import program.entity.project.Project.ROOM_TYPE;
import program.entity.users.Manager;
import program.entity.users.User;

public class ProjectApplicationMenu extends MenuGroup {
    public ProjectApplicationMenu(User user) {
        super("Manage your BTO Application", 
        // Managers may not apply
        dummyVar -> !(user instanceof Manager));

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
            dummyVar -> (user.see2Rooms()) || (user.see3Rooms())
        );

        this.addMenuItem("View your applications",
            () -> user.printPastReq()
        );

        this.addMenuItem("Request application withdrawal",
            () -> Main.reqList.reqWithdrawal(user),
            dummyVar -> !(user.hasActiveApplication())  // can't withdraw if you havent applied
        );
    }
}
