package program.boundary.menu;

import program.boundary.menuTemplate.MenuGroup;
import program.boundary.officerAssignIO.OfficerAssignSelector;
import program.control.officerApply.AssignReq;
import program.entity.users.Manager;
import program.entity.users.Officer;
import program.entity.users.User;

public class ProjectManageMenu extends MenuGroup {
    public ProjectManageMenu(User user) {
        super("Manage your Projects", 
            // only officers and above may manage projects
            dummyVar -> user instanceof Officer);

        this.addMenuItem("Approve incoming Officer requests", () -> {
            Manager manager = (Manager) user;
            AssignReq req = OfficerAssignSelector.selectByProject(manager.getCurProject());
            OfficerAssignSelector.selectAcceptOrReject(req);
            // Manager and above
        }, dummyVar -> user instanceof Manager);

        this.addMenuItem("Approve HDB Applications", () -> {
            
        }, dummyVar -> user instanceof Manager);
        
        this.addMenuItem("Approve HDB Applications", () -> {

        });
        // // template
        // this.addMenuItem("", () -> {
        // });

    }
    
}
