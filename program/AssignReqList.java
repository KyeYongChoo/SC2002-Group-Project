package program;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import program.Project.ROOM_TYPE;
import com.sun.net.httpserver.Request;

public class AssignReqList extends ArrayList<AssignReq>{
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

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
        if (req.getOfficer().assigned()){
            System.out.println("Error: You have an active application at " + req.getOfficer().getProject());
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
        return true;
    }

    public static void printPastReq(Officer officer){
        for (AssignReq req : officer.getAssignReqList()){
            System.out.println("Project: " + req.getProject());
            System.out.println("Application Status: " + req.getApplicationStatus());
            System.out.println("Manager in charge: " + req.getManager());

        }
    }

    public boolean add(Officer officer, Project project){
        AssignReq enq = new AssignReq(officer, project);
        return this.add(enq);
    }

    // Used if need to modify in place ie avoid java.util.ConcurrentModificationException
    public void superAdd(AssignReq e){
        super.add(e);
    }

    public static void delete(AssignReq req){
        if (req == null) {
            System.out.println("Error: no Enquiries found");
            return;
        }
        Main.enquiryList.remove(req);
        System.out.println("\nRemoval Successful");
    }
}
