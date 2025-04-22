package program.control.housingApply;

import program.boundary.projectIO.ProjectSelect;
import program.control.Main;
import program.control.housingApply.HousingReq.REQUEST_STATUS;
import program.control.housingApply.HousingReq.WITHDRAWAL_STATUS;
import program.entity.project.Project;
import program.entity.project.Project.ROOM_TYPE;
import program.entity.users.Manager;
import program.entity.users.User;

import java.util.ArrayList;

/**
 * <p>
 * {@code HousingReqList} represents a list of {@link HousingReq} objects related to housing applications made by users.
 * This list manages the relationship between users, housing projects, and application statuses.
 * </p>
 *
 * <p>
 * A {@code HousingReqList} can be found in three contexts:
 * <ul>
 *     <li>As part of each {@link User}, tracking their personal housing requests.</li>
 *     <li>As part of each {@link Project}, tracking all requests for that project.</li>
 *     <li>In {@link Main}, representing the master list of all housing requests in the system.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Important behavior:
 * <ul>
 *     <li>The first request in a {@code HousingReqList} is always either the active request or the most recent unsuccessful one.</li>
 *     <li>All other requests are considered historical (unsuccessful).</li>
 * </ul>
 * </p>
 *
 * @see HousingReq A single housing request.
 * @see Main The central control class managing system-wide data.
 */
public class HousingReqList extends ArrayList<HousingReq> {

    /**
     * <p>
     * Retrieves the {@link HousingReq} for a given {@link User} and {@link Project}.
     * </p>
     *
     * @param client The user who submitted the request.
     * @param project The project for which the request was made.
     * @return The matching {@code HousingReq}, or {@code null} if no match is found.
     */
    public HousingReq get(User client, Project project){
        for (HousingReq request : this){
            if (request.getUser() == client && request.getProject() == project){
                return request;
            }
        }
        return null;
    }

    /**
     * <p>
     * Adds a {@link HousingReq} to the list. If the request is valid, it is inserted at the front of all relevant lists:
     * the global housing request list, the user's request list, and the project's request list.
     * </p>
     *
     * <p>
     * Validation includes ensuring:
     * <ul>
     *     <li>The user does not already have an active request.</li>
     *     <li>No duplicate active request exists for the same user and project.</li>
     * </ul>
     * </p>
     *
     * @param req The housing request to be added.
     * @return {@code true} if the request was successfully added; {@code false} otherwise.
     */
    @Override
    public boolean add(HousingReq req){
        if (req.getUser().hasActiveApplication()){
            System.out.println("Error: You have an active application");
            HousingReq duplicateReq = HousingReqList.activeReq(req.getUser());
            if (duplicateReq.getWithdrawalStatus() == WITHDRAWAL_STATUS.requested){
                System.out.println("Your withdrawal is being processed by " + duplicateReq.getManager());
            }
            return false;
        }
        User client = req.getUser();
        Project project = req.getProject();
        HousingReq duplicateReq = Main.housingReqList.get(client, project);
        if (duplicateReq != null){
            if (duplicateReq.getStatus() == REQUEST_STATUS.unsuccessful){
                System.out.println("Error: Prior request denied for project.");
                return false;
            } else {
                System.out.println("Warning: Unexpected duplicate active request detected.");
                return false;
            }
        }

        project.getReqList().add(0, req);
        client.getReqList().add(0, req);
        Main.housingReqList.add(0, req);
        return true;
    }

    /**
     * <p>
     * Directly adds a {@link HousingReq} to this list by calling the original {@code super.add()} method,
     * bypassing the overridden {@link #add(HousingReq)} logic.
     * </p>
     *
     * @param req The housing request to be added.
     * @return {@code true} if the request was added.
     */
    private boolean superAdd(HousingReq req){
        return super.add(req);
    }

