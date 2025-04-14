package program.entity.project;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import program.boundary.console.AppScanner;
import program.control.Main;
import program.control.enquiry.Enquiry;
import program.control.enquiry.EnquiryList;
import program.control.housingApply.HousingReqList;
import program.control.officerApply.TimeCompare;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        this.openDate = LocalDate.parse(openDate, formatter);
        this.closeDate = LocalDate.parse(closeDate, formatter);
        this.createdBy = this.manager = (Manager) Main.managerList.getByName(manager);
        if(this.manager == null) throw new Exception("Manager not Found. Manager field for reference: "+ manager);
        try {
            this.officerSlots = Integer.parseInt(officerSlots);
        } catch (NumberFormatException e) {
            throw new Exception("Officer Slot isn't an Integer. Officer Slot field for reference: "+ officerSlots);
        }
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

    public void setVisibility(Manager manager) throws Exception{
        if (!isManager(manager)){
            throw new Exception("You are not the manager of this HDB! ");
        }

        String choice;
        Scanner sc = AppScanner.getInstance();
        do { 
            System.out.println("Please toggle visibility of " + name + " (Y/N)" +"\nCurrent Visibility: " + (visibility?"Y":"N"));
            choice = sc.nextLine().toUpperCase();
        } while (!"Y".equals(choice) && !"N".equals(choice));
        sc.close();
        this.visibility = ("Y".equals(choice));
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
        return (user instanceof Manager // User is a manager
                || this.projOfficerList.contains(user) // User is/was an officer working on this project
                || (user instanceof Officer && TimeCompare.officerUnassigned(((Officer) user), this))) // User is an officer with overlapping time with this project
                || Main.reqList.stream().anyMatch(HousingReq -> HousingReq.getUser().equals(user));
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
}
