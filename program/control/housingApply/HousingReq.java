package program.control.housingApply;

import program.entity.project.Project;
import program.entity.project.Project.ROOM_TYPE;
import program.entity.users.Manager;
import program.entity.users.Officer;
import program.entity.users.User;

/**
 * <p>
 * The {@code HousingReq} class represents a housing request made by a {@link User} for a specific {@link Project}.
 * It contains details about the user, the project, the requested room type, and the status of the housing request.
 * This class also manages approval and booking information related to the housing request.
 * </p>
 *
 * <p>
 * The housing request can be in various states, such as pending, successful, unsuccessful, or booked.
 * It also includes functionality to manage the withdrawal status of the request.
 * </p>
 *
 * @see program.entity.project.Project
 * @see program.entity.users.User
 * @see program.entity.users.Manager
 * @see program.entity.users.Officer
 */
public class HousingReq {

    private User user;
    private Project project;
    private Manager approvedBy = null;
    private Officer bookedBy = null;
    private REQUEST_STATUS requestStatus = REQUEST_STATUS.pending;
    private ROOM_TYPE roomType;
    private WITHDRAWAL_STATUS withdrawalStatus = WITHDRAWAL_STATUS.notRequested;

    /**
     * Enum representing the possible statuses of a housing request.
     */
    public static enum REQUEST_STATUS {
        pending,
        successful,
        unsuccessful,
        booked
    }

    /**
     * Enum representing the possible withdrawal statuses for a housing request.
     */
    public static enum WITHDRAWAL_STATUS {
        notRequested,
        requested,
        approved,
        rejected
    }

    /**
     * Gets the withdrawal status of the housing request.
     *
     * @return the withdrawal status
     */
    public WITHDRAWAL_STATUS getWithdrawalStatus() {
        return withdrawalStatus;
    }

    /**
     * Sets the withdrawal status of the housing request.
     *
     * @param withdrawalStatus the new withdrawal status
     */
    public void setWithdrawalStatus(WITHDRAWAL_STATUS withdrawalStatus) {
        this.withdrawalStatus = withdrawalStatus;
    }

    /**
     * Gets the manager who approved the housing request.
     *
     * @return the manager who approved the request
     */
    public Manager getApprovedBy() {
        return approvedBy;
    }

    /**
     * Sets the manager who approved the housing request.
     *
     * @param manager the manager who approved the request
     */
    public void setApprovedBy(Manager manager) {
        approvedBy = manager;
    }

    /**
     * Gets the officer who booked the housing request.
     *
     * @return the officer who booked the request
     */
    public Officer getBookedBy() {
        return bookedBy;
    }

    /**
     * Sets the officer who booked the housing request.
     *
     * @param officer the officer who booked the request
     */
    public void setBookedBy(Officer officer) {
        bookedBy = officer;
    }

    /**
     * Gets the manager associated with the project of this housing request.
     *
     * @return the project manager
     */
    public Manager getManager() {
        return this.project.getManager();
    }

    /**
     * Constructs a new {@code HousingReq} with the specified client, project, and room type.
     *
     * @param client the user who is requesting the housing
     * @param project the project the housing request is for
     * @param roomType the type of room requested
     */
    public HousingReq(User client, Project project, ROOM_TYPE roomType) {
        this.user = client;
        this.project = project;
        this.roomType = roomType;
    }

    /**
     * Gets the status of the housing request.
     *
     * @return the request status
     */
    public REQUEST_STATUS getStatus() {
        return requestStatus;
    }

    /**
     * Gets the room type requested for the housing.
     *
     * @return the room type
     */
    public ROOM_TYPE getRoomType() {
        return roomType;
    }

    /**
     * Sets the room type for the housing request.
     *
     * @param roomType the new room type
     */
    public void setRoomType(ROOM_TYPE roomType) {
        this.roomType = roomType;
    }

    /**
     * Sets the status of the housing request based on a string value.
     *
     * @param status the new status of the request (e.g., "PENDING", "SUCCESSFUL")
     * @throws Exception if the provided status is not valid
     */
    public void setStatus(String status) throws Exception {
        switch (status.toUpperCase().trim()) {
            case ("PENDING"):
                setStatus(REQUEST_STATUS.pending);
                return;
            case ("SUCCESSFUL"):
                setStatus(REQUEST_STATUS.successful);
                return;
            case ("UNSUCCESSFUL"):
                setStatus(REQUEST_STATUS.unsuccessful);
                return;
            case ("BOOKED"):
                setStatus(REQUEST_STATUS.booked);
                return;
            default:
                throw new Exception("Status not found. Status received: " + status);
        }
    }

    /**
     * Sets the status of the housing request.
     *
     * @param status the new status of the request
     */
    public void setStatus(REQUEST_STATUS status) {
        this.requestStatus = status;
    }

    /**
     * Gets the user who made the housing request.
     *
     * @return the user who made the request
     */
    public User getUser() {
        return user;
    }

    /**
     * Gets the project associated with the housing request.
     *
     * @return the project
     */
    public Project getProject() {
        return project;
    }

    /**
     * Returns a string representation of the housing request, displaying details
     * about the user, project, request status, withdrawal status, and approval/booking information.
     *
     * @return a string representation of the housing request
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Housing Request Details ===\n");
        sb.append("Applicant Name   : ").append(user.getName()).append("\n");
        sb.append("Applicant NRIC   : ").append(user.getUserId()).append("\n");
        sb.append("Project Name     : ").append(project.getName()).append("\n");
        sb.append("Project Location : ").append(project.getNeighbourhood()).append("\n");
        sb.append("Flat Type        : ").append(roomType).append("\n");
        sb.append("Remaining flats  : ").append(project.getVacancy(roomType)).append("\n");
        sb.append("Request Status   : ").append(requestStatus).append("\n");
        sb.append("Withdrawal Status: ").append(withdrawalStatus).append("\n");
        if (approvedBy != null) {
            sb.append("Approved By      : ").append(approvedBy.getName()).append("\n");
        } else {
            sb.append("Approved By      : Not yet approved\n");
        }
        if (bookedBy != null) {
            sb.append("Booked By        : ").append(bookedBy.getName()).append("\n");
        } else {
            sb.append("Booked By        : Not yet booked\n");
        }
        return sb.toString();
    }
}
