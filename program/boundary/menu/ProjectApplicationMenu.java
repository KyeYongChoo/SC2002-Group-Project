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

public class ProjectApplicationMenu extends MenuGroup {
    public ProjectApplicationMenu(User user) {
        super("Manage your BTO Application", 
        // Managers may not apply
         user_ ->!(user_ instanceof Manager));

        this.addMenuItem("View projects", () -> {
            ProjectSelect.printVisible(user);
        });

        this.addMenuItem("Apply to a project", 
            () -> {
                Project project = ProjectSelector.chooseProjectsApplyAsApplicant(user, Main.projectList);
                if (project == null) return;
                ROOM_TYPE targetRoomType = RoomTypeSelector.selectRoomType(user, project);

                // Add the application to the request list
                Main.reqList.add(user, project, targetRoomType);
                System.out.println("Application submitted successfully.");
            }, 
            // If the User is not a manager and satisfies one of the BTO requirements, they can apply for projects
             user_ ->(user_.see2Rooms()) || (user_.see3Rooms())
        );

        this.addMenuItem("View your applications",
            () -> HousingReqList.printPast(user)
        );

        this.addMenuItem("Request application withdrawal",
            () -> Main.reqList.reqWithdrawal(user),
             user_ ->!(user_.hasActiveApplication())  // can't withdraw if you havent applied
        );
    }
}
