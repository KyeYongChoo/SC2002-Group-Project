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

        // Validate officer slots
        if (!validateOfficerSlots(req)) {
            return;
        }

        // Prompt manager to accept or reject the request
        String choice;
        do {
            System.out.print("Accept or Reject this request? (A/R): ");
            choice = sc.nextLine().trim().toUpperCase();
        } while (!choice.equals("A") && !choice.equals("R"));

        // Process the manager's decision
        if (choice.equals("A")) {
            acceptRequest(req);
        } else {
            rejectRequest(req);
        }
    }

    /**
     * Validates if the project has available officer slots.
     * @param req The assignment request to validate.
     * @return True if slots are available, false otherwise.
     */
    private static boolean validateOfficerSlots(AssignReq req) {
        if (req.getProject().getOfficerSlots() == 0) {
            System.out.println("Sorry. No remaining officer slots.");
            String choice;
            do {
                System.out.print("Reject this request? (Y/N): ");
                choice = sc.nextLine().trim().toUpperCase();
            } while (!choice.equals("Y") && !choice.equals("N"));

            if (choice.equals("Y")) {
                rejectRequest(req);
            } else {
                System.out.println("Request remains pending.");
            }
            return false;
        }
        return true;
    }

    /**
     * Accepts the assignment request and updates the project officer slots.
     * @param req The assignment request to accept.
     */
    private static void acceptRequest(AssignReq req) {
        req.setApplicationStatus(AssignReq.APPLICATION_STATUS.accepted);
        req.getProject().setOfficerSlots(req.getProject().getOfficerSlots() - 1);
        System.out.println("Request accepted.");
    }

    /**
     * Rejects the assignment request.
     * @param req The assignment request to reject.
     */
    private static void rejectRequest(AssignReq req) {
        req.setApplicationStatus(AssignReq.APPLICATION_STATUS.rejected);
        System.out.println("Request rejected.");
    }
}