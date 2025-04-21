package program.entity.users;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import program.boundary.projectIO.UserPrefSorting;
import program.boundary.security.UserValidator;
import program.control.enquiry.Enquiry;
import program.control.enquiry.EnquiryList;
import program.control.housingApply.HousingReq;
import program.control.housingApply.HousingReqList;
import program.control.security.Password;
import program.entity.project.Project;

/**
 * <p>
 * The {@code User} class represents a general user in the system, which includes essential personal
 * details such as name, age, marital status, and user ID. This class is the base class for various user
 * types, including applicants and officers, and provides functionalities such as viewing projects,
 * filtering projects, managing housing applications, and interacting with enquiries.
 * </p>
 *
 * <p>
 * The {@code User} class also contains methods for filtering projects based on user preferences,
 * checking eligibility for 2-room or 3-room flats, and updating marital status and age information.
 * </p>
 */
public class User {

    private String name;
    private String userId;
    private Password password = new Password();
    private int age;
    private MARITAL_STATUS maritalStatus;
    private HousingReqList reqList = new HousingReqList();
    private EnquiryList enquiryList = new EnquiryList();
    private FILTER_SETTING filterSetting = FILTER_SETTING.ALPHABETICAL;

    public static enum FILTER_SETTING {
        LOCATION,
        FLAT_TYPE_2_ROOM,
        FLAT_TYPE_3_ROOM,
        ALPHABETICAL,
        OWN_PROJECTS_ONLY
    }

    public static enum MARITAL_STATUS {
        Married,
        Single
    }

    /**
     * Retrieves the first approved project based on the status of the housing application.
     *
     * @return the approved {@link Project}, or {@code null} if no approved project is found
     */
    public Project getApprovedProject() {
        return reqList.stream().filter(req -> req.getStatus().equals(HousingReq.REQUEST_STATUS.successful))
                .findAny().map(HousingReq::getProject).orElse(null);
    }

    /**
     * Returns a list of projects filtered by the status of the housing request.
     *
     * @param status the status of the housing request
     * @return a list of {@link Project} objects associated with the specified status
     */
    public List<Project> getProjectStatus(HousingReq.REQUEST_STATUS status) {
        return reqList.stream().filter(req -> req.getStatus().equals(status))
                .map(HousingReq::getProject).collect(Collectors.toList());
    }

    /**
     * Returns the current filter setting used for sorting or viewing projects.
     *
     * @return the current filter setting
     */
    public FILTER_SETTING getFilterSetting() {
        return filterSetting;
    }

    /**
     * Sets the filter setting used for sorting or viewing projects.
     *
     * @param filterSetting the new filter setting to apply
     */
    public void setFilterSetting(FILTER_SETTING filterSetting) {
        this.filterSetting = filterSetting;
    }

    /**
     * Retrieves the list of enquiries associated with the user.
     *
     * @return an {@link EnquiryList} containing the user's enquiries
     */
    public EnquiryList getEnquiryList() {
        return enquiryList;
    }

    /**
     * Checks if the user is eligible to view 3-room flats. The eligibility criteria require
     * the user to be married and at least 21 years old.
     *
     * @return {@code true} if eligible to view 3-room flats, otherwise {@code false}
     */
    public boolean see3Rooms() {
        return age >= 21 && maritalStatus == User.MARITAL_STATUS.Married;
    }

    /**
     * Checks if the user is eligible to view 2-room flats. The eligibility criteria require
     * the user to be single and at least 35 years old.
     *
     * @return {@code true} if eligible to view 2-room flats, otherwise {@code false}
     */
    public boolean see2Rooms() {
        return age >= 35 && maritalStatus == User.MARITAL_STATUS.Single;
    }

    /**
     * Checks if the user has any active housing application.
     *
     * @return {@code true} if there is an active housing application, otherwise {@code false}
     */
    public boolean hasActiveApplication() {
        return HousingReqList.activeReq(this) != null;
    }

    /**
     * Constructs a new {@code User} with the provided NRIC, name, age, marital status, and validates the inputs.
     *
     * @param NRIC the NRIC of the user
     * @param name the name of the user
     * @param age the age of the user
     * @param maritalStatus the marital status of the user
     * @throws Exception if there is an issue with the provided data (e.g., invalid NRIC, age, or marital status)
     */
    public User(String NRIC, String name, int age, String maritalStatus) throws Exception {
        this.userId = UserValidator.validateNRIC(NRIC);
        this.name = name;
        this.age = validateAge(age);
        this.maritalStatus = validateMaritalStatus(maritalStatus);
    }

    /**
     * Constructs a new {@code User} with the provided NRIC, name, age, marital status, and password hash.
     *
     * @param NRIC the NRIC of the user
     * @param name the name of the user
     * @param age the age of the user
     * @param maritalStatus the marital status of the user
     * @param passwordHash the hashed password of the user
     * @throws Exception if there is an issue with the provided data (e.g., invalid NRIC, age, or marital status)
     */
    public User(String NRIC, String name, int age, String maritalStatus, String passwordHash) throws Exception {
        this(NRIC, name, age, maritalStatus);
        this.password = new Password(passwordHash);
    }