    /**
     * <p>
     * Prints all past housing applications of a {@link User}, showing active and unsuccessful requests separately.
     * </p>
     *
     * @param client The user whose request history is to be printed.
     */
    public static void printPast(User client){
        if (client.getReqList().equals(new HousingReqList())){
            System.out.println("You have not applied for any HDB yet.");
            return;
        }

        HousingReqList reqList = client.getReqList();
        HousingReq req;
        for(int loopCount = 0; loopCount < reqList.size(); loopCount++){
            req = reqList.get(loopCount);
            Project project = req.getProject();
            if (loopCount == 0 && !client.hasActiveApplication()){
                System.out.println("\nUnsuccessful Applications\n");
                System.out.println("Application Status: " + req.getStatus());
            }
            if (loopCount == 0 && client.hasActiveApplication()){
                if (req.getWithdrawalStatus() == WITHDRAWAL_STATUS.requested){
                    System.out.println("\n(Processing Withdrawal Request)");
                    System.out.println("Application Status: " + req.getStatus());
                }
                System.out.println("Active Application\n");
                System.out.println("Application Status: " + req.getStatus());
            }
            if (loopCount == 1 && !client.hasActiveApplication()){
                System.out.println("\nUnsuccessful Applications\n");
                System.out.println("Application Status: " + req.getStatus());
            }
            ProjectSelect.printVisible(client, project);
        }
    }

    /*
     * @param client The user submitting the application.
     * @param project The project being applied to.
     * @param roomType The desired room type.
     * @return {@code true} if the request was successfully created and added.
     */
    public boolean add(User client, Project project, ROOM_TYPE roomType){
        HousingReq req = new HousingReq(client, project, roomType);
        return this.add(req);
    }

    /**
     * <p>
     * Retrieves the active {@link HousingReq} for a {@link User}.
     * </p>
     *
     * @param client The user whose active request is to be found.
     * @return The active {@code HousingReq}, or {@code null} if none exists.
     */
    public static HousingReq activeReq(User client){
        if (client.getReqList().isEmpty()) return null;
        HousingReq req = client.getReqList().get(0);
        if (req.getStatus() == REQUEST_STATUS.unsuccessful) return null;
        return req;
    }

    /**
     * <p>
     * Requests withdrawal of the active housing application for a {@link User}.
     * </p>
     *
     * @param client The user requesting withdrawal.
     */
    public void reqWithdrawal(User client){
        if (!client.hasActiveApplication()){
            System.out.println("Sorry, no active applications");
            return;
        }
        HousingReq req = activeReq(client);
        if (req.getWithdrawalStatus() == WITHDRAWAL_STATUS.requested){
            System.out.println("Sorry, your withdrawal request for " + req.getProject() + " is being processed.");
            return;
        }
        req.setWithdrawalStatus(WITHDRAWAL_STATUS.requested);
        System.out.println("Withdrawal successfully requested from Manager " + req.getManager());
    }

    /**
     * <p>
     * Retrieves all withdrawal requests assigned to a specific {@link Manager}.
     * </p>
     *
     * @param manager The manager responsible for handling the withdrawal requests.
     * @return A {@code HousingReqList} containing all pending withdrawal requests.
     */
    public static HousingReqList getWithdrawalList(Manager manager){
        return getWithdrawalList(manager, true);
    }

    /**
     * <p>
     * Retrieves withdrawal requests for a {@link Manager}, optionally filtering only unprocessed requests.
     * </p>
     *
     * @param manager The manager responsible for the projects.
     * @param unprocessedOnly {@code true} to retrieve only pending withdrawal requests; {@code false} to retrieve all.
     * @return A {@code HousingReqList} matching the criteria.
     */
    public static HousingReqList getWithdrawalList(Manager manager, boolean unprocessedOnly){
        HousingReqList managerReqList = new HousingReqList();
        for (HousingReq req : Main.housingReqList){
            if (!req.getProject().isManager(manager)){
                continue;
            }
            if (unprocessedOnly){
                if (req.getWithdrawalStatus() == WITHDRAWAL_STATUS.requested){
                    managerReqList.superAdd(req);
                }
            } else {
                managerReqList.superAdd(req);
            }
        }
        return managerReqList;
    }
}
