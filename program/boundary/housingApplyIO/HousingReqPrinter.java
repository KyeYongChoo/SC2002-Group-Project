package program.boundary.housingApplyIO;

import java.util.List;

import program.boundary.projectIO.ProjectSelect;
import program.boundary.projectIO.UserPrefSorting;
import program.control.Main;
import program.control.housingApply.HousingReq;
import program.control.housingApply.HousingReq.WITHDRAWAL_STATUS;
import program.entity.project.Project;
import program.entity.users.User;

public class HousingReqPrinter {

    public static void printPastApplications(User client) {
        if (client.getReqList().isEmpty()) {
            System.out.println("You have not applied for any room yet.");
            return;
        }
        
        List<HousingReq> reqList = UserPrefSorting.sortHousingReqs(client, Main.housingReqList);

        for (int i = 0; i < reqList.size(); i++) {
            HousingReq req = reqList.get(i);
            Project project = req.getProject();

            if (i == 0 && !client.hasActiveApplication()) {
                System.out.println("\nUnsuccessful Applications\n");
            }
            if (i == 0 && client.hasActiveApplication()) {
                if (req.getWithdrawalStatus() == WITHDRAWAL_STATUS.requested) {
                    System.out.println("\n(Processing Withdrawal Request)");
                }
                System.out.println("Active Application\n");
            }
            if (i == 1 && !client.hasActiveApplication()) {
                System.out.println("\nUnsuccessful Applications\n");
            }

            ProjectSelect.printVisible(client, project);
        }
    }

    /**
     * Prints the details of a given HousingReq.
     */
    public static void printHousingRequest(HousingReq request) {
        System.out.println("=== Housing Request Details ===");
        System.out.println("Applicant Name   : " + request.getUser().getName());
        System.out.println("Applicant NRIC   : " + request.getUser().getUserId());
        System.out.println("Project Name     : " + request.getProject().getName());
        System.out.println("Project Location : " + request.getProject().getNeighbourhood());
        System.out.println("Flat Type        : " + request.getRoomType());
        System.out.println("Request Status   : " + request.getStatus());
        System.out.println("Withdrawal Status: " + request.getWithdrawalStatus());
        if (request.getApprovedBy() != null) {
            System.out.println("Approved By      : " + request.getApprovedBy().getName());
        } else {
            System.out.println("Approved By      : Not yet approved");
        }
    }
}