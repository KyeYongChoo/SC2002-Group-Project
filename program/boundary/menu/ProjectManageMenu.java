package program.boundary.menu;

import program.boundary.menuTemplate.MenuGroup;
import program.boundary.projectIO.ProjectSelector;
import program.control.Main;
import program.entity.users.Manager;
import program.entity.users.Officer;
import program.entity.users.User;

public class ProjectManageMenu extends MenuGroup {
    public ProjectManageMenu(User user) {
        super("Manage your Projects", 
            // only officers and above may manage projects
            dummyVar -> user instanceof Officer);

        this.addMenuItem("Join project as HDB Officer",
            () -> 
                Main.assignReqList.add((Officer) user,ProjectSelector.chooseProjectForOfficer((Officer) user, Main.projectList))
            ,
            dummyVar -> !(user instanceof Manager) // managers may not join HDB Projects
        );

        this.addMenuItem(
            new MenuGroup("Check your HDB Officer registration: ", dummyVar -> !(user instanceof Manager)) // Managers are not HDB Officers
            .addMenuItem("Check your Profile", () -> System.out.println(user.getGreeting()))
            //.addMenuItem("Check status of registration: ", ()-> )
        );
    }
    
}
