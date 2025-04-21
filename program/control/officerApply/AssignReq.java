package program.control.officerApply;

import program.boundary.console.DateTimeFormat;
import program.control.TimeCompare;
import program.entity.project.Project;
import program.entity.users.Manager;
import program.entity.users.Officer;

/**
 * <p>
 * {@code AssignReq} represents an officer's request to be assigned to a particular project.
 * It encapsulates the relationship between an {@link Officer}, the {@link Project} they wish to join,
 * and the {@link Manager} responsible for that project. Additionally, it tracks the status of the application,
 * such as whether it has been applied, accepted, or rejected.
 * </p>
 *
 * <p>
 * This class provides detailed information about the assignment request and supports easy formatting of its details
 * through a custom {@link #toString()} method. It also integrates checks on the project's active status
 * using the {@link TimeCompare} utility.
 * </p>
 *
 * @see Officer The user who is applying for the project.
 * @see Manager The user managing the project.
 * @see Project The project to which the officer is applying.
 */
public class AssignReq {

    /**
     * The officer submitting the assignment request.
     */
    private Officer officer;

    /**
     * The manager overseeing the project.
     */
    private Manager manager;

    /**
     * The project that the officer wishes to be assigned to.
     */
    private Project project;

    /**
     * The current status of the application. Defaults to {@link APPLICATION_STATUS#applied}.
     */
    private APPLICATION_STATUS applicationStatus = APPLICATION_STATUS.applied;

    /**
     * <p>
     * {@code APPLICATION_STATUS} is an enumeration representing the possible states of an assignment request:
     * </p>
     * <ul>
     *   <li>{@code applied} - The officer has submitted a request and is awaiting review.</li>
     *   <li>{@code rejected} - The request has been reviewed and denied.</li>
     *   <li>{@code accepted} - The request has been approved and the officer is assigned to the project.</li>
     * </ul>
     */
    public enum APPLICATION_STATUS {
        applied,
        rejected,
        accepted;
    }

    public void setApplicationStatus(APPLICATION_STATUS applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    /**
     * Constructs a new {@code AssignReq} instance linking an officer to a project and its manager.
     *
     * @param officer The officer applying for the project.
     * @param project The project to which the officer is applying.
     */

    public AssignReq(Officer officer, Project project){
        this.officer = officer;
        this.project = project;
        this.manager = project.getManager();
    }

    /**
     * Sets the status of this application request.
     *
     * @param applicationStatus The new status to assign.
     */
    public void setApplicationStatus(APPLICATION_STATUS applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    /**
     * Returns the current status of the assignment request.
     *
     * @return The current {@link APPLICATION_STATUS}.
     */
    public APPLICATION_STATUS getApplicationStatus() {
        return applicationStatus;
    }

    /**
     * Returns the officer associated with this assignment request.
     *
     * @return The {@link Officer} who made the request.
     */
    public Officer getOfficer() {
        return officer;
    }

    /**
     * Returns the manager responsible for the project related to this request.
     *
     * @return The {@link Manager} overseeing the project.
     */
    public Manager getManager() {
        return manager;
    }

    /**
     * Returns the project that the officer is applying to.
     *
     * @return The {@link Project} associated with the request.
     */
    public Project getProject() {
        return project;
    }

    /**
     * Provides a detailed, formatted string representation of this assignment request,
     * including officer, project, manager, current status, and active project period (if applicable).
     *
     * @return A string describing the details of the assignment request.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Assignment Request Details ===\n");
        sb.append("Officer: ").append(officer.getName()).append("\n");
        sb.append("Project: ").append(project.getName()).append("\n");
        sb.append("Manager: ").append(manager.getName()).append("\n");
        sb.append("Status: ").append(applicationStatus).append("\n");
        sb.append("Active: ");
        if (TimeCompare.currentlyActive(project)) {
            sb.append("Yes\n");
            sb.append("From: ").append(project.getOpenDate().format(DateTimeFormat.getDateFormatter())).append("\n");
            sb.append("To: ").append(project.getCloseDate().format(DateTimeFormat.getDateFormatter())).append("\n");
        } else {
            sb.append("No\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true; // Same reference
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; // Null or different class
        }
        AssignReq other = (AssignReq) obj;

        // Compare all attributes
        return officer.equals(other.officer) &&
               manager.equals(other.manager) &&
               project.equals(other.project) &&
               applicationStatus == other.applicationStatus;
    }
}
