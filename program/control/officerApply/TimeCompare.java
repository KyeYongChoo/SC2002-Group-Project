package program.control.officerApply;

import java.time.LocalDate;

import program.control.Main;
import program.entity.project.Project;
import program.entity.users.Officer;

public class TimeCompare {
    public static boolean officerUnassigned (Officer officer, Project targetProject){
        return Main.projectList.stream()
            .filter(project -> project.getOfficers().contains(officer))
            .allMatch(project -> projectTimingSeparate(project, targetProject));
    }

    public static boolean projectTimingSeparate (Project proj1, Project proj2){
        if (proj1 == null || proj2 == null) return false;
        LocalDate openDate1 = proj1.getOpenDate();
        LocalDate openDate2 = proj2.getOpenDate();
        LocalDate closeDate1 = proj1.getCloseDate();
        LocalDate closeDate2 = proj2.getCloseDate();
        if (openDate1.isAfter(closeDate2) || closeDate1.isBefore(openDate2)) return false;
        else return true;
    }
}
