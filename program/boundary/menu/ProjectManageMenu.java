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

        // /**
        //  * Retrieves a BTOApplication for an applicant based on NRIC, within the project(s) this Officer handles.
        //  * There probably another menu etc nvm 
        //  */
        // public HousingReq retrieveApplication(String nric) {
        //     // If an Officer is assigned to a single project:
        //     if (this.getCurProject() == null) {
        //         System.out.println("Officer is not assigned to any project.");
        //         return null;
        //     }
        //     HousingReqList reqList = this.getCurProject().getReqList();
        //     // Iterate through all HousingReq in that list, searching for the matching NRIC
        //     for (HousingReq req : reqList) {
        //         User reqUser = req.getUser();
        //         if (reqUser != null && reqUser.getUserId().equalsIgnoreCase(nric)) {
        //             return req;  // Found the matching application
        //         }
        //     }
        //     System.out.println("No application found for NRIC: " + nric + " in project " + this.getCurProject().getName());
        //     return null;
        // }
        this.addMenuItem(
            new MenuGroup("Check your HDB Officer registration: ", dummyVar -> !(user instanceof Manager)) // Managers are not HDB Officers
            .addMenuItem("Check your Profile", () -> System.out.println(user.getGreeting()))
            //.addMenuItem("Check status of registration: ", ()-> )
        );
    }
    
}
