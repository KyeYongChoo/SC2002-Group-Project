package program.control;

import java.time.LocalDate;

import program.entity.project.Project;
import program.entity.users.Officer;

public class TimeCompare {
    /*
     * Officer is unassigned to a project if the officer is not assigned to any project that has overlapping dates with the target project.
     * This is used to check if the officer can be assigned to the target project without any date conflicts.
     * @param officer The officer to check
     * @param targetProject The project to check against
     * @return true if the officer is unassigned to the target project, false otherwise
     */
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
        return timeSeparate(openDate1,closeDate1,openDate2,closeDate2);
    }

    public static boolean timeSeparate (LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2){
        return start1.isAfter(end2) || end1.isBefore(start2); 
    }

    public static boolean currentlyActive (Project proj){
        return proj.getOpenDate().isBefore(LocalDate.now()) && 
            proj.getCloseDate().isAfter(LocalDate.now());
    }
}
