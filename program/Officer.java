package program;

import java.time.LocalDate;
import java.time.LocalDateTime;
import program.Project.ROOM_TYPE;
import program.HousingReq.REQUEST_STATUS;
import program.HousingReqList;
import program.AssignReq.APPLICATION_STATUS;

public class Officer extends Applicant {
    public Officer(String NRIC, String name, int age, String marital_status, String password) throws Exception{
        super(NRIC, name, age, marital_status, password);
    }

    public AssignReqList getAssignReqList(){
        AssignReqList reqList = new AssignReqList();
        for (AssignReq req : MainActivity.assignReqList){
            if (req.getOfficer() == this){
                reqList.add(req);
            }
        }
        return reqList;
    }
    public boolean assigned(){
        return this.getProject() != null;
    }

    public Project getProject(){
        for (Project project : MainActivity.projectList){
            if (project.getOfficers().contains(this)){
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
        if (this.getProject()!=project) {
            System.out.println("You are not assigned to this project, cannot update flats.");
            return;
        }
        if (flatType == ROOM_TYPE.room2) {
            project.setUnits2Room(newCount);
        } else {
            project.setUnits3Room(newCount);
        }
        System.out.println("Updated " + flatType + " availability to " + newCount + " for project: " + this.getProject().getName());
    }

    /**
     * Updates the status of a specific applicant’s BTO application. 
     * E.g., from PENDING to SUCCESSFUL, or SUCCESSFUL to BOOKED, etc.
     */
    public void updateApplicantStatus(HousingReq application, REQUEST_STATUS newStatus) {
        if (this.getProject()!=application.getProject()) {
            System.out.println("You are not authorized to update applications for this project.");
        }
        else{
            application.setStatus(newStatus);
            System.out.println("Application " + application.getUser() + " status changed to " + newStatus);
        }
        
    }

    /**
     * Retrieves a BTOApplication for an applicant based on NRIC, within the project(s) this Officer handles.
     * Adjust as needed if you keep track of multiple projects or application repositories.
     */
    public HousingReq retrieveApplication(String nric) {
        // If an Officer is assigned to a single project:
        if (this.assigned() == false) {
            System.out.println("Officer is not assigned to any project.");
            return null;
        }
        HousingReqList reqList = this.getProject().getReqList();
        // Iterate through all HousingReq in that list, searching for the matching NRIC
        for (HousingReq req : reqList) {
            User reqUser = req.getUser();
            if (reqUser != null && reqUser.getUserId().equalsIgnoreCase(nric)) {
                return req;  // Found the matching application
            }
        }
        System.out.println("No application found for NRIC: " + nric + " in project " + this.getProject().getName());
        return null;
    }

    /**
     * Displays the Officer's own profile details (NRIC, Name, Age, etc.).
     */
    public void viewOfficerProfile() {
        System.out.println("=== Officer Profile ===");
        System.out.println("Name         : " + this.getName());
        System.out.println("NRIC         : " + this.getUserId());
        System.out.println("Age          : " + this.getAge());
        System.out.println("Marital Status: " + this.getMaritalStatus());
        // Add any additional fields or methods as needed
    }

    public void updateApplicantProfile(HousingReq application, ROOM_TYPE flatType) {

        // i have no idea why the officer is supposed to update the applicant's profile with the type of flat chosen under the project
    }

    /**
     * Generates a receipt for a booked application. 
     * Typically done after the applicant's status is set to BOOKED.
     */
    public void generateReceipt(HousingReq application) {
        if (this.getProject()!=application.getProject()) {
            System.out.println("You are not assigned to this project, cannot generate a receipt.");
        }
        else{
            if (application.getStatus() != REQUEST_STATUS.booked) {
                System.out.println("Cannot generate a receipt because the application is not BOOKED yet.");
            }
            else{
                System.out.println("=== Receipt ===");
                System.out.println("Name         : " + application.getUser().getName());
                System.out.println("NRIC         : " + application.getUser().getUserId());
                System.out.println("Age          : " + application.getUser().getAge());
                System.out.println("Marital Status: " + application.getUser().getMaritalStatus());
                System.out.println("Flat Type Booked: " + application.getRoomType());
                System.out.println("Project Name: " + application.getProject().getName());
                System.out.println("Project Location: " + application.getProject().getNeighbourhood());
            }
        }
    }

    /**
     * View (or list) inquiries for the Officer’s assigned project(s). 
     * If you store Enquiries in a collection in BTOProject or a separate manager class, 
     * adapt the retrieval accordingly.
     */
    public void viewInquiries(Project project) {
        if (this.getProject()!=project) {
            System.out.println("You do not handle this project, cannot view inquiries.");
        }
        else{
            System.out.println("=== Inquiries for Project: " + project.getName() + " ===");
            for (Enquiry e : project.getEnquiryList()) {
                System.out.println("Enquiry ID: " + e.getId() + " | Author: " + e.getUser().getName() + " | Content: " + e.get(0).getText());
            }
        
        }
    }

    /**
     * Replies to an inquiry with a given string. Typically you’d store the reply in the Enquiry or 
     * log it somewhere.
     */
    public void replyInquiries(Enquiry enquiry, String reply) {
        Project project = enquiry.getProject();
        if (this.getProject()!=project) {
            System.out.println("You do not handle this project, cannot reply to inquiries.");
            return;
        }
        enquiry.addReply("Officer " + this.getName() + " replies: " + reply);
        System.out.println("Reply added to Enquiry ID: " + enquiry.getEnquiryID());
    }

     /**
     * Views the project details of the assigned project or any project the system allows an officer to see.
     */
    public void viewProject(Project project) {
        if (this.getProject()!=project) {
            System.out.println("You are not authorized to view the full details of this project.");
            return;
        }
        System.out.println("=== Project Details ===");
        System.out.println("Project Name     : " + project.getName());
        System.out.println("Neighborhood     : " + project.getNeighbourhood());
        System.out.println("Available 2-Room : " + project.getUnits2Room());
        System.out.println("Available 3-Room : " + project.getUnits3Room());
        // Print any other relevant info your BTOProject class may hold
    }



    public boolean overlapTime (Project targetProject){
        return overlapTime(targetProject,this.getProject());
    }
    public boolean overlapTime (Project proj1, Project proj2){
        if (proj1 == null || proj2 == null) return false;
        LocalDate openDate1 = proj1.getOpenDate();
        LocalDate openDate2 = proj2.getOpenDate();
        LocalDate closeDate1 = proj1.getCloseDate();
        LocalDate closeDate2 = proj2.getCloseDate();
        if (openDate1.isAfter(closeDate2) || closeDate1.isBefore(openDate2)) return false;
        else return true;
    }
    public boolean overlapTime (LocalDateTime dateTime){
        return overlapTime(dateTime.toLocalDate());
    }
    public boolean overlapTime (LocalDate date){
        LocalDate openDate = this.getProject().getOpenDate();
        LocalDate closeDate = this.getProject().getCloseDate();
        if (openDate.isBefore(date) && closeDate.isAfter(date)){
            return true;
        }else return false;
    }
}