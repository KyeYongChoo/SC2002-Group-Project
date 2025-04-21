package program.entity.users;

import java.util.function.Predicate;

import program.control.Main;
import program.control.TimeCompare;
import program.control.enquiry.Enquiry;
import program.control.security.Password;
import program.entity.project.Project;



/**
 * <p>
 * The {@code Manager} class represents a specific type of officer who holds a managerial role
 * and is responsible for overseeing projects. This class extends the {@link Officer} class and
 * inherits all the properties and functionalities of an officer. In addition to the inherited
 * functionalities, the {@code Manager} class introduces project-specific responsibilities, such as
 * managing the current project and filtering enquiries.
 * </p>
 *
 * <p>
 * A {@code Manager} can promote officers, manage the current project they are overseeing, and filter
 * enquiries based on their managerial role. The class provides constructors to initialize a manager
 * with essential personal details such as NRIC, name, age, marital status, and password.
 * </p>
 *
 * @see program.entity.users.Officer
 */
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
    /**
     * Constructs a {@code Manager} with the specified NRIC, name, age, marital status, and password.
     *
     * @param NRIC the NRIC of the manager
     * @param name the name of the manager
     * @param age the age of the manager
     * @param marital_status the marital status of the manager
     * @param password the password of the manager
     * @throws Exception if there is an error during the initialization of the manager
     */
    public Manager(String NRIC, String name, int age, String marital_status, String password) throws Exception{
        super(NRIC, name, age, marital_status, password);
    }
    /**
     * Constructs a {@code Manager} with the specified NRIC, name, age, and marital status.
     * The password is not set in this constructor.
     *
     * @param NRIC the NRIC of the manager
     * @param name the name of the manager
     * @param age the age of the manager
     * @param marital_status the marital status of the manager
     * @throws Exception if there is an error during the initialization of the manager
     */
    public Manager(String NRIC, String name, int age, String marital_status) throws Exception{
        super(NRIC, name, age, marital_status);
    }
    
    /**
     * Constructs a {@code Manager} with the specified NRIC, name, age, marital status, and password object.
     *
     * @param NRIC the NRIC of the manager
     * @param name the name of the manager
     * @param age the age of the manager
     * @param marital_status the marital status of the manager
     * @param password the password object of the manager
     * @throws Exception if there is an error during the initialization of the manager
     */
    public Manager(String NRIC, String name, int age, String marital_status, Password password) throws Exception{
        super(NRIC, name, age, marital_status, password);
    }

    /**
     * Promotes an {@link Officer} to a {@code Manager}. The current implementation does not modify
     * the officer's role and simply returns the manager instance.
     *
     * @param officer the {@link Officer} to be promoted
     * @return the current {@code Manager} instance
     */
    public void setReportFilter(REPORT_FILTER filter){
        reportFilter = filter;
    }

    public REPORT_FILTER getReportFilter (){
        return reportFilter;
    }

    /**
     * Retrieves the project that the {@code Manager} is currently overseeing. A manager can be
     * in charge of multiple projects at different times, and this method returns the currently
     * active project assigned to the manager.
     *
     * @return the current {@link Project} being managed by the manager, or {@code null} if no active project is found
     */
    @Override 
    public Project getCurProject() {
        return Main.projectList.stream()
            .filter(project -> project.getManager().equals(this) && 
                TimeCompare.currentlyActive(project))
            .findAny()
            .orElse(null);
    }

    /**
     * Provides a personalized greeting for the {@code Manager}, including details about the
     * current project they are handling.
     *
     * @return a string containing a greeting message with the project information
     */
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

    /**
     * Returns a filter predicate that allows the {@code Manager} to reply to enquiries only for
     * projects that they are managing.
     *
     * @return a predicate that filters enquiries based on whether the {@code Manager} is in charge of the project
     */
    @Override
    public Predicate<Enquiry> getEnquiryReplyFilter(){
        return enquiry -> ((Enquiry) enquiry).getProject().getManager().equals(this);
    }
}
