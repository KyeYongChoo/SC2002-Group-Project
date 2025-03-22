package Program;

import Program.Project.ROOM_TYPE;
import java.util.ArrayList;

/* There's gonna be 3 types of request lists: 
* 1. As every user's attribute: Says which requests they are involved in
* 2. As every project's attribute: Says which requests they are involved in
* 3. In the Main function: Contains every request. 
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
        }
        User client = req.getUser();
        Project project = req.getProject();
        HousingReq duplicateReq = MainActivity.reqList.get(client,project);
        if (duplicateReq != null){
            if (duplicateReq.getStatus() == HousingReq.REQUEST_STATUS.unsuccessful){
                System.out.println("Error: Prior request denied for project.");
                return false;
            }
            else {
                System.out.println("Eiyo come take a look at HousingReqList's add function there's something spooky happening here this if else case is never supposed to happen");
                return false;
            }
        }

        boolean successfulAdd = project.getReqList().superAdd(req)
                                && client.getReqList().superAdd(req)
                                && MainActivity.reqList.superAdd(req);
        if (successfulAdd){
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
        }
        return successfulAdd;
    }

    // Used to avoid infinite recursion in the add() method 
    private boolean superAdd(HousingReq req){
        return super.add(req);
    }

    // In case I blur blur forget that I'm supposed to make a HousingReq object first then pass into HousingReq
    public boolean add(User client, Project project, ROOM_TYPE roomType){
        HousingReq req = new HousingReq(client, project, roomType);
        return this.add(req);
    }
}
