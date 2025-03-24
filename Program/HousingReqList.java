package Program;

import Program.HousingReq.REQUEST_STATUS;
import Program.HousingReq.WITHDRAWAL_STATUS;
import Program.Project.ROOM_TYPE;
import java.util.ArrayList;
/* There's gonna be 3 types of request lists: 
* 1. As every user's attribute: Says which requests they are involved in
* 2. As every project's attribute: Says which requests they are involved in
* 3. In the Main function: Contains every request. 
*
* Frontmost element is always either active or unsuccessful application, all other applications will be unsuccessful
*/
public class HousingReqList extends ArrayList<HousingReq>{
    public HousingReq get(User client, Project project){
        for (HousingReq request : this){
            if (request.getUser() == client && request.getProject() == project){
                return request;
            }
        }
        return null;
    }

    
    @Override
    public boolean add(HousingReq req){
        // if a request has been made before for same applicant and same project
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
        HousingReq duplicateReq = MainActivity.reqList.get(client,project);
        if (duplicateReq != null){
            if (duplicateReq.getStatus() == REQUEST_STATUS.unsuccessful){
                System.out.println("Error: Prior request denied for project.");
                return false;
            }
            else {
                System.out.println("Eiyo come take a look at HousingReqList's add function there's something spooky happening here this if else case is never supposed to happen");
                return false;
            }
        }

        // Used add(index, req) syntax instead of add(req) syntax for 2 reasons
        // 1. This function overrides add(req) signature, so using that will cause infinite recursion
        // 2. Front will always be active or unsuccessful. Active requests must be at the front, if it exists
        project.getReqList().add(0,req);
        client.getReqList().add(0,req);
        MainActivity.reqList.add(0,req);
        switch (req.getRoomType()){
            case ROOM_TYPE.room2:
                project.setUnits2Room(project.getUnits2Room() - 1);
                break;
            case ROOM_TYPE.room3:
                project.setUnits3Room(project.getUnits3Room() - 1);
                break;
            default: 
                System.out.println("Eiyo come take a look at HousingReqList's add function there's something spooky happening here this if else case is never supposed to happen");
        }
        return true;
    }

    // Just in case I have to use the old add() function
    private boolean superAdd(HousingReq req){
        return super.add(req);
    }

    public static void printPastReq(User client){
        if (client.getReqList().equals(new HousingReqList())){
            System.out.println("You have not applied for any room yet.");
            return;
        }

        HousingReqList reqList = client.getReqList();
        HousingReq req;
        for(int loopCount = 0; loopCount < reqList.size(); loopCount++){
            req = reqList.get(loopCount);
            Project project = req.getProject();
            if (loopCount == 0 && !client.hasActiveApplication()){
                System.out.println("\nUnsuccessful Applications\n");
            }
            if (loopCount == 0 && client.hasActiveApplication()){
                if (req.getWithdrawalStatus() == WITHDRAWAL_STATUS.requested){
                    System.out.println("\n(Processing Withdrawal Request)");
                }
                System.out.println("Active Application\n");
            }
            if (loopCount == 1 && !client.hasActiveApplication()){
                System.out.println("\nUnsuccessful Applications\n");
            }
                
            project.printVisible(client);
        }
    }

    // In case I blur blur forget that I'm supposed to make a HousingReq object first then pass into HousingReq
    public boolean add(User client, Project project, ROOM_TYPE roomType){
        HousingReq req = new HousingReq(client, project, roomType);
        return this.add(req);
    }

    public static HousingReq activeReq(User client){
        if (client.getReqList().isEmpty()) return null; // if no prior request, return null
        HousingReq req = client.getReqList().get(0);
        if (req.getStatus() == REQUEST_STATUS.unsuccessful) return null; // if most recent request is unsuccessful, return null
        return req;
    }

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
    public static HousingReqList getWithdrawalList(Manager manager){
        return getWithdrawalList(manager,true);
    }
    
    public static HousingReqList getWithdrawalList(Manager manager, boolean unprocessedOnly){
        HousingReqList managerReqList = new HousingReqList();
        for (HousingReq req : MainActivity.reqList){

            // If not the manager then skip to next 
            if (!req.getProject().isManager(manager)){
                continue;
            }
            if (unprocessedOnly){
                if (req.getWithdrawalStatus() == WITHDRAWAL_STATUS.requested){
                    managerReqList.superAdd(req);
                }
            }
            else {
                managerReqList.superAdd(req);
            }
        }
        return managerReqList;
    }
}
