package program.boundary.menu;

import program.boundary.menuTemplate.MenuGroup;
import program.boundary.officerAssignIO.OfficerAssignPrinter;
import program.boundary.projectIO.ProjectSelector;
import program.control.Main;
import program.entity.project.Project;
import program.entity.users.Manager;
import program.entity.users.Officer;
import program.entity.users.User;

public class OfficerApplyMenu extends MenuGroup {
    public OfficerApplyMenu(User user) {
        // Menu title and condition: Only accessible to Officers who are not Managers
        super("Manage your HDB Officer applications ", 
            user_ -> user_ instanceof Officer && !(user instanceof Manager));

        // Option 1: Check the officer's profile
        this.addMenuItem("Check your Profile", () -> 
            System.out.println(user.getGreeting())); // Prints a greeting or profile information for the user

        // Option 2: Check the status of the officer's registration
        this.addMenuItem("Check status of registration ", () -> 
            OfficerAssignPrinter.printAssignReq((Officer) user)); // Prints all assignment requests for the officer

        // Option 3: Join a project as an HDB Officer
        this.addMenuItem("Join project as HDB Officer", () -> {
            Project targetProject = ProjectSelector.chooseProjectsApplyAsOfficer((Officer) user,Main.projectList);
            if (targetProject == null) return;
            Main.assignReqList.add((Officer) user, targetProject);
        });
    }
}
