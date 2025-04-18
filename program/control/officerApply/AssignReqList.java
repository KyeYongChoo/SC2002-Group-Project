package program.control.officerApply;

import java.util.ArrayList;
import program.control.Main;
import program.control.TimeCompare;
import program.entity.project.Project;
import program.entity.users.Officer;

public class AssignReqList extends ArrayList<AssignReq>{

    public static AssignReq get(Officer officer, Project project){
        for (AssignReq assignReq : Main.assignReqList){
            if (assignReq.getOfficer() == officer && assignReq.getProject() == project){
                return assignReq;
            }
        }
        return null;
    }

    @Override
    public boolean add(AssignReq req){
        // if a request has been made before for same applicant and same project
        if (!TimeCompare.officerUnassigned(req.getOfficer(), req.getProject())){
            System.out.println("Error: You have an active application at " + req.getOfficer().getCurProject());
            // DEBUG:
            // System.out.println(req.getOfficer() + " application to " + req.getProject());
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
        // DEBUG:
        // System.out.println(officer + " application to " + project);
        return true;
    }

    public boolean add(Officer officer, Project project){
        AssignReq enq = new AssignReq(officer, project);
        return this.add(enq);
    }

    // Used if need to modify in place ie avoid java.util.ConcurrentModificationException
    // Does not add a copy if it is already present 
    public boolean superAdd(AssignReq req){
        if (this.stream().anyMatch(req_ -> ((AssignReq) req_).equals(req))) return false;
        return super.add(req);
    }

    public static void delete(AssignReq req){
        if (req == null) {
            System.out.println("Error: no Enquiries found");
            return;
        }
        Main.assignReqList.remove(req);
        System.out.println("\nRemoval Successful");
    }
}
