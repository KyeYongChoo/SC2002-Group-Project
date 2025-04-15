package program.entity.project;
import java.time.LocalDate;

import program.boundary.console.DateTimeFormat;
import program.control.Main;
import program.control.TimeCompare;
import program.control.enquiry.Enquiry;
import program.control.enquiry.EnquiryList;
import program.control.housingApply.HousingReqList;
import program.entity.users.Manager;
import program.entity.users.Officer;
import program.entity.users.User;
import program.entity.users.UserList;

public class Project {
    private String name;
    private String neighbourhood;
    private int units2room;
    private int units2roomPrice;
    private int units3room;
    private int units3roomPrice;
    private LocalDate openDate;
    private LocalDate closeDate;
    private Manager createdBy;
    private Manager manager;
    private int officerSlots;
    // Note projOfficerList != MainActivity.projectOfficerList
    private UserList projOfficerList = new UserList();
    private boolean visibility = true;
    private HousingReqList reqList = new HousingReqList();
    private EnquiryList enquiryList = new EnquiryList();

    // Currently used by the HousingReq and HousingReqList classes only. May want to refactor the units2room units3 room to be a HashMap<ROOMTYPE, ArrayList<int vacancies, int price>>. 
    public static enum ROOM_TYPE{
        room2,
        room3
    }
    

    public Project(
        String name, 
        String neighbourhood, 
        String units2room, 
        String units2roomPrice, 
        String units3room, 
        String units3roomPrice, 
        String openDate, 
        String closeDate, 
        String manager, 
        String officerSlots,
        String OfficerLstStrInput) throws Exception{

        this.name = name.trim().toUpperCase();
        this.neighbourhood = neighbourhood.trim().toUpperCase();
        try {
            this.units2room = Integer.parseInt(units2room);
            this.units2roomPrice = Integer.parseInt(units2roomPrice);
            this.units3room = Integer.parseInt(units3room);
            this.units3roomPrice = Integer.parseInt(units3roomPrice);
        } catch (NumberFormatException ExceptionDueToEmptyString) {
        }
        this.openDate = LocalDate.parse(openDate, DateTimeFormat.getDateFormatter());
        this.closeDate = LocalDate.parse(closeDate, DateTimeFormat.getDateFormatter());
        this.createdBy = this.manager = (Manager) Main.managerList.getByName(manager);
        if(this.manager == null) throw new Exception("Manager not Found. Manager field for reference: "+ manager);
        try {
            this.officerSlots = Integer.parseInt(officerSlots);
        } catch (NumberFormatException e) {
            throw new Exception("Officer Slot isn't an Integer. Officer Slot field for reference: "+ officerSlots);
        }
        if (OfficerLstStrInput.isEmpty()) return;
        Officer officer;
        for (String officerStr : OfficerLstStrInput.split(",")){
            officer = (Officer) Main.officerList.getByName(officerStr);
            if (officer == null) throw new Exception("Officer not found. Officer for reference: "+ officer);
            projOfficerList.add(officer);
        }   
    }

    public boolean isManager(Manager manager){
        return manager == this.manager;
    }

    public static boolean isManager(Project project, Manager manager){
        return project.isManager(manager);
    }

    public void setVisibility(boolean visibility){
        this.visibility = visibility;
    }

    public EnquiryList getEnquiryList(){
        return enquiryList;
    }

    public boolean isVisibleTo(User user) {
        boolean inCharge = (user instanceof Manager && user.equals(this.manager)) || this.projOfficerList.contains(user);
        boolean applicationOpen = visibility  
                                && nowOpen()
                                && (user.see2Rooms() && units2room > 0 || user.see3Rooms() && units3room > 0);
        return applicationOpen || inCharge;
    }

    public boolean nowOpen(){
        LocalDate today = LocalDate.now();
        return (today.isEqual(openDate) || today.isAfter(openDate)) && (today.isBefore(closeDate) || today.isEqual(closeDate));
    }

    public boolean conflictInterest(User user) {
        // // For debug
        // System.out.println("Manager: " + (user instanceof Manager)); // User is a manager
        // System.out.println("Involved in this proj: " + this.projOfficerList.contains(user)); // User is/was an officer working on this project
        // System.out.println("user busy: " + (user instanceof Officer && !TimeCompare.officerUnassigned(((Officer) user), this))); // User is an officer with overlapping time with this project
        // System.out.println("alreadyHave House: " + user.hasActiveApplication());
        return (user instanceof Manager) // User is a manager
                || this.projOfficerList.contains(user) // User is/was an officer working on this project
                || (user instanceof Officer && !TimeCompare.officerUnassigned(((Officer) user), this)) // User is an officer with overlapping time with this project
                || user.hasActiveApplication();
    }

    public boolean canDeleteEnquiry(Enquiry enquiry) {
        return enquiry.getReplies().isEmpty();
    }
    
    public void setReqList(HousingReqList reqList){
        this.reqList = reqList;
    }
    public HousingReqList getReqList(){
        return reqList;
    }
    public String getNeighbourhood(){
        return neighbourhood;
    }

    public void setUnits2Room(int units2room){
        this.units2room = units2room;
    }

    public void setUnits3Room(int units3room){
        this.units3room = units3room;
    }

    public int getUnits2Room(){
        return units2room;
    }

    public int getUnits3Room(){
        return units3room;
    }

    public int getUnits2RoomPrice(){
        return units2roomPrice;
    }

    public String getName(){
        return name;
    }

    public int getUnits3RoomPrice(){
        return units3roomPrice;
    }
    public LocalDate getOpenDate(){
        return openDate;
    }
    public LocalDate getCloseDate(){
        return closeDate;
    }
    public void setCloseDate(LocalDate closeDate){
        this.closeDate = closeDate;
    }
    public void setManager(Manager manager){
        this.manager = manager;
    }
    public Manager getManager(){
        return manager;
    }
    public void setOfficerSlots(int officerSlots){
        this.officerSlots = officerSlots;
    }
    public int getOfficerSlots(){
        return officerSlots;
    }
    public UserList getOfficers(){
        return projOfficerList;
    }

    @Override
    public String toString(){
        return name;
    }

    public Manager getCreatedBy(){
        return createdBy;
    }

    public boolean getVisibility() {
        return visibility;
    }

    public int getVacancy (ROOM_TYPE roomType){
        return switch(roomType){
            case room2 -> units2room;
            case room3 -> units3room;
        };
    }

    public void decrementRoomType (ROOM_TYPE roomType){
        switch(roomType){
            case room2 -> units2room--;
            case room3 -> units3room--;
        };
    }

    public void incrementRoomType (ROOM_TYPE roomType){
        switch(roomType){
            case room2 -> units2room++;
            case room3 -> units3room--;
        };
    }

    public void setName(String name) {
        this.name = name.trim().toUpperCase();
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood.trim().toUpperCase();
    }

    public void setUnits2RoomPrice(int units2roomPrice) {
        this.units2roomPrice = units2roomPrice;
    }

    public void setUnits3RoomPrice(int units3roomPrice) {
        this.units3roomPrice = units3roomPrice;
    }

    public void setOpenDate(LocalDate openDate) {
        this.openDate = openDate;
    }
}
