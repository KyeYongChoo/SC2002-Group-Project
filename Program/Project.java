package Program;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
    private List<Officer> projOfficerList = new ArrayList<>();
    private boolean visibility = true;
    private HousingReqList reqList = new HousingReqList();

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
        // Holy smokes upcasting Downcasting 101 down here
        this.manager = (Manager) MainActivity.managerList.getByName(manager);
        if(this.manager == null) throw new Exception("Manager not Found. Manager field for reference: "+ manager);
        try {
            this.officerSlots = Integer.parseInt(officerSlots);
        } catch (NumberFormatException e) {
            throw new Exception("Officer Slot isn't an Integer. Officer Slot field for reference: "+ officerSlots);
        }
        Officer officer;
        for (String officerStr : OfficerLstStrInput.split(",")){
            officer = (Officer) MainActivity.officerList.getByName(officerStr);
            if (officer == null) throw new Exception("Officer not found. Officer for reference: "+ officer);
            projOfficerList.add(officer);
        }   
    }

    public void setVisibility(Manager manager) throws Exception{
        if (manager != this.manager){
            throw new Exception("You are not the manager of this HDB! ");
        }

        String choice;
        do { 
            System.out.println("Please toggle visibility of " + name + " (Y/N)" +"\nCurrent Visibility: " + (visibility?"Y":"N"));
            Scanner sc = new Scanner(System.in);
            choice = sc.nextLine().toUpperCase();
        } while (!"Y".equals(choice) && !"N".equals(choice));
        this.visibility = ("Y".equals(choice));
    }

    public void printVisible(User client){
        if (client == null) return;
        // Manager needs separate logic because they see all rooms regardless of visibility
        if (client instanceof Manager){
            printVisible((Manager) client);
            return;
        }

        if (this.visibility == false) return;
        // Officer needs separate logic because of table formatting issues if sometimes show 3 room sometimes show 2 rooms only
        if (client instanceof Officer){
            printVisible((Officer) client);
            return;
        }

        boolean table3RoomFormatting = true;
        boolean firstLoop = true;
        for (Officer officer : projOfficerList){
            if (firstLoop){
                firstLoop = false;
                if (client.see2Rooms()){
                    table3RoomFormatting = false;
                    MainActivity.updateTableRef(table3RoomFormatting);
                    System.out.printf(MainActivity.formatTableRef, (Object[])MainActivity.tableHeaders);
                    System.out.printf(
                        MainActivity.formatTableRef,
                        name,
                        neighbourhood,
                        units2room,
                        units2roomPrice,
                        openDate,
                        manager,
                        officerSlots,
                        officer);
                }
                else if (client.see3Rooms()){
                    table3RoomFormatting = true;
                    MainActivity.updateTableRef(table3RoomFormatting);
                    System.out.printf(MainActivity.formatTableRef, (Object[])MainActivity.tableHeaders);
                    System.out.printf(
                        MainActivity.formatTableRef,
                        name,
                        neighbourhood,
                        units2room,
                        units2roomPrice,
                        units3room,
                        units3roomPrice,
                        openDate,
                        manager,
                        officerSlots,
                        officer);
                }
                else{
                    break;
                }
            }
            else{
                if (table3RoomFormatting) System.out.printf(MainActivity.formatTableRef,"", "", "", "", "", "", "", "", "", officer);
                else System.out.printf(MainActivity.formatTableRef,"", "", "", "", "", "", "", officer);
              }
            }  
    }
    
    public void printVisible(Officer officer){
        boolean table3RoomFormatting;
        table3RoomFormatting = true;
        MainActivity.updateTableRef(table3RoomFormatting);
        boolean firstLoop = true;
        for (Officer projOfficer : projOfficerList){
            if (firstLoop){
                firstLoop = false;
                if (projOfficerList.contains(officer) || officer.see3Rooms()){
                    System.out.printf(MainActivity.formatTableRef, (Object[])MainActivity.tableHeaders);
                    System.out.printf(
                        MainActivity.formatTableRef,
                        name,
                        neighbourhood,
                        units2room,
                        units2roomPrice,
                        units3room,
                        units3roomPrice,
                        openDate,
                        manager,
                        officerSlots,
                        projOfficer);
                }
                else if (officer.see2Rooms()){
                    System.out.printf(MainActivity.formatTableRef, (Object[])MainActivity.tableHeaders);
                    System.out.printf(
                        MainActivity.formatTableRef,
                        name,
                        neighbourhood,
                        units2room,
                        units2roomPrice,
                        "", // to fill in the 3roomPrice clause
                        "",
                        openDate,
                        manager,
                        officerSlots,
                        projOfficer);
                }
                else{
                    break;
                }
            }
            else{
                System.out.printf(
                    MainActivity.formatTableRef,"", "", "", "", "", "", "", "", "", projOfficer);
              }
            }  
    }

    public void printVisible(Manager manager){
        boolean table3RoomFormatting;
        table3RoomFormatting = true;
        MainActivity.updateTableRef(table3RoomFormatting);

        boolean firstLoop = true;
        for (Officer officer : projOfficerList){
            if (firstLoop){
                firstLoop = false;
                System.out.printf(MainActivity.formatTableRef, (Object[])MainActivity.tableHeaders);
                System.out.printf(
                    MainActivity.formatTableRef,
                    name,
                    neighbourhood,
                    units2room,
                    units2roomPrice,
                    units3room,
                    units3roomPrice,
                    openDate,
                    this.manager,
                    officerSlots,
                    officer);
            }
            else{
                System.out.printf(
                    MainActivity.formatTableRef,"", "", "", "", "", "", "", "", "", officer);
              }
            }  
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
    public List<Officer> getOfficers(){
        return projOfficerList;
    }

    @Override
    public String toString(){
        return name;
    }
}
