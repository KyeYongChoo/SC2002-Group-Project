package program.boundary.menu;

import program.boundary.menuTemplate.MenuGroup;
import program.boundary.officerAssignIO.OfficerAssignSelector;
import program.boundary.projectIO.SetUpProject;
import program.control.Main;
import program.control.officerApply.AssignReq;
import program.entity.project.Project;
import program.entity.users.Manager;
import program.entity.users.Officer;
import program.entity.users.User;

import java.util.Scanner;

public class ProjectManageMenu extends MenuGroup {
    public ProjectManageMenu(User user) {
        super("Manage your Projects", 
            // only officers and above may manage projects
            user_ -> user_ instanceof Officer);

        this.addMenuItem("View/Approve incoming Officer requests", () -> {
            Manager manager = (Manager) user;
            AssignReq req = OfficerAssignSelector.selectByProject(manager.getCurProject());
            if (req == null) return;
            if (req.getApplicationStatus() != AssignReq.APPLICATION_STATUS.applied){
                System.out.println("Request has already been accepted/rejected. ");
                return;
            }
            OfficerAssignSelector.selectAcceptOrReject(req);
            // Manager and above
        }, user_ -> user_ instanceof Manager);

        this.addMenuItem("Approve HDB Applications", () -> {
            
        }, user_ -> user_ instanceof Manager);
        
        this.addMenuItem("Process withdrawal Requests", () -> {

        });

        this.addMenuItem("Promote Officer to Manager", () -> {
        });

        this.addMenuItem("Help applicant book", () -> {
            // generate receipt by default the first time u book 
        });

        this.addMenuItem("Generate Receipt of Applicant Booking", () -> {
        });
        this.addMenuItem(new MenuGroup("Manager BTO Project Listings", user_ -> user_ instanceof Manager)
            .addMenuItem("Create Project Listing", new SetUpProject(user))
            
            .addMenuItem("Edit Project Listing", () -> {
                
            }, user_ -> ((Manager) user_).getCurProject() != null)

            .addMenuItem("Delete Project Listing", () -> {
                
            }, user_ -> ((Manager) user_).getCurProject() != null)

            .addMenuItem("Edit Project Listing", () -> {
                
            }, user_ -> ((Manager) user_).getCurProject() != null)

            .addMenuItem("Toggle Visibility of project listing", () -> {
                
            }, user_ -> ((Manager) user_).getCurProject() != null)
        );
        // template
        // this.addMenuItem("", () -> {
        // });

    }
    
}
