package program.control.interclass;

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
}
