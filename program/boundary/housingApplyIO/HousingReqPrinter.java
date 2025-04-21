package program.boundary.housingApplyIO;

import java.util.List;

import program.boundary.projectIO.ProjectSelect;
import program.boundary.projectIO.UserPrefSorting;
import program.control.Main;
import program.control.housingApply.HousingReq;
import program.control.housingApply.HousingReq.WITHDRAWAL_STATUS;
import program.entity.project.Project;
import program.entity.users.User;

/**
 * <p>
 * The {@code HousingReqPrinter} class is responsible for printing housing request-related
 * details for users within the application. It serves as a utility for displaying
 * information about housing applications in a user-friendly format.
 * </p>
 *
 * <p>
 * This class provides functionality to print a user's past housing applications,
 * including their status (active, withdrawn, or unsuccessful), as well as detailed
 * information about a specific housing request. By organizing and presenting this
 * information clearly, it enhances the user experience when managing housing applications.
 * </p>
 *
 * @see program.control.housingApply.HousingReq
 * @see program.entity.users.User
 */
public class HousingReqPrinter {

    /**
     * <p>
     * Prints the list of past housing applications for the specified {@link User}.
     * The applications are sorted according to user preferences, allowing for
     * a tailored view of the user's application history.
     * </p>
     *
     * <p>
     * The method displays whether each application is active, withdrawn, or unsuccessful.
     * If the user has no applications, a message is displayed indicating this.
     * </p>
     *
     * @param client the {@link User} whose applications are to be printed
     * @see program.boundary.projectIO.UserPrefSorting
     * @see program.boundary.projectIO.ProjectSelect#printVisible(User, Project)
     */
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
     * <p>
     * Prints detailed information about a given {@link HousingReq}, including
     * applicant details, project information, flat type, status, and approver
     * information. This method provides a comprehensive view of the housing
     * request, making it easier for users to understand the specifics of their
     * application.
     * </p>
     *
     * @param request the {@link HousingReq} whose details are to be printed
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