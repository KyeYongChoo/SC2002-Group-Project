package program.control.officerApply;

import java.util.ArrayList;
import program.control.Main;
import program.control.TimeCompare;
import program.entity.project.Project;
import program.entity.users.Officer;

/**
 * <p>
 * {@code AssignReqList} is a specialized list that manages {@link AssignReq} objects,
 * representing assignment requests made by officers to join projects.
 * </p>
 *
 * <p>
 * This class extends {@link ArrayList} and overrides certain methods to provide
 * custom behaviors, such as checking for duplicate requests, validating officer
 * assignment status, and handling safe removal and addition of requests.
 * </p>
 *
 * <p>
 * The list operates closely with {@link Main#assignReqList} as its main source of data
 * and integrates with utility classes like {@link TimeCompare} to enforce assignment rules.
 * </p>
 *
 * @see AssignReq Represents a single officer assignment request.
 * @see Officer The user submitting the request.
 * @see Project The target project of the request.
 */
public class AssignReqList extends ArrayList<AssignReq> {

    /**
     * Retrieves an existing {@code AssignReq} matching the specified officer and project.
     *
     * @param officer The officer making the request.
     * @param project The project the officer is applying to.
     * @return The matching {@link AssignReq}, or {@code null} if none found.
     */
    public static AssignReq get(Officer officer, Project project) {
        for (AssignReq assignReq : Main.assignReqList) {
            if (assignReq.getOfficer() == officer && assignReq.getProject() == project) {
                return assignReq;
            }
        }
        return null;
    }

    /**
     * Attempts to add a new {@code AssignReq} to the list.
     * <p>
     * Before adding, it checks if the officer:
     * </p>
     * <ul>
     *   <li>Is not already assigned to another project.</li>
     *   <li>Has not already made a duplicate or rejected request for the same project.</li>
     * </ul>
     *
     * @param req The {@link AssignReq} to add.
     * @return {@code true} if the request was added successfully; {@code false} otherwise.
     */
    @Override
    public boolean add(AssignReq req) {
        if (!TimeCompare.officerUnassigned(req.getOfficer(), req.getProject())){
            System.out.println("Error: You have an active application at " + req.getOfficer().getCurProject());
            return false;
        }

        Officer officer = req.getOfficer();
        Project project = req.getProject();
        AssignReq duplicateReq = get(officer, project);
        if (duplicateReq != null){
            if (duplicateReq.getApplicationStatus() == AssignReq.APPLICATION_STATUS.rejected){
                System.out.println("Error: Prior request denied for project.");
                return false;
            }
            else {
                System.out.println("Error: You have already requested to join this project. Manager in charge: " + project.getManager());
                return false;
            }
        }
        Main.assignReqList.add(0,req);
        System.out.println("Requested successfully");
        return true;
    }

    /**
     * A convenience method to create and add a new {@code AssignReq} based on the given officer and project.
     *
     * @param officer The officer making the new request.
     * @param project The project the officer wishes to join.
     * @return {@code true} if the request was added successfully; {@code false} otherwise.
     */
    public boolean add(Officer officer, Project project) {
        AssignReq enq = new AssignReq(officer, project);
        return this.add(enq);
    }

    /**
     * Adds an {@code AssignReq} to the list without performing any duplicate checks on the Main AssignReqList.
     * <p>
     * Treats the list as a standalone list, not connected to Main's AssignReqList
     * This method is intended for internal use where bypassing checks is necessary,
     * such as bulk modifications where {@link java.util.ConcurrentModificationException} must be avoided.
     * </p>
     *
     * @param e The {@link AssignReq} to add.
     */
    public void superAdd(AssignReq e) {
        super.add(e);
    }

    /**
     * Removes the specified {@code AssignReq} from the list.
     * <p>
     * If the request is {@code null}, an error message will be displayed instead of attempting removal.
     * </p>
     *
     * @param req The {@link AssignReq} to remove.
     */
    public static void delete(AssignReq req) {
        if (req == null) {
            System.out.println("Error: no Enquiries found");
            return;
        }
        Main.assignReqList.remove(req);
        System.out.println("\nRemoval Successful");
    }
}
