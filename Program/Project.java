package Program;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Project {
    private String projectName;
    private String neighbourhood;
    private FLAT_TYPE flatType;
    private int units;
    private LocalDate openDate;
    private LocalDate closeDate;
    private Manager manager;
    private int officerSlots;
    private Officer[] OfficerList;

    public static enum FLAT_TYPE {
        Room2,
        Room3
    };

    public Project(String projectName, String neighbourhood, FLAT_TYPE flatType, int units, String openDate, String closeDate, Manager manager, int officerSlots) throws Exception{
        this.projectName = projectName.trim().toUpperCase();
        this.neighbourhood = neighbourhood.trim().toUpperCase();
        this.flatType = flatType;
        this.units = units;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("DD/MM/YYYY");
        this.openDate = LocalDate.parse(openDate, formatter);
        this.closeDate = LocalDate.parse(closeDate, formatter);
        this.manager = manager;
        this.officerSlots = officerSlots;
    }
    
    public void printVisible(User client){
        if (!client.getVisibility()){
            System.out.println("")
            //Continue here
        }
    }

    public int getUnits(){
        return units;
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
}
