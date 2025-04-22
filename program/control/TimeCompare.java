package program.control;

import java.time.LocalDate;

import program.entity.project.Project;
import program.entity.users.Officer;

/**
 * The {@code TimeCompare} class contains utility methods for comparing project timelines
 * and determining officer assignments based on date conflicts.
 * <p>
 * Key functionalities include:
 * <ul>
 *   <li>Checking if an officer is unassigned to a project based on overlapping dates.</li>
 *   <li>Comparing the start and end dates of two projects to see if they overlap.</li>
 *   <li>Determining if a project is currently active based on its dates.</li>
 * </ul>
 * </p>
 *
 * @see Project for project details including dates.
 * @see Officer for officer details.
 */
public class TimeCompare {

    /**
     * Checks if an officer is unassigned to a project by ensuring there are no overlapping dates
     * between the officer's current projects and the target project.
     * <p>
     * This method filters all projects assigned to the officer and checks if any of them have
     * overlapping dates with the target project.
     * </p>
     *
     * @param officer The officer to check.
     * @param targetProject The project to check against.
     * @return {@code true} if the officer is unassigned to the target project (no date conflicts),
     *         {@code false} if there are any conflicts with the officer's existing assignments.
     */
    public static boolean officerUnassigned (Officer officer, Project targetProject){
        return Main.projectList.stream()
            .filter(project -> project.getOfficers().contains(officer))
            .allMatch(project -> projectTimingSeparate(project, targetProject));
    }

    /**
     * Compares the dates of two projects to determine if their timelines do not overlap.
     * <p>
     * This method checks if the open and close dates of two projects are separate.
     * If one project ends before the other starts or vice versa, the method returns {@code true}.
     * </p>
     *
     * @param proj1 The first project to compare.
     * @param proj2 The second project to compare.
     * @return {@code true} if the projects' timelines do not overlap,
     *         {@code false} if there is an overlap.
     */
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


    /**
     * Checks if a project is currently active by comparing its open and close dates with the current date.
     * <p>
     * This method checks if the project is ongoing, i.e., the current date lies between the project's open and close dates.
     * </p>
     *
     * @param proj The project to check.
     * @return {@code true} if the project is currently active,
     *         {@code false} if the project is closed or not yet opened.
     */
    public static boolean currentlyActive (Project proj){
        return proj.getOpenDate().isBefore(LocalDate.now()) && 
            proj.getCloseDate().isAfter(LocalDate.now());
    }
}
