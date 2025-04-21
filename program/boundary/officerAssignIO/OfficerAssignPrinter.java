package program.boundary.officerAssignIO;

import program.boundary.console.DateTimeFormat;
import program.control.officerApply.AssignReq;
import program.control.officerApply.AssignReqList;
import program.entity.users.Officer;

/**
 * {@code OfficerAssignPrinter} is a utility class dedicated to displaying information
 * related to officer assignment requests to the console. It provides methods to print
 * lists of requests for a specific officer, as well as detailed information about individual requests.
 *
 * <p>This class helps in presenting assignment request data in a user-friendly format,
 * both for officers viewing their own requests and for managers reviewing incoming applications.</p>
 */
public class OfficerAssignPrinter {

    /**
     * Prints a summary of all assignment requests that have been made by a specific officer.
     * If the officer has no pending or past assignment requests, a message indicating this
     * will be displayed.
     *
     * @param officer The {@link Officer} object whose assignment requests are to be printed.
     */
    public static void printAssignReq(Officer officer) {
        // Retrieve the list of assignment requests associated with the given officer.
        AssignReqList reqList = officer.getAssignReqList();
        // Check if the list of requests is empty.
        if (reqList.isEmpty()) {
            System.out.println("No assignment requests found.");
            return; // If no requests are found, exit the method.
        }

        System.out.println("=== Assignment Requests ===");
        // Iterate through the list of assignment requests.
        for (int i = 0; i < reqList.size(); i++) {
            AssignReq req = reqList.get(i);
            // For each request, print a numbered summary including the project name and the application status.
            System.out.printf("%d. Project: %s | Status: %s | Start Date: %s | End Date: %s\n",
                i + 1, req.getProject().getName(), req.getApplicationStatus(), req.getProject().getOpenDate().format(DateTimeFormat.getDateFormatter()), req.getProject().getCloseDate().format(DateTimeFormat.getDateFormatter()));
        }
    }

    /**
     * Prints detailed information about a single, specific assignment request.
     * This method relies on the {@code toString()} method of the {@link AssignReq} class
     * to provide a comprehensive representation of the request details.
     *
     * @param req The {@link AssignReq} object whose details are to be printed.
     */
    public static void printAssignReq(AssignReq req) {
        // Print the string representation of the AssignReq object.
        // It's assumed that the toString() method in AssignReq provides relevant details.
        System.out.println(req.toString());
    }

    /**
     * Prints details of a specific assignment request, but excludes the information
     * about the manager who is responsible for the project. This can be useful in
     * contexts where the officer needs the request details without the manager's specifics.
     *
     * @param req The {@link AssignReq} object whose details are to be printed (excluding manager info).
     */
    public static void printAssignReqOmitManager(AssignReq req) {
        System.out.println("=== Assignment Request Details ===");
        // Print the name of the officer who made the request.
        System.out.println("Officer: " + req.getOfficer().getName());
        // Print the name of the project for which the assignment is requested.
        System.out.println("Project: " + req.getProject().getName());
        // Print the current status of the assignment request (e.g., applied, accepted, rejected).
        System.out.println("Status: " + req.getApplicationStatus());
    }

    /**
     * Prints details of a specific assignment request, but excludes the information
     * about the officer who made the request. This can be useful for managers reviewing
     * applications and needing project and status details without the officer's specifics.
     *
     * @param req The {@link AssignReq} object whose details are to be printed (excluding officer info).
     */
    public static void printAssignReqOmitOfficer(AssignReq req) {
        System.out.println("=== Assignment Request Details ===");
        // Print the name of the project for which the assignment is requested.
        System.out.println("Project: " + req.getProject().getName());
        // Print the name of the manager who is responsible for the project.
        System.out.println("Manager: " + req.getManager().getName());
        // Print the current status of the assignment request.
        System.out.println("Status: " + req.getApplicationStatus());
    }
}