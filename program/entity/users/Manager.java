package program.entity.users;

import java.util.function.Predicate;

import program.control.Main;
import program.control.TimeCompare;
import program.control.enquiry.Enquiry;
import program.control.security.Password;
import program.entity.project.Project;

public class Manager extends Officer {
    private REPORT_FILTER reportFilter = REPORT_FILTER.VIEW_ALL;
    public enum REPORT_FILTER{
        MARRIED,
        SINGLE,
        PROJECT,
        FLAT_TYPE_2_ROOM,
        FLAT_TYPE_3_ROOM,
        VIEW_ALL
    }

    public Manager(String NRIC, String name, int age, String marital_status, String password) throws Exception{
        super(NRIC, name, age, marital_status, password);
    }
    
    public Manager(String NRIC, String name, int age, String marital_status) throws Exception{
        super(NRIC, name, age, marital_status);
    }
    
    public Manager(String NRIC, String name, int age, String marital_status, Password password) throws Exception{
        super(NRIC, name, age, marital_status, password);
    }

    public void setReportFilter(REPORT_FILTER filter){
        reportFilter = filter;
    }

    public REPORT_FILTER getReportFilter (){
        return reportFilter;
    }

    /*
     * returns the project which Manager is in charge of now
     * Take note Manager may be in charge of different projects in the past and future 
     */
    @Override 
    public Project getCurProject() {
        return Main.projectList.stream()
            .filter(project -> project.getManager().equals(this) && 
                TimeCompare.currentlyActive(project))
            .findAny()
            .orElse(null);
    }

    @Override
    public String getGreeting(){
        Project project = getCurProject();
        return ("\nWelcome " + getName() + 
            "\nYou are currently " + 
            (this.see3Rooms() ? "eligible" : "ineligible") + 
            " to see 3-Room and may see 2-Rooms\n" +
            "User ID: " + getUserId() + 
            "\nAge: " + getAge() +
            "\nMarital Status: " + getMaritalStatus() +
            ((project!=null)?
            ("\nYou are currently handling project:\n" + project + "\nFrom " + project.getOpenDate() + " until " +project.getCloseDate()):
            ("\nYou are currently not handling a project")));
    }

    @Override
    public Predicate<Enquiry> getEnquiryViewFilter(){
        return enquiry -> true;
    }

    @Override
    public Predicate<Enquiry> getEnquiryReplyFilter(){
        return enquiry -> ((Enquiry) enquiry).getProject().getManager().equals(this);
    }
}
