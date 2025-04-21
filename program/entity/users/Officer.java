package program.entity.users;

import java.util.function.Predicate;

import program.control.Main;
import program.control.enquiry.Enquiry;
import program.control.housingApply.HousingReq;
import program.control.housingApply.HousingReq.REQUEST_STATUS;
import program.control.officerApply.AssignReq;
import program.control.officerApply.AssignReqList;
import program.control.security.Password;
import program.entity.project.Project;
import program.entity.project.Project.ROOM_TYPE;

/**
 * <p>
 * The {@code Officer} class represents an applicant who has been assigned a role as an officer.
 * This class extends the {@link Applicant} class and introduces functionalities specific to officers
 * in the system, such as the ability to manage and update flats, handle BTO applications, and view
 * and respond to enquiries for the projects they are involved with.
 * </p>
 *
 * <p>
 * An {@code Officer} is responsible for managing the flats' availability within a project, updating
 * applicants' BTO application statuses, and handling enquiries for projects they are assigned to.
 * The class provides constructors to initialize an officer with essential personal details such as NRIC,
 * name, age, marital status, and password.
 * </p>
 *
 * @see program.entity.users.Applicant
 */
public class Officer extends Applicant {

    /**
     * Constructs an {@code Officer} with the specified NRIC, name, age, marital status, and password.
     *
     * @param NRIC the NRIC of the officer
     * @param name the name of the officer
     * @param age the age of the officer
     * @param marital_status the marital status of the officer
     * @param password the password of the officer
     * @throws Exception if there is an error during the initialization of the officer
     */
    public Officer(String NRIC, String name, int age, String marital_status) throws Exception {
        super(NRIC, name, age, marital_status);
    }

    /**
     * Constructs an {@code Officer} with the specified NRIC, name, age, marital status, and password.
     *
     * @param NRIC the NRIC of the officer
     * @param name the name of the officer
     * @param age the age of the officer
     * @param marital_status the marital status of the officer
     * @param password the password of the officer
     * @throws Exception if there is an error during the initialization of the officer
     */
    public Officer(String NRIC, String name, int age, String marital_status, String password) throws Exception {
        super(NRIC, name, age, marital_status, password);
    }

    /**
     * Constructs an {@code Officer} with the specified NRIC, name, age, marital status, and password object.
     *
     * @param NRIC the NRIC of the officer
     * @param name the name of the officer
     * @param age the age of the officer
     * @param marital_status the marital status of the officer
     * @param password the password object of the officer
     * @throws Exception if there is an error during the initialization of the officer
     */
    public Officer(String NRIC, String name, int age, String marital_status, Password password) throws Exception {
        super(NRIC, name, age, marital_status, password);
    }

    /**
     * Retrieves the list of {@link AssignReq} (assignment requests) related to the officer. These
     * requests are filtered by the officer they are assigned to.
     *
     * @return a list of assignment requests associated with the officer
     */
    public AssignReqList getAssignReqList() {
        AssignReqList reqList = new AssignReqList();
        for (AssignReq req : Main.assignReqList) {
            if (req.getOfficer() == this) {
                reqList.superAdd(req);
            }
        }
        return reqList;
    }

    /**
     * Returns the current project that the officer is in charge of. If the officer is not currently
     * assigned to any open projects, this method returns {@code null}.
     *
     * @return the current {@link Project} the officer is overseeing, or {@code null} if no active project
     */
    public Project getCurProject() {
        for (Project project : Main.projectList) {
            if (project.getOfficers().contains(this) && project.nowOpen()) {
                return project;
            }
        }
        return null;
    }

    /**
     * Updates the flat availability in a specified project. This could be triggered after a successful
     * booking or cancellation of a flat. The officer can either decrement or increment the available units
     * for either 2-room or 3-room flats.
     *
     * @param project the project where the flat availability needs to be updated
     * @param flatType the type of flat (2-room or 3-room) that needs to be updated
     * @param newCount the new count of available flats for the specified type
     */
    public void updateFlat(Project project, ROOM_TYPE flatType, int newCount) {
        if (this.getCurProject() != project) {
            System.out.println("You are not assigned to this project, cannot update flats.");
            return;
        }
        if (flatType == ROOM_TYPE.room2) {
            project.setUnits2Room(newCount);
        } else {
            project.setUnits3Room(newCount);
        }
        System.out.println("Updated " + flatType + " availability to " + newCount + " for project: " + this.getCurProject().getName());
    }

    /**
     * Updates the status of an applicant's BTO application within the officer's assigned project.
     * The officer can change the status of the application from PENDING to SUCCESSFUL, or from SUCCESSFUL to BOOKED, etc.
     *
     * @param application the housing application that needs status update
     * @param newStatus the new status to set for the application
     */
    public void updateApplicantStatus(HousingReq application, REQUEST_STATUS newStatus) {
        if (this.getCurProject() != application.getProject()) {
            System.out.println("You are not authorized to update applications for this project.");
        } else {
            application.setStatus(newStatus);
            System.out.println("Application " + application.getUser() + " status changed to " + newStatus);
        }
    }

    /**
     * Provides a personalized greeting for the {@code Officer}, including details about the
     * current project they are overseeing.
     *
     * @return a string containing a greeting message with the project information
     */
    @Override
    public String getGreeting() {
        Project curProject = getCurProject();
        if (curProject == null) {
            return super.getGreeting() +
                    "\nYou are currently not handling a project";
        }
        return super.getGreeting() +
                "\nYou are currently handling project:\n" +
                this.getCurProject() +
                "\nFrom " +
                this.getCurProject().getOpenDate() +
                " until " +
                this.getCurProject().getCloseDate();
    }

    /**
     * Returns a filter predicate for viewing enquiries related to the officer. The officer can view
     * enquiries either submitted by themselves or related to the projects they are assigned to.
     *
     * @return a predicate that filters enquiries visible to the officer
     */
    @Override
    public Predicate<Enquiry> getEnquiryViewFilter() {
        return enquiry -> ((Enquiry) enquiry).getUser().equals(this) || ((Enquiry) enquiry).getProject().getOfficers().contains(this);
    }

    /**
     * Returns a filter predicate for replying to enquiries related to the officer's assigned projects.
     * The officer can respond to enquiries related to projects they are assigned to, or based on the
     * super class's reply filter.
     *
     * @return a predicate that filters enquiries for replying based on the officer's assignment
     */
    @Override
    public Predicate<Enquiry> getEnquiryReplyFilter() {
        return enquiry -> ((Enquiry) enquiry).getProject().getOfficers().contains(this) ||
                super.getEnquiryReplyFilter().test(enquiry);
    }
}