    /**
     * Constructs a new {@code User} with the provided NRIC, name, age, marital status, and password object.
     *
     * @param NRIC the NRIC of the user
     * @param name the name of the user
     * @param age the age of the user
     * @param maritalStatus the marital status of the user
     * @param password the password object of the user
     * @throws Exception if there is an issue with the provided data (e.g., invalid NRIC, age, or marital status)
     */
    public User(String NRIC, String name, int age, String maritalStatus, Password password) throws Exception {
        this(NRIC, name, age, maritalStatus);
        this.password = password;
    }

    /**
     * Retrieves the name of the user.
     *
     * @return the user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     *
     * @param name the new name for the user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the user's password object.
     *
     * @return the password object of the user
     */
    public Password getPassword() {
        return password;
    }

    /**
     * Retrieves the marital status of the user.
     *
     * @return the marital status of the user
     */
    public MARITAL_STATUS getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * Sets the marital status of the user.
     *
     * @param maritalStatus the new marital status for the user
     */
    public void setMaritalStatus(MARITAL_STATUS maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    /**
     * Sets the marital status of the user from a string input.
     *
     * @param maritalStatus the string representation of the marital status
     * @throws Exception if the marital status is not valid
     */
    public void setMaritalStatus(String maritalStatus) throws Exception {
        this.maritalStatus = validateMaritalStatus(maritalStatus);
    }

    /**
     * Prints all past housing requests for the user.
     */
    public void printPastReq() {
        HousingReqList.printPast(this);
    }

    /**
     * Prints all past housing requests for a specified user.
     *
     * @param client the user whose past housing requests need to be printed
     */
    public static void printPastReq(User client) {
        HousingReqList.printPast(client);
    }

    /**
     * Retrieves the age of the user.
     *
     * @return the user's age
     */
    public int getAge() {
        return age;
    }

    /**
     * Validates the provided age to ensure it is within a valid range (0-150).
     *
     * @param age the age to validate
     * @return the validated age
     * @throws Exception if the age is not within the valid range
     */
    public final int validateAge(int age) throws Exception {
        if (age < 0 || age > 150) {
            throw new Exception("Age should be between 0 and 150 inclusive");
        }
        return age;
    }

    /**
     * Retrieves the user ID.
     *
     * @return the user's ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the housing request list for the user.
     *
     * @param reqList the new housing request list to set
     */
    public void setReqList(HousingReqList reqList) {
        this.reqList = reqList;
    }

    /**
     * Retrieves the housing request list for the user.
     *
     * @return the user's housing request list
     */
    public HousingReqList getReqList() {
        return reqList;
    }

    /**
     * Provides a string representation of the user.
     *
     * @return the string representation of the user's name
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Validates the provided marital status string.
     *
     * @param maritalStatus the marital status string to validate
     * @return the corresponding marital status enum value
     * @throws Exception if the marital status is not valid
     */
    public final MARITAL_STATUS validateMaritalStatus(String maritalStatus) throws Exception {
        switch (maritalStatus.toUpperCase().trim()) {
            case "MARRIED":
                return MARITAL_STATUS.Married;
            case "SINGLE":
                return MARITAL_STATUS.Single;
            default:
                throw new Exception("Marital Status for NRIC No." + this.userId + " should be either Married or Single\nReceived marital status: " + maritalStatus);
        }
    }

    /**
     * Sets the password for the user.
     *
     * @param password the new password for the user
     */
    public void setPassword(Password password) {
        this.password = password;
    }

    /**
     * Retrieves a greeting message for the user, indicating their eligibility for viewing flats.
     *
     * @return the greeting message for the user
     */
    public String getGreeting() {
        return ("Welcome " + name + 
            "\nYou are currently " + 
            (this.see3Rooms() ? "eligible" : "ineligible") + 
            " to see 3-Room and may see 2-Rooms\n" +
            "User ID: " + userId + 
            "\nAge: " + age +
            "\nMarital Status: " + maritalStatus);
    }

    /**
     * Applies the user filter to a list of projects, using user preferences for sorting and filtering.
     *
     * @param projects the list of projects to filter
     * @return a stream of filtered {@link Project} objects based on the user's preferences
     */
    public Stream<Project> userFilterStream(List<Project> projects) {
        return UserPrefSorting.userFilterStream(this, projects);
    }

    /**
     * Returns a filter predicate for viewing enquiries related to the user.
     *
     * @return a predicate that filters enquiries visible to the user
     */
    public Predicate<Enquiry> getEnquiryViewFilter() {
        return enquiry -> ((Enquiry) enquiry).getUser().equals(this);
    }

    /**
     * Returns a filter predicate for editing or deleting enquiries related to the user.
     *
     * @return a predicate that filters editable or deletable enquiries for the user
     */
    public Predicate<Enquiry> getEnquiryEditDeleteFilter() {
        return enquiry -> ((Enquiry) enquiry).getUser().equals(this) && !((Enquiry) enquiry).isStaffReplyPresent();
    }

    /**
     * Returns a filter predicate for replying to enquiries related to the user.
     *
     * @return a predicate that always returns false since applicants cannot reply to enquiries
     */
    public Predicate<Enquiry> getEnquiryReplyFilter() {
        return enquiry -> false; // Applicants cannot reply
    }
}
