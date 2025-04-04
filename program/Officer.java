package program;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Officer extends User {
    public Officer(String NRIC, String name, int age, String marital_status) throws Exception{
        super(NRIC, name, age, marital_status);
    }
    public Officer(String NRIC, String name, int age, String marital_status, String password) throws Exception{
        super(NRIC, name, age, marital_status, password);
    }

    public AssignReqList getAssignReqList(){
        AssignReqList reqList = new AssignReqList();
        for (AssignReq req : Main.assignReqList){
            if (req.getOfficer() == this){
                reqList.add(req);
            }
        }
        return reqList;
    }
    public boolean assigned(){
        return this.getProject() != null;
    }

    public Project getProject(){
        for (Project project : Main.projectList){
            if (project.getOfficers().contains(this)){
                return project;
            }
        }
        return null;
    }

    public boolean overlapTime (Project targetProject){
        return overlapTime(targetProject,this.getProject());
    }
    public boolean overlapTime (Project proj1, Project proj2){
        if (proj1 == null || proj2 == null) return false;
        LocalDate openDate1 = proj1.getOpenDate();
        LocalDate openDate2 = proj2.getOpenDate();
        LocalDate closeDate1 = proj1.getCloseDate();
        LocalDate closeDate2 = proj2.getCloseDate();
        if (openDate1.isAfter(closeDate2) || closeDate1.isBefore(openDate2)) return false;
        else return true;
    }
    public boolean overlapTime (LocalDateTime dateTime){
        return overlapTime(dateTime.toLocalDate());
    }
    public boolean overlapTime (LocalDate date){
        LocalDate openDate = this.getProject().getOpenDate();
        LocalDate closeDate = this.getProject().getCloseDate();
        if (openDate.isBefore(date) && closeDate.isAfter(date)){
            return true;
        }else return false;
    }
}