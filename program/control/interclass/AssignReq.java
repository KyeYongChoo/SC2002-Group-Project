package program.control.interclass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import program.entity.project.Project;
import program.entity.users.Manager;
import program.entity.users.Officer;

public class AssignReq {
    private LocalDate startDate;
    private LocalDate endDate;
    private Officer officer;
    private Manager manager;
    private Project project;
    private APPLICATION_STATUS applicationStatus = APPLICATION_STATUS.applied;
    public enum APPLICATION_STATUS{
        applied,
        rejected,
        accepted;
    }

    private static LocalDate parseLocalDate(String Date){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
            return LocalDate.parse(Date, formatter);
        } catch (DateTimeParseException e) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-M-yyyy");
            return LocalDate.parse(Date, formatter);
        }

    }

    public AssignReq(Officer officer, Project project){
        this.officer = officer;
        this.project = project;
        this.manager = project.getManager();
        this.startDate = project.getOpenDate();
        this.endDate = project.getCloseDate();
    }

    public APPLICATION_STATUS getApplicationStatus(){
        return applicationStatus;
    }

    public Officer getOfficer(){
        return officer;
    }
    public Manager getManager(){
        return manager;
    }
    public Project getProject(){
        return project;
    }
}
