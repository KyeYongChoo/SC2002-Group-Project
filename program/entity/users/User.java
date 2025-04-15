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

public class User{
    private String name;
    private String userId;
    private Password password = new Password(); 
    private int age;
    private MARITAL_STATUS maritalStatus;
    private HousingReqList reqList = new HousingReqList();
    private EnquiryList enquiryList = new EnquiryList();
    private FILTER_SETTING filterSetting = FILTER_SETTING.ALPHABETICAL;

    public static enum FILTER_SETTING{
        LOCATION,
        FLAT_TYPE_2_ROOM,
        FLAT_TYPE_3_ROOM,
        ALPHABETICAL,
        OWN_PROJECTS_ONLY
    }

    public Project getApprovedProject(){
        return reqList.stream().filter(req -> req.getStatus().equals(HousingReq.REQUEST_STATUS.successful))
            .findAny().map(HousingReq::getProject).orElse(null);
    }
    public List<Project> getProjectStatus(HousingReq.REQUEST_STATUS status){
        return reqList.stream().filter(req -> req.getStatus().equals(status))
            .map(HousingReq::getProject).collect(Collectors.toList());
    }

    public FILTER_SETTING getFilterSetting(){
        return filterSetting;
    }

    public void setFilterSetting(FILTER_SETTING filterSetting){
        this.filterSetting = filterSetting;
    }

    public EnquiryList getEnquiryList(){
        return enquiryList;
    }

    public boolean see3Rooms(){
        return age >= 21 && maritalStatus == User.MARITAL_STATUS.Married;
    }

    public boolean see2Rooms(){
        return age >= 35 && maritalStatus == User.MARITAL_STATUS.Single;
    }

    public static enum MARITAL_STATUS {
        Married,
        Single
    }

    public boolean hasActiveApplication(){
        return HousingReqList.activeReq(this) != null;
    }

    public User(String NRIC, String name, int age, String maritalStatus) throws Exception{
        this.userId = UserValidator.validateNRIC(NRIC);
        this.name = name;
        this.age = validateAge(age);
        this.maritalStatus = validateMaritalStatus(maritalStatus);
        
    }

    public User(String NRIC, String name, int age, String maritalStatus, String passwordHash) throws Exception{
        this(NRIC, name, age, maritalStatus);
        this.password = new Password(passwordHash);
        
    }

    public User(String NRIC, String name, int age, String maritalStatus, Password password) throws Exception{
        this(NRIC, name, age, maritalStatus);
        this.password = password;
        
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Password getPassword(){
        return password;
    }

    public MARITAL_STATUS getMaritalStatus(){
        return maritalStatus;
    }

    public void setMaritalStatus(MARITAL_STATUS maritalStatus){
        this.maritalStatus = maritalStatus;
    }
    
    public void setMaritalStatus(String maritalStatus) throws Exception{
        this.maritalStatus = validateMaritalStatus(maritalStatus);
    }

    // In case I'm forgetful and forgot which class to call printPastReq on
    public void printPastReq(){
        HousingReqList.printPast(this);
    }

    public static void printPastReq(User client){
        HousingReqList.printPast(client);
    }

    public int getAge(){
        return age;
    }

    public final int validateAge(int age) throws Exception{
        if (age < 0 || age > 150) {
            throw new Exception("Age should be between 0 and 150 inclusive");
        }
        return age;
    }

    public String getUserId(){
        return userId;
    }

    public void setReqList(HousingReqList reqList){
        this.reqList = reqList;
    }
    public HousingReqList getReqList(){
        return reqList;
    }

    @Override
    public String toString(){
        return name;
    }

    public final MARITAL_STATUS validateMaritalStatus(String maritalStatus) throws Exception{
        switch (maritalStatus.toUpperCase().trim()){
            case "MARRIED": 
                return MARITAL_STATUS.Married;
            case "SINGLE": 
                return MARITAL_STATUS.Single;
            default:
                throw new Exception("Marital Status for NRIC No." + this.userId + "should be either Married or Single\nReceived marital status: " + maritalStatus);
        }
    }
    
    public void setPassword(Password password) {
        this.password = password;
    }

    public String getGreeting() {
        return ("Welcome " + name + "\nYou are currently " + (this.see3Rooms() ? "eligible" : "ineligible") + " to see 3-Room and may see 2-Rooms");
    }

    // Currently Unused. Could be used for shorthand later on
    public Stream<Project> userFilterStream(List<Project> projects) {
        return UserPrefSorting.userFilterStream(this, projects);
    }

    public Predicate<Enquiry> getEnquiryViewFilter(){
        return enquiry -> ((Enquiry) enquiry).getUser().equals(this);
    }

    public Predicate<Enquiry> getEnquiryEditDeleteFilter(){
        return enquiry -> ((Enquiry) enquiry).getUser().equals(this) && !((Enquiry) enquiry).isStaffReplyPresent();
    }

    public Predicate<Enquiry> getEnquiryReplyFilter(){
        return enquiry -> false; // Applicants cannot reply
    }

}