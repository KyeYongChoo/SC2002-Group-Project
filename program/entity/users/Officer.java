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

public class Officer extends Applicant {
    public Officer(String NRIC, String name, int age, String marital_status) throws Exception{
        super(NRIC, name, age, marital_status);
    }
    public Officer(String NRIC, String name, int age, String marital_status, String password) throws Exception{
        super(NRIC, name, age, marital_status, password);
    }
    public Officer(String NRIC, String name, int age, String marital_status, Password password) throws Exception{
        super(NRIC, name, age, marital_status, password);
    }

    public AssignReqList getAssignReqList(){
        AssignReqList reqList = new AssignReqList();
        for (AssignReq req : Main.assignReqList){
            if (req.getOfficer() == this){
                reqList.superAdd(req);
            }
        }
        return reqList;
    }

    /*
     * returns current project the officer is in charge of
     * Take note officer may be approved for more objects in the future
     * If not sure, use Project's conflictInterest or manually filter through Project
     */
    public Project getCurProject(){
        for (Project project : Main.projectList){
            if (project.getOfficers().contains(this) && project.nowOpen()){
                return project;
            }
        }
        return null;
    }

    /**
     * Updates the flat availability in the given project. 
     * For example, decrement/increment available units after a successful booking or cancellation.
     */
    public void updateFlat(Project project, ROOM_TYPE flatType, int newCount) 
    {
        if (this.getCurProject()!=project) {
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
     * Updates the status of a specific applicant’s BTO application. 
     * E.g., from PENDING to SUCCESSFUL, or SUCCESSFUL to BOOKED, etc.
     */
    public void updateApplicantStatus(HousingReq application, REQUEST_STATUS newStatus) {
        if (this.getCurProject()!=application.getProject()) {
            System.out.println("You are not authorized to update applications for this project.");
        }
        else{
            application.setStatus(newStatus);
            System.out.println("Application " + application.getUser() + " status changed to " + newStatus);
        }
        
    }
    
    @Override
    public String getGreeting(){
        Project curProject = getCurProject();
        if (curProject == null){
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

    @Override
    public Predicate<Enquiry> getEnquiryViewFilter(){
        return enquiry -> ((Enquiry) enquiry).getUser().equals(this) || ((Enquiry) enquiry).getProject().getOfficers().contains(this);
    }

    @Override
    public Predicate<Enquiry> getEnquiryReplyFilter(){
        return enquiry -> ((Enquiry) enquiry).getProject().getOfficers().contains(this) ||
            super.getEnquiryReplyFilter().test(enquiry);
    }
}