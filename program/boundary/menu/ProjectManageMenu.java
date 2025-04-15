package program.boundary.menu;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import program.boundary.housingApplyIO.HousingReqPrinter;
import program.boundary.housingApplyIO.HousingReqSelector;
import program.boundary.menuTemplate.MenuGroup;
import program.boundary.menuTemplate.MenuNavigator;
import program.boundary.menuTemplate.SelectionMenu;
import program.boundary.officerAssignIO.OfficerAssignSelector;
import program.boundary.projectIO.SetUpProject;
import program.control.Main;
import program.control.housingApply.HousingReq;
import program.control.officerApply.AssignReq;
import program.entity.users.Manager;
import program.entity.users.Officer;
import program.entity.users.User;

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
        
        this.addMenuItem(new SelectionMenu<>(
            "Approve HDB Applications", 
            user_ -> user_ instanceof Manager,
            Main.reqList.stream()
                .filter(project -> project.getManager().equals(user))
                .filter(project -> project.getStatus() == HousingReq.REQUEST_STATUS.pending)
                .collect(Collectors.toList()), 
            HousingReq::toString, 
            req -> {
                        boolean hasVacancy = req.getProject().getVacancy(req.getRoomType()) > 0;
                
                        String menuName = hasVacancy
                            ? "Please choose to accept or reject this application"
                            : "Sorry, not enough vacancy to accept application";
                        
                        List<String> choices = hasVacancy
                            ? List.of("Accept", "Reject")
                            : List.of("Reject");

                        MenuNavigator.getInstance().pushMenu(new SelectionMenu<>(
                            menuName,
                            choices,
                            String::toString,
                            strChoice -> {
                                if (strChoice.equals("Accept")){
                                    req.getProject().decrementRoomType(req.getRoomType());
                                    req.setApprovedBy((Manager) user);
                                    req.setStatus(HousingReq.REQUEST_STATUS.successful);
                                }else if (strChoice.equals("Reject")){
                                    req.setApprovedBy((Manager) user);
                                    req.setStatus(HousingReq.REQUEST_STATUS.unsuccessful);
                                }
                            }

                    ));
            }
        ));

        this.addMenuItem("Process withdrawal Requests", () -> {
            
        }, user_ -> user_ instanceof Manager);

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
