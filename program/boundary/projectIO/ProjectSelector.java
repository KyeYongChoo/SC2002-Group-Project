package program.boundary.projectIO;

import program.control.Main;
import program.control.TimeCompare;
import program.entity.project.Project;
import program.entity.users.Officer;
import program.entity.users.User;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


/**
 * <p>
 * The {@code ProjectSelector} class provides functionality for users to select {@link Project}
 * objects from a given list based on various criteria, including visibility, availability of unit types,
 * conflict of interest, and officer slot availability. It interacts with the user through the console
 * to facilitate their project selection.
 * </p>
 */
public class ProjectSelector {

    /**
     * <p>
     * The static {@code sc} instance of the {@link Scanner} class is used to read user input
     * from the console during project selection.
     * </p>
     */
    private static final Scanner sc = new Scanner(System.in);

    /**
     * <p>
     * Allows a client to choose a project from the list of visible projects. It filters the project list
     * to include only those projects the client is permitted to see (via {@link Project#isVisibleTo(User)}),
     * and then presents the filtered list for selection via the console.
     * </p>
     *
     * @param client      the {@link User} choosing a project.
     * @param projectList the list of all available {@link Project} objects.
     * @return the {@link Project} selected by the client, or {@code null} if no valid projects are available.
     */
    public static Project chooseVisibleProject(User client, List<Project> projectList) {
        // <p>Filter projects that the client is allowed to see.</p>
        List<Project> visibleProjects = UserPrefSorting.userFilterStream(client,projectList)
            .filter(project -> project.isVisibleTo(client))
            .collect(Collectors.toList());
        // <p>Present the filtered list of visible projects to the client for selection.</p>
        return chooseFromList(client, visibleProjects, "Choose a project visible to you:");
    }

    /**
     * <p>
     * Allows a client to choose a project to apply for, ensuring that the project has available units
     * matching the client's preferences and does not present a conflict of interest (determined by
     * {@link Project#conflictInterest(User)}).
     * </p>
     *
     * @param client      the {@link User} applying for a project.
     * @param projectList the list of all available {@link Project} objects.
     * @return the {@link Project} selected by the client, or {@code null} if no suitable project is found.
     */
    public static Project chooseProjectsApplyAsApplicant(User client, List<Project> projectList) {
        // <p>Filter projects based on unit type preferences, visibility, and conflict of interest.</p>
        List<Project> visibleProjects = UserPrefSorting.userFilterStream(client,projectList)
            .filter(project -> 
            ((client.see2Rooms() && project.getUnits2Room() > 0) || 
            (client.see3Rooms() && (project.getUnits3Room() > 0 || project.getUnits2Room() > 0 ))))
            .filter(project -> project.isVisibleTo(client))
            .filter(project -> !project.conflictInterest(client))
            .collect(Collectors.toList());

        // <p>Present the filtered list of projects for selection.</p>
        return chooseFromList(client, visibleProjects, "Choose a project visible to you without conflict of interest:");
    }

    /**
     * <p>
     * Allows an {@link Officer} to choose a project to apply to as a HDB Officer. The officer's preferences
     * are considered, as well as the availability of officer slots, existing assignment requests, and any time
     * conflicts (using {@link TimeCompare#officerUnassigned(Officer, Project)}).
     * </p>
     *
     * @param client      the {@link Officer} applying for a project.
     * @param projectList the list of all available {@link Project} objects.
     * @return the {@link Project} selected by the officer, or {@code null} if no suitable project is found.
     */
    public static Project chooseProjectsApplyAsOfficer(Officer client, List<Project> projectList) {
        // DEBUG
        // System.out.println("Initial number of projects: " + projectList.size());

        // <p>Filter projects based on officer preferences, available slots, and conflict-free status.</p>
        List<Project> visibleProjects = UserPrefSorting.userFilterStream(client, projectList)
            // Debug: Print the number of projects after user preference filtering
            // .peek(project -> System.out.println("Filtered by user preferences: " + project.getName()))
            // Filter projects with available officer slots
            .filter(project -> project.getOfficerSlots() > 0) // Filter projects with available officer slots
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

        // <p>Present the filtered list of projects to the officer for selection.</p>
        return chooseFromList(client, visibleProjects, "Choose a project to apply to as a HDB Officer:");
    }

    /**
     * <p>
     * Presents a list of projects to the given {@code client} for selection via the console.
     * Each project in the list is displayed with its details (using {@link ProjectPrinter#printProjectDetails(Project, boolean)}),
     * and the client is prompted to select a project by entering the corresponding number.
     * </p>
     *
     * @param client   the {@link User} selecting from the list of projects.
     * @param projects the list of {@link Project} objects to choose from.
     * @param prompt   the message displayed to the user before the selection list.
     * @return the {@link Project} selected by the client, or {@code null} if no valid selection is made.
     */
    private static Project chooseFromList(User client, List<Project> projects, String prompt) {
        // <p>Check if the list is empty and inform the user if no projects are available.</p>
        if (projects.isEmpty()) {
            System.out.println("No projects available.");
            return null;
        }

        System.out.println(prompt);
        // <p>Display each project's details and number for selection.</p>
        for (int i = 0; i < projects.size(); i++) {
            System.out.printf("\n%d. ", i + 1);
            ProjectPrinter.printProjectDetails(projects.get(i), client.see3Rooms());
        }

        int choice = -1;
        // <p>Prompt the user to enter their choice, ensuring valid input.</p>
        do {
            System.out.print("\nEnter the number of your choice: ");
            try {
                choice = Integer.parseInt(sc.nextLine()) - 1;  // Adjust input to 0-based index
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        } while (choice < 0 || choice >= projects.size());

        // <p>Return the selected project from the list.</p>
        return projects.get(choice);
    }
}
