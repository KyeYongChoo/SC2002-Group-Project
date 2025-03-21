package Program;
import Program.User.VISIBILITY;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Project {
    private String name;
    private String neighbourhood;
    int units2room;
    int units2roomPrice;
    int units3room;
    int units3roomPrice;
    private LocalDate openDate;
    private LocalDate closeDate;
    private Manager manager;
    private int officerSlots;
    private List<Officer> OfficerList = new ArrayList<>();

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
            OfficerList.add(officer);
        }

        
    }
    
    public void printVisible(User client){
        printVisible(client.getVisibility());
    }

    public void printVisible(VISIBILITY visibility){
        boolean firstLoop = true;
        for (Officer officer : OfficerList){
            if (firstLoop){
                firstLoop = false;
                if (visibility == VISIBILITY.Room2){
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
                else if (visibility == VISIBILITY.RoomAll){
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
                else if (visibility == VISIBILITY.RoomNone){
                    break;
                }
            }
            else{
                System.out.printf(
                    MainActivity.formatTableRef,"", "", "", "", "", "", "", "", "", officer);
              }
            }
            
        }

    public static void printApplied(User client){

    }

    public String getNeighbourhood(){
        return neighbourhood;
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
        return OfficerList;
    }

    @Override
    public String toString(){
        return name;
    }
}
