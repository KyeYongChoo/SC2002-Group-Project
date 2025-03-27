package program;

import program.Project.ROOM_TYPE;

/*
 * Named HousingRequest because naming it Application would be too confusing like apply for house application or software application lmao
 */
public class HousingReq {
    private User user;
    private Project project;
    private Officer approvedBy = null;
    private REQUEST_STATUS requestStatus = REQUEST_STATUS.pending;
    private ROOM_TYPE roomType; 
    private WITHDRAWAL_STATUS withdrawalStatus = WITHDRAWAL_STATUS.notRequested;

    public WITHDRAWAL_STATUS getWithdrawalStatus(){
        return withdrawalStatus;
    }
    public void setWithdrawalStatus(WITHDRAWAL_STATUS withdrawalStatus){
        this.withdrawalStatus = withdrawalStatus;
    }

    public static enum WITHDRAWAL_STATUS{
        notRequested,
        requested,
        approved,
    }

    public Manager getManager(){
        return this.project.getManager();
    }

    public static enum REQUEST_STATUS{
        pending,
        successful,
        unsuccessful,
        booked
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
}
