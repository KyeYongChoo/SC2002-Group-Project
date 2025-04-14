package program.boundary.housingApplyIO;

import java.util.List;

import program.boundary.projectIO.ProjectSelect;
import program.boundary.projectIO.UserPrefSorting;
import program.control.Main;
import program.control.housingApply.HousingReq;
import program.control.housingApply.HousingReqList;
import program.control.housingApply.HousingReq.WITHDRAWAL_STATUS;
import program.entity.project.Project;
import program.entity.users.User;

public class HousingReqPrinter {

    public static void printPastApplications(User client) {
        if (client.getReqList().isEmpty()) {
            System.out.println("You have not applied for any room yet.");
            return;
        }
        
        List<HousingReq> reqList = UserPrefSorting.sortHousingReqs(client, Main.reqList);

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

    public static void printWithdrawalRequests(HousingReqList withdrawalList) {
        if (withdrawalList.isEmpty()) {
            System.out.println("No withdrawal requests available.");
            return;
        }

        System.out.println("=== Withdrawal Requests ===");
        for (int i = 0; i < withdrawalList.size(); i++) {
            HousingReq req = withdrawalList.get(i);
            System.out.printf("%d. Project: %s | Applicant: %s | Status: %s\n",
                i + 1, req.getProject().getName(), req.getUser().getName(), req.getWithdrawalStatus());
        }
    }
    /**
     * Generates a receipt for a booked application. 
     * Typically done after the applicant's status is set to BOOKED.
     */
    public static void printReceipt(HousingReq application) {
        System.out.println("=== Receipt ===");
        System.out.println("Name         : " + application.getUser().getName());
        System.out.println("NRIC         : " + application.getUser().getUserId());
        System.out.println("Age          : " + application.getUser().getAge());
        System.out.println("Marital Status: " + application.getUser().getMaritalStatus());
        System.out.println("Flat Type Booked: " + application.getRoomType());
        System.out.println("Project Name: " + application.getProject().getName());
        System.out.println("Project Location: " + application.getProject().getNeighbourhood());
    }
}