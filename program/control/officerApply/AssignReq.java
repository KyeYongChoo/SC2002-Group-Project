package program.control.officerApply;

import program.boundary.console.DateTimeFormat;
import program.control.TimeCompare;
import program.entity.project.Project;
import program.entity.users.Manager;
import program.entity.users.Officer;

public class AssignReq {
    private Officer officer;
    private Manager manager;
    private Project project;
    private APPLICATION_STATUS applicationStatus = APPLICATION_STATUS.applied;
    public enum APPLICATION_STATUS{
        applied,
        rejected,
        accepted;
    }

    public void setApplicationStatus(APPLICATION_STATUS applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public AssignReq(Officer officer, Project project){
        this.officer = officer;
        this.project = project;
        this.manager = project.getManager();
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Assignment Request Details ===\n");
        sb.append("Officer: ").append(officer.getName()).append("\n");
        sb.append("Project: ").append(project.getName()).append("\n");
        sb.append("Manager: ").append(manager.getName()).append("\n");
        sb.append("Status: ").append(applicationStatus).append("\n");
        sb.append("Active: ");
        if (TimeCompare.currentlyActive(project)) {
            sb.append("Yes\n");
            sb.append("From: ").append(project.getOpenDate().format(DateTimeFormat.getDateFormatter())).append("\n");
            sb.append("To: ").append(project.getCloseDate().format(DateTimeFormat.getDateFormatter())).append("\n");
        } else {
            sb.append("No\n");
        }
        return sb.toString();
    }
}
