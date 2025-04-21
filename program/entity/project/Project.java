package program.entity.project;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import program.control.Main;
import program.control.TimeCompare;
import program.control.enquiry.Enquiry;
import program.control.enquiry.EnquiryList;
import program.control.housingApply.HousingReq;
import program.control.housingApply.HousingReqList;
import program.entity.users.Manager;
import program.entity.users.Officer;
import program.entity.users.User;
import program.entity.users.UserList;

/**
 * <p>
 * The {@code Project} class represents a housing project that is part of the application. Each project is
 * associated with a manager, has a set of officer slots, and contains information about housing units, including
 * the number of available rooms and their prices. Additionally, it includes visibility rules and housing request
 * management, as well as the ability to handle project-specific enquiries and officers assigned to the project.
 * </p>
 *
 * <p>
 * The class is designed to handle project lifecycle, including opening and closing dates, visibility settings,
 * and the addition of officers to the project.
 * </p>
 *
 * <p>
 * The class also provides several utility methods to check if a user is eligible to view the project or if there
 * are any conflicts of interest with regard to housing applications or officer assignments.
 * </p>
 *
 * @see program.entity.users.Manager
 * @see program.entity.users.Officer
 */
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
    private UserList projOfficerList = new UserList();
    private boolean visibility = true;
    private HousingReqList reqList = new HousingReqList();
    private EnquiryList enquiryList = new EnquiryList();

    /**
     * Enumeration representing the types of rooms available in the project.
     */
    public static enum ROOM_TYPE {
        room2, room3
    }

    /**
     * Constructs a new {@code Project} with the provided details.
     *
     * <p>
     * The constructor initializes a project with the given parameters, parsing the room counts, room prices, and
     * dates from string inputs. The project is also associated with a manager and a list of officers.
     * </p>
     *
     * @param name the name of the project
     * @param neighbourhood the neighbourhood where the project is located
     * @param units2room the number of 2-room units in the project
     * @param units2roomPrice the price of 2-room units in the project
     * @param units3room the number of 3-room units in the project
     * @param units3roomPrice the price of 3-room units in the project
     * @param openDate the opening date of the project
     * @param closeDate the closing date of the project
     * @param manager the name of the manager responsible for the project
     * @param officerSlots the number of officer slots available for the project
     * @param OfficerLstStrInput a comma-separated string of officer names assigned to the project
     * @throws Exception if any of the input values are invalid or cannot be parsed correctly
     */
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
            String OfficerLstStrInput) throws Exception {
        this.name = name.trim().toUpperCase();
        this.neighbourhood = neighbourhood.trim().toUpperCase();
        try {
            this.units2room = Integer.parseInt(units2room);
            this.units2roomPrice = Integer.parseInt(units2roomPrice);
            this.units3room = Integer.parseInt(units3room);
            this.units3roomPrice = Integer.parseInt(units3roomPrice);
        } catch (NumberFormatException ExceptionDueToEmptyString) {
        }
        this.openDate = LocalDate.parse(openDate, DateTimeFormatter.ofPattern("d/M/yyyy"));
        this.closeDate = LocalDate.parse(closeDate, DateTimeFormatter.ofPattern("d/M/yyyy"));
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

    /**
     * Checks if the given manager is the manager responsible for this project.
     *
     * @param manager the manager to check
     * @return {@code true} if the given manager is the manager of this project, {@code false} otherwise
     */
    public boolean isManager(Manager manager){
        return manager == this.manager;
    }

    /**
     * Checks if the specified project is managed by the given manager.
     *
     * @param project the project to check
     * @param manager the manager to check
     * @return {@code true} if the given project is managed by the specified manager, {@code false} otherwise
     */
    public static boolean isManager(Project project, Manager manager){
        return project.isManager(manager);
    }

    /**
     * Sets the visibility of the project.
     *
     * @param visibility {@code true} to make the project visible, {@code false} to hide it
     */
    public void setVisibility(boolean visibility){
        this.visibility = visibility;
    }

    /**
     * Gets the list of enquiries associated with this project.
     *
     * @return the list of enquiries
     */
    public EnquiryList getEnquiryList(){
        return enquiryList;
    }

    /**
     * Checks if the project is visible to the specified user.
     *
     * @param user the user to check
     * @return {@code true} if the project is visible to the user, {@code false} otherwise
     */
    public boolean isVisibleTo(User user) {
        boolean activeApplication = Main.housingReqList.stream().anyMatch(req -> req.getProject().equals(this) && 
            req.getUser().equals(user) &&
            req.getStatus() != HousingReq.REQUEST_STATUS.unsuccessful &&
            req.getWithdrawalStatus() != HousingReq.WITHDRAWAL_STATUS.approved
            );
        boolean inCharge = (user instanceof Manager && user.equals(this.manager)) || this.projOfficerList.contains(user);
        boolean applicationOpen = visibility  
                                && nowOpen()
                                && (user.see2Rooms() && units2room > 0 || user.see3Rooms() && (units3room > 0 || units2room > 0));
        return applicationOpen || inCharge || activeApplication;
    }

    /**
     * Checks if the project is currently open for applications.
     *
     * @return {@code true} if the project is open for applications, {@code false} otherwise
     */
    public boolean nowOpen(){
        LocalDate today = LocalDate.now();
        return (today.isEqual(openDate) || today.isAfter(openDate)) && (today.isBefore(closeDate) || today.isEqual(closeDate));
    }

    /**
     * Checks if there is any conflict of interest for the specified user with respect to the project.
     *
     * @param user the user to check
     * @return {@code true} if there is a conflict of interest, {@code false} otherwise
     */
    public boolean conflictInterest(User user) {
        // // For debug
        // System.out.println("Project: " + this);
        // System.out.println("Manager: " + (user instanceof Manager)); // User is a manager
        // System.out.println("Involved in this proj: " + this.projOfficerList.contains(user)); // User is/was an officer working on this project
        // System.out.println("user busy: " + (user instanceof Officer && !TimeCompare.officerUnassigned(((Officer) user), this))); // User is an officer with overlapping time with this project
        // System.out.println("alreadyHave application: " + user.hasActiveApplication());
        return (user instanceof Manager) // User is a manager
                || this.projOfficerList.contains(user) // User is/was an officer working on this project
                || (user instanceof Officer && !TimeCompare.officerUnassigned(((Officer) user), this)) // User is an officer with overlapping time with this project
                || user.hasActiveApplication();
    }

    /**
     * Checks if an enquiry can be deleted based on whether it has any replies.
     *
     * @param enquiry the enquiry to check
     * @return {@code true} if the enquiry can be deleted (i.e., it has no replies), {@code false} otherwise
     */
    public boolean canDeleteEnquiry(Enquiry enquiry) {
        return enquiry.getReplies().isEmpty();
    }

    /**
     * Sets the list of housing requests for this project.
     *
     * @param reqList the list of housing requests
     */
    public void setReqList(HousingReqList reqList) {
        this.reqList = reqList;
    }

    /**
     * Gets the list of housing requests for this project.
     *
     * @return the list of housing requests
     */
    public HousingReqList getReqList() {
        return reqList;
    }

    // Getter and setter methods for project properties

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setUnits2Room(int units2room) {
        this.units2room = units2room;
    }

    public void setUnits3Room(int units3room) {
        this.units3room = units3room;
    }

    public int getUnits2Room() {
        return units2room;
    }

    public int getUnits3Room() {
        return units3room;
    }

    public int getUnits2RoomPrice() {
        return units2roomPrice;
    }

    public String getName() {
        return name;
    }

    public int getUnits3RoomPrice() {
        return units3roomPrice;
    }

    public LocalDate getOpenDate() {
        return openDate;
    }

    public LocalDate getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(LocalDate closeDate) {
        this.closeDate = closeDate;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public Manager getManager() {
        return manager;
    }

    public void setOfficerSlots(int officerSlots) {
        this.officerSlots = officerSlots;
    }
    public int getOfficerSlots() {
        return officerSlots;
    }

    public UserList getOfficers() {
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

    public int getVacancy(ROOM_TYPE roomType) {
        return switch (roomType) {
            case room2 -> units2room;
            case room3 -> units3room;
        };
    }

    public void decrementRoomType(ROOM_TYPE roomType) {
        switch (roomType) {
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

    public void addOfficer(Officer officer){
        projOfficerList.add(officer);
    }

    public void setOfficerList(List<Officer> officerList){
        UserList officerListTemp = new UserList();
        officerList.forEach(officer -> officerListTemp.add(officer));
        this.projOfficerList = officerListTemp;
    }
}
