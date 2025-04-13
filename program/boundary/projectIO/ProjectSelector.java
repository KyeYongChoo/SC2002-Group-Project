package program.boundary.projectIO;

import program.entity.project.Project;
import program.entity.users.Officer;
import program.entity.users.User;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ProjectSelector {

    private static final Scanner sc = new Scanner(System.in);

    public static Project chooseVisibleProject(User client, List<Project> projectList) {
        List<Project> visibleProjects = projectList.stream()
            .filter(project -> project.isVisibleTo(client))
            .sorted((p1, p2) -> ProjectFilter.compareProjects(p1, p2, client.getFilterSetting())) // Sort based on filter setting
            .filter(project -> ProjectFilter.filterByFlatType(project, client.getFilterSetting())) // Additional filtering for flat types
            .collect(Collectors.toList());

        return chooseFromList(client, visibleProjects, "Choose a project visible to you:");
    }

    public static Project chooseVisibleProjectWithoutConflict(User client, List<Project> projectList) {
        List<Project> visibleProjects = projectList.stream()
            .filter(project -> 
            ((client.see2Rooms() && project.getUnits2Room() > 0) || 
            (client.see3Rooms() && project.getUnits3Room() > 0)))
            .filter(project -> project.isVisibleTo(client))
            .filter(project -> ProjectFilter.filterByFlatType(project, client.getFilterSetting())) // Additional filtering for flat types
            .sorted((p1, p2) -> ProjectFilter.compareProjects(p1, p2, client.getFilterSetting())) // Sort based on filter setting
            .filter(project -> !project.conflictInterest(client))
            .collect(Collectors.toList());

        return chooseFromList(client, visibleProjects, "Choose a project visible to you without conflict of interest:");
    }

    public static Project chooseProjectForOfficer(Officer officer, List<Project> projectList) {
        List<Project> officerProjects = projectList.stream()
            .filter(project -> ProjectFilter.filterByFlatType(project, officer.getFilterSetting())) // Additional filtering for flat types
            .sorted((p1, p2) -> ProjectFilter.compareProjects(p1, p2, officer.getFilterSetting())) // Sort based on filter setting
            .filter(project -> !project.getOfficers().contains(officer))
            .filter(project -> !officer.overlapTime(project))
            .collect(Collectors.toList());

        return chooseFromList(officer, officerProjects, "Choose a project:");
    }

    private static Project chooseFromList(User client, List<Project> projects, String prompt) {
        if (projects.isEmpty()) {
            System.out.println("No projects available.");
            return null;
        }

        System.out.println(prompt);
        for (int i = 0; i < projects.size(); i++) {
            System.out.printf("\n%d. ", i + 1);
            ProjectPrinter.printVisible(client, projects.get(i));
        }

        int choice = -1;
        do {
            System.out.print("\nEnter the number of your choice: ");
            try {
                choice = Integer.parseInt(sc.nextLine()) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        } while (choice < 0 || choice >= projects.size());

        return projects.get(choice);
    }
}