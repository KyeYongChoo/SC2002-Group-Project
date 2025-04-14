package program.boundary.menu;

import program.boundary.menuTemplate.MenuGroup;
import program.boundary.officerAssignIO.OfficerAssignPrinter;
import program.boundary.projectIO.ProjectSelector;
import program.control.Main;
import program.entity.users.Manager;
import program.entity.users.Officer;
import program.entity.users.User;

public class OfficerApplyMenu extends MenuGroup {
    public OfficerApplyMenu(User user) {
        super("Check your HDB Officer registration: ", 
            dummyVar -> user instanceof Officer && !(user instanceof Manager));

        this.addMenuItem("Check your Profile", () -> 
            System.out.println(user.getGreeting()));
        this.addMenuItem("Check status of registration: ", () -> 
            OfficerAssignPrinter.printAssignReq((Officer) user));
        this.addMenuItem("Join project as HDB Officer",() -> 
            Main.assignReqList.add((Officer) user, ProjectSelector.chooseVisibleProjectWithoutConflict((Officer) user, Main.projectList)));
    }
}
