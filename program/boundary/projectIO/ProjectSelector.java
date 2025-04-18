package program.boundary.projectIO;

import program.control.Main;
import program.control.TimeCompare;
import program.entity.project.Project;
import program.entity.users.Officer;
import program.entity.users.User;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ProjectSelector {

    private static final Scanner sc = new Scanner(System.in);

    public static Project chooseVisibleProject(User client, List<Project> projectList) {
        List<Project> visibleProjects = UserPrefSorting.userFilterStream(client,projectList)
            .filter(project -> project.isVisibleTo(client))
            .collect(Collectors.toList());

        return chooseFromList(client, visibleProjects, "Choose a project visible to you:");
    }

    public static Project chooseProjectsApplyAsApplicant(User client, List<Project> projectList) {
        List<Project> visibleProjects = UserPrefSorting.userFilterStream(client,projectList)
            .filter(project -> 
            ((client.see2Rooms() && project.getUnits2Room() > 0) || 
            (client.see3Rooms() && (project.getUnits3Room() > 0 || project.getUnits2Room() > 0 ))))
            .filter(project -> project.isVisibleTo(client))
            .filter(project -> !project.conflictInterest(client))
            .collect(Collectors.toList());

        return chooseFromList(client, visibleProjects, "Choose a project visible to you without conflict of interest:");
    }

    public static Project chooseProjectsApplyAsOfficer(Officer client, List<Project> projectList) {
        // DEBUG
        // System.out.println("Initial number of projects: " + projectList.size());

        // Apply user preference filters
        List<Project> visibleProjects = UserPrefSorting.userFilterStream(client, projectList)
            // Debug: Print the number of projects after user preference filtering
            // .peek(project -> System.out.println("Filtered by user preferences: " + project.getName()))
            // Filter projects with available officer slots
            .filter(project -> project.getOfficerSlots() > 0) 
            // .peek(project -> System.out.println("Filtered by officer slots: " + project.getName()))
            // Exclude projects where the officer already has an assignment request
            .filter(project -> Main.assignReqList.stream().noneMatch(assignReq -> 
                    assignReq.getOfficer().equals(client) 
                    && assignReq.getProject().equals(project)
                )
            )
            // Debug: Print the number of projects after officer slots filtering
            // .peek(project -> System.out.println("Filtered by overlapping requests: " + project.getName()))
            .filter(project -> TimeCompare.officerUnassigned(client, project))
            // Debug: Print the number of projects after TimeCompare filtering
            // .peek(project -> System.out.println("Filtered by timeCompare filtering: " + project.getName()))
            .collect(Collectors.toList());

        return chooseFromList(client, visibleProjects, "Choose a project to apply to as a HDB Officer:");
    }

    private static Project chooseFromList(User client, List<Project> projects, String prompt) {
        if (projects.isEmpty()) {
            System.out.println("No projects available.");
            return null;
        }

        System.out.println(prompt);
        for (int i = 0; i < projects.size(); i++) {
            System.out.printf("\n%d. ", i + 1);
            ProjectPrinter.printProjectDetails(projects.get(i), client.see3Rooms());
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