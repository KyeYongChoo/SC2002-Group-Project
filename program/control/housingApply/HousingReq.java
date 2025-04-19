package program.control.housingApply;

import program.entity.project.Project;
import program.entity.project.Project.ROOM_TYPE;
import program.entity.users.Manager;
import program.entity.users.Officer;
import program.entity.users.User;

/*
 * Named HousingRequest because naming it Application would be too confusing like apply for house application or software application lmao
 */
public class HousingReq {
    private User user;
    private Project project;
    private Manager approvedBy = null;
    private Officer bookedBy = null;
    private REQUEST_STATUS requestStatus = REQUEST_STATUS.pending;
    private ROOM_TYPE roomType; 
    private WITHDRAWAL_STATUS withdrawalStatus = WITHDRAWAL_STATUS.notRequested;

    public static enum REQUEST_STATUS{
        pending,
        successful,
        unsuccessful,
        booked
    }

    public static enum WITHDRAWAL_STATUS{
        notRequested,
        requested,
        approved,
        rejected
    }

    public WITHDRAWAL_STATUS getWithdrawalStatus(){
        return withdrawalStatus;
    }
    public void setWithdrawalStatus(WITHDRAWAL_STATUS withdrawalStatus){
        this.withdrawalStatus = withdrawalStatus;
    }

    public Manager getApprovedBy(){
        return approvedBy;
    }

    public void setApprovedBy(Manager manager){
        approvedBy = manager;
    }

    public Officer getBookedBy(){
        return bookedBy;
    }

    public void setBookedBy(Officer officer){
        bookedBy = officer;
    }

    public Manager getManager(){
        return this.project.getManager();
    }
    
    public HousingReq(User client, Project project, ROOM_TYPE roomType){
        this.user = client;
        this.project = project;
        this.roomType = roomType;
    }

    public REQUEST_STATUS getStatus(){
        return requestStatus;
    }

    public ROOM_TYPE getRoomType(){
        return roomType;
    }

    public void setRoomType(ROOM_TYPE roomType){
        this.roomType = roomType;
    }

    public void setStatus(String status) throws Exception{
        switch (status.toUpperCase().trim()){
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
        }
        throw new Exception("Status not found. Status received: " + status);
    }

    public void setStatus(REQUEST_STATUS status){
        this.requestStatus = status;
    }

    public User getUser(){
        return user;
    }

    public Project getProject(){
        return project;
    }

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
            sb.append("Booked By      : ").append(bookedBy.getName()).append("\n");
        } else {
            sb.append("Booked By      : Not yet booked\n");
        }
        return sb.toString();
    }
}
