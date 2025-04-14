package program.boundary.officerAssignIO;

import program.control.officerApply.AssignReq;
import program.control.officerApply.AssignReqList;
import program.entity.users.Officer;

public class OfficerAssignPrinter {

    /**
     * Prints all assignment requests for a specific officer.
     * @param officer The officer whose assignment requests are to be printed.
     */
    public static void printAssignReq(Officer officer) {
        AssignReqList reqList = officer.getAssignReqList();
        if (reqList.isEmpty()) {
            System.out.println("No assignment requests found.");
            return;
        }

        System.out.println("=== Assignment Requests ===");
        for (int i = 0; i < reqList.size(); i++) {
            AssignReq req = reqList.get(i);
            System.out.printf("%d. Project: %s | Status: %s\n",
                i + 1, req.getProject().getName(), req.getApplicationStatus());
        }
    }

    /**
     * Prints details of a specific assignment request.
     * @param req The assignment request to print.
     */
    public static void printAssignReq(AssignReq req) {
        System.out.println("=== Assignment Request Details ===");
        System.out.println("Officer: " + req.getOfficer().getName());
        System.out.println("Project: " + req.getProject().getName());
        System.out.println("Manager: " + req.getManager().getName());
        System.out.println("Status: " + req.getApplicationStatus());
    }

    /**
     * Prints assignment request details, omitting the manager's information.
     * @param req The assignment request to print.
     */
    public static void printAssignReqOmitManager(AssignReq req) {
        System.out.println("=== Assignment Request Details ===");
        System.out.println("Officer: " + req.getOfficer().getName());
        System.out.println("Project: " + req.getProject().getName());
        System.out.println("Status: " + req.getApplicationStatus());
    }

    /**
     * Prints assignment request details, omitting the officer's information.
     * @param req The assignment request to print.
     */
    public static void printAssignReqOmitOfficer(AssignReq req) {
        System.out.println("=== Assignment Request Details ===");
        System.out.println("Project: " + req.getProject().getName());
        System.out.println("Manager: " + req.getManager().getName());
        System.out.println("Status: " + req.getApplicationStatus());
    }
}