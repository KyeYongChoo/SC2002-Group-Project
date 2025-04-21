package program.boundary.officerAssignIO;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import program.boundary.console.AppScanner;
import program.control.Main;
import program.control.officerApply.AssignReq;
import program.control.officerApply.AssignReqList;
import program.entity.project.Project;

/**
 * {@code OfficerAssignSelector} provides functionalities for managers to interact with
 * officer assignment requests. It allows managers to view requests associated with
 * a specific project and to decide whether to accept or reject these applications.
 *
 * <p>This class handles the user interface logic for selecting and processing
 * officer assignment requests through the console.</p>
 */
public class OfficerAssignSelector {
    /**
     * {@code sc} is a static and final instance of the {@link Scanner} class, obtained
     * from {@link AppScanner}. It is used to read input from the console when prompting
     * the manager for choices.
     */
    private static final Scanner sc = AppScanner.getInstance();

    /**
     * Allows a manager to view all the officer assignment requests submitted for a given project
     * and select one of them for further action (accept or reject).
     *
     * @param project The {@link Project} object for which the manager wants to see the assignment requests.
     * @return The selected {@link AssignReq} object if the manager makes a valid selection,
     * or {@code null} if there are no assignment requests for the project or if the
     * manager does not make a valid choice (though the current implementation ensures a valid choice).
     */
    public static AssignReq selectToApproveReject(Project project) {
        AssignReqList reqList = new AssignReqList();
        for (AssignReq req : Main.assignReqList) {
            if (req.getProject().equals(project) && req.getApplicationStatus().equals(AssignReq.APPLICATION_STATUS.applied)) {
                reqList.superAdd(req);
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
        // Prompt the manager to enter the number corresponding to the assignment request they want to select.
        do {
            System.out.print("Enter the number of your choice: ");
            try {
                choice = Integer.parseInt(sc.nextLine()) - 1; // Read the input and adjust to be zero-based index.
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
            // Keep prompting until a valid number within the range of the list is entered.
        } while (choice < 0 || choice >= reqList.size());

        // Return the selected assignment request based on the manager's choice.
        return reqList.get(choice);
    }

    /**
     * Allows a manager to make a decision (accept or reject) on a specific officer assignment request.
     * Before prompting for a decision, it displays the details of the assignment request to the manager.
     * It also validates if there are available officer slots in the project before allowing acceptance.
     *
     * @param req The {@link AssignReq} object that the manager needs to process.
     */
    public static void selectAcceptOrReject(AssignReq req) {
        // If no assignment request was provided (e.g., if selectByProject returned null).
        if (req == null) {
            System.out.println("No assignment request selected.");
            return; // Exit the method.
        }

        System.out.println("=== Process Assignment Request ===");
        // Display the details of the assignment request to the manager.
        OfficerAssignPrinter.printAssignReq(req);

        // Check if the project has any remaining officer slots. If not, handle the situation.
        if (!validateOfficerSlots(req)) {
            return; // If validation fails (no slots and manager doesn't reject), exit.
        }

        String choice;
        // Prompt the manager to enter 'A' to accept or 'R' to reject the request.
        do {
            System.out.print("Accept or Reject this request? (A/R): ");
            choice = sc.nextLine().trim().toUpperCase(); // Read input, remove leading/trailing spaces, and convert to uppercase for case-insensitive comparison.
            // Keep prompting until the manager enters either 'A' or 'R'.
        } while (!choice.equals("A") && !choice.equals("R"));

        // Process the manager's decision.
        if (choice.equals("A")) {
            acceptRequest(req); // If 'A' was entered, call the method to accept the request.
        } else {
            rejectRequest(req); // If 'R' was entered, call the method to reject the request.
        }
    }

    /**
     * Checks if the project associated with the given assignment request has any available officer slots.
     * If no slots are available, it prompts the manager to confirm if they want to reject the request.
     *
     * @param req The {@link AssignReq} object to validate against the project's officer slots.
     * @return {@code true} if there are officer slots available in the project, or if there are no slots but the manager chooses not to reject; {@code false} if there are no slots and the manager chooses to reject (in which case the request is rejected within this method).
     */
    private static boolean validateOfficerSlots(AssignReq req) {
        // Check if the number of available officer slots in the project is zero.
        if (req.getProject().getOfficerSlots() == 0) {
            System.out.println("Sorry. No remaining officer slots.");
            String choice;
            // Prompt the manager to decide whether to reject the request due to no available slots.
            do {
                System.out.print("Reject this request? (Y/N): ");
                choice = sc.nextLine().trim().toUpperCase();
            } while (!choice.equals("Y") && !choice.equals("N"));

            // If the manager chooses to reject the request due to no slots.
            if (choice.equals("Y")) {
                rejectRequest(req); // Call the method to reject the request.
            } else {
                System.out.println("Request remains pending.");
            }
        }
        return true; // Return true if there are officer slots available.
    }

    /**
     * Accepts the given assignment request by updating its application status to 'accepted'
     * and decrementing the number of available officer slots in the associated project.
     *
     * @param req The {@link AssignReq} object to be accepted.
     */
    private static void acceptRequest(AssignReq req) {
        Project targetProject = req.getProject();
        req.setApplicationStatus(AssignReq.APPLICATION_STATUS.accepted); // Update the status of the request to accepted.
        // Decrease the number of available officer slots in the project by one.
        targetProject.setOfficerSlots(targetProject.getOfficerSlots() - 1);
        targetProject.addOfficer(req.getOfficer());
        System.out.println("Request accepted."); // Inform the manager that the request has been accepted.
    }

    /**
     * Rejects the given assignment request by updating its application status to 'rejected'.
     * It does not modify the officer slots of the project.
     *
     * @param req The {@link AssignReq} object to be rejected.
     */
    private static void rejectRequest(AssignReq req) {
        req.setApplicationStatus(AssignReq.APPLICATION_STATUS.rejected); // Update the status of the request to rejected.
    }
}