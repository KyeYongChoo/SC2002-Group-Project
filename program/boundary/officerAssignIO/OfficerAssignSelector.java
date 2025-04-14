package program.boundary.officerAssignIO;

import java.util.Scanner;

import program.boundary.console.AppScanner;
import program.control.Main;
import program.control.officerApply.AssignReq;
import program.control.officerApply.AssignReqList;
import program.entity.project.Project;

public class OfficerAssignSelector {
    private static final Scanner sc = AppScanner.getInstance();

    /**
     * Allows a manager to select an assignment request for a specific project.
     * @param project The project for which assignment requests are to be selected.
     * @return The selected assignment request, or null if no valid selection is made.
     */
    public static AssignReq selectByProject(Project project) {
        AssignReqList reqList = new AssignReqList();
        for (AssignReq req : Main.assignReqList) {
            if (req.getProject().equals(project)) {
                reqList.add(req);
            }
        }

        if (reqList.isEmpty()) {
            System.out.println("No assignment requests found for this project.");
            return null;
        }

        System.out.println("=== Assignment Requests for Project: " + project.getName() + " ===");
        for (int i = 0; i < reqList.size(); i++) {
            AssignReq req = reqList.get(i);
            System.out.printf("%d. Officer: %s | Status: %s\n",
                i + 1, req.getOfficer().getName(), req.getApplicationStatus());
        }

        int choice = -1;
        do {
            System.out.print("Enter the number of your choice: ");
            try {
                choice = Integer.parseInt(sc.nextLine()) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        } while (choice < 0 || choice >= reqList.size());

        return reqList.get(choice);
    }

    /**
     * Allows a manager to accept or reject an assignment request.
     * @param req The assignment request to process.
     */
    public static void selectAcceptOrReject(AssignReq req) {
        if (req == null) {
            System.out.println("No assignment request selected.");
            return;
        }

        System.out.println("=== Process Assignment Request ===");
        OfficerAssignPrinter.printAssignReq(req);

        String choice;
        do {
            System.out.print("Accept or Reject this request? (A/R): ");
            choice = sc.nextLine().trim().toUpperCase();
        } while (!choice.equals("A") && !choice.equals("R"));

        if (choice.equals("A")) {
            req.setApplicationStatus(AssignReq.APPLICATION_STATUS.accepted);
            req.getProject().setOfficerSlots(req.getProject().getOfficerSlots() -1);
            System.out.println("Request accepted.");
        } else {
            req.setApplicationStatus(AssignReq.APPLICATION_STATUS.rejected);
            System.out.println("Request rejected.");
        }
    }
}