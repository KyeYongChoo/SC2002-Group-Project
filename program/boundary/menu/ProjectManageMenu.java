package program.boundary.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import program.boundary.console.DateTimeFormat;
import program.boundary.menuTemplate.MenuGroup;
import program.boundary.menuTemplate.MenuNavigator;
import program.boundary.menuTemplate.SelectionMenu;
import program.boundary.officerAssignIO.OfficerAssignSelector;
import program.boundary.projectIO.ProjectPrinter;
import program.boundary.projectIO.SetUpProject;
import program.control.Main;
import program.control.TimeCompare;
import program.control.housingApply.HousingReq;
import program.control.housingApply.HousingReq.WITHDRAWAL_STATUS;
import program.control.officerApply.AssignReq;
import program.entity.project.Project;
import program.entity.project.Project.ROOM_TYPE;
import program.entity.users.Manager;
import program.entity.users.Manager.REPORT_FILTER;
import program.entity.users.Officer;
import program.entity.users.User;
import program.entity.users.User.MARITAL_STATUS;

/**
 * {@code ProjectManageMenu} serves as the central menu for managing Housing Development Board (HDB) projects.
 * It offers a range of functionalities tailored to the user's role, either an Officer or a Manager.
 *
 * <p>This menu dynamically adapts its options based on the logged-in {@link User}. Managers gain access
 * to project administration tasks, officer request approvals, housing application management,
 * and report generation. Officers, on the other hand, can assist applicants with bookings and generate
 * booking receipts for projects they are assigned to.</p>
 *
 * <p>Key features include:</p>
 * <ul>
 * <li>Creating, editing, deleting, and toggling the visibility of BTO (Build-To-Order) project listings (Manager).</li>
 * <li>Viewing and approving incoming officer assignment requests for their projects (Manager).</li>
 * <li>Reviewing and deciding on HDB housing applications for their projects (Manager).</li>
 * <li>Processing withdrawal requests from applicants for their projects (Manager).</li>
 * <li>Facilitating booking of successful housing applications for their assigned projects (Officer).</li>
 * <li>Generating booking receipts for applicants of their assigned projects (Officer).</li>
 * <li>Generating comprehensive reports on applicants associated with their managed projects (Manager).</li>
 * </ul>
 *
 * <p>This class extends {@link MenuGroup}, inheriting its base menu structure and navigation capabilities.</p>
 *
 * @see program.boundary.menuTemplate.MenuGroup
 */
public class ProjectManageMenu extends MenuGroup {
    private Project selectedProjectForReport = null;

    /**
     * Constructs a {@code ProjectManageMenu} instance for a specific {@link User}.
     * The menu items are populated dynamically based on whether the user is an Officer or a Manager.
     *
     * @param user The {@link User} who is accessing this menu. The available options will be tailored
     * based on their role (Officer or Manager).
     */
    public ProjectManageMenu(User user) {
        // Call the constructor of the parent class (MenuGroup) with the menu title
        // and a condition to determine if this menu is relevant for the given user.
        // In this case, the base menu is always relevant if the user is an Officer.
        super("Manage your Projects", 
            // only officers and above may manage projects

            /*
             * Problem: For shallow-depth selectionMenus, extra visibleIf check can be tacked on easily enough
             * Help Applicant book and Generate Receipt of Applicant Booking appears under very specific scenarios. 
             * The Menu system isnt smart enough to look ahead several layers in advance, hence officers sometimes can see this menu even if
             * after instantiating the elements, it appears that none of the options are applicable 
             * 
             * user_ -> visibleIf.test(user_) &&
             * itemSupplier.get()!=null && 
             * !itemSupplier.get().isEmpty()
             * For standard MenuGroups, the items are often invisible as well. so need to pass keep looking down through the menus until a visible item is found, else terminate.
             * But looking down through arbitrary depth of menus need instantiation, which causes classCast issues if instantiate menus that were never meant to be visible
             * TA can u teach us how to do this pls pls pls. It still works well enough to pass all tests. 
             */ 
            user_ -> user_ instanceof Officer);

        this.addMenuItem("View projects", 
            () -> ProjectPrinter.printManagerViewAll((Manager) user), 
            user_ -> user_ instanceof Manager);

        // Menu item for Managers to view and approve incoming officer assignment requests.
        this.addMenuItem("View/Approve incoming Officer requests", () -> {
            // Cast the User to Manager to access Manager-specific functionalities.
            Manager manager = (Manager) user;
            // Use OfficerAssignSelector to find pending officer assignment requests for the manager's current project.
            AssignReq req = OfficerAssignSelector.selectToApproveReject(manager.getCurProject());
            // If no request is found, exit the menu item action.
            if (req == null) return;
            // Check if the request has already been processed.
            if (req.getApplicationStatus() != AssignReq.APPLICATION_STATUS.applied){
                System.out.println("Request has already been accepted/rejected. ");
                return;
            }
            // If the request is pending, allow the manager to accept or reject it.
            OfficerAssignSelector.selectAcceptOrReject(req);
        }, user_ -> user_ instanceof Manager); // This menu item is only visible to Managers.

        // Selection menu for Managers to approve HDB housing applications.
        this.addSelectionMenu(
                "Approve HDB Applications",
                user_ -> user_ instanceof Manager, // Only visible to Managers.
                () -> Main.housingReqList.stream()
                        // Filter housing requests for the projects managed by the current manager.
                        .filter(req -> req.getProject().getManager().equals(user))
                        // Filter for requests that are currently pending approval.
                        .filter(req -> req.getStatus() == HousingReq.REQUEST_STATUS.pending)
                        // Collect the filtered requests into a list.
                        .collect(Collectors.toList()),
                HousingReq::toString, // How each HousingReq object should be displayed in the menu.
                req -> { // Action to be performed when a HousingReq is selected.
                    // Check if there is vacancy for the requested room type in the project.
                    boolean hasVacancy = req.getProject().getVacancy(req.getRoomType()) > 0;
                    // Determine the menu name based on vacancy.
                    String menuName = hasVacancy
                            ? "Please choose to accept or reject this application"
                            : "Sorry, not enough vacancy to accept application";

                    // Define the choices available to the manager.
                    List<String> choices = hasVacancy
                            ? List.of("Accept", "Reject")
                            : List.of("Reject");

                    // Push a new SelectionMenu onto the navigation stack for accepting or rejecting the application.
                    MenuNavigator.getInstance().pushMenu(new SelectionMenu<>(
                            menuName,
                            () -> choices, // Provide the list of choices.
                            String::toString, // How each choice should be displayed.
                            strChoice -> { // Action based on the manager's choice.
                                if (strChoice.equals("Accept")){
                                    // Set the approving manager.
                                    req.setApprovedBy((Manager) user);
                                    // Update the status of the housing request to successful.
                                    req.setStatus(HousingReq.REQUEST_STATUS.successful);
                                } else if (strChoice.equals("Reject")){
                                    // If rejected, set the approving manager.
                                    req.setApprovedBy((Manager) user);
                                    // Update the status of the housing request to unsuccessful.
                                    req.setStatus(HousingReq.REQUEST_STATUS.unsuccessful);
                                }
                            }
                    ).setTransient(true));
                }
        );

        // Selection menu for Managers to process withdrawal requests from applicants.
        this.addSelectionMenu(
                "Process withdrawal Requests",
                user_ -> user_ instanceof Manager, // Only visible to Managers.
                () -> Main.housingReqList.stream()
                        // Filter requests for projects managed by the current manager.
                        .filter(req -> req.getProject().getManager().equals(user))
                        // Filter for requests where withdrawal has been requested.
                        .filter(req -> req.getWithdrawalStatus().equals(HousingReq.WITHDRAWAL_STATUS.requested))
                        // Collect the filtered requests.
                        .collect(Collectors.toList()),
                HousingReq::toString, // Display each HousingReq.
                req -> MenuNavigator.getInstance().pushMenu(new SelectionMenu<>(
                        "Please choose to accept or reject this withdrawal",
                        () -> List.of("Accept", "Reject"), // Choices for the manager.
                        String::toString,
                        strChoice -> { // Action based on the choice.
                            if (strChoice.equals("Accept")){
                                // If accepted, clear the approved and booked by fields.
                                req.setApprovedBy(null);
                                req.setBookedBy(null);
                                // If the original application was successful, increment the room vacancy.
                                if (req.getStatus().equals(HousingReq.REQUEST_STATUS.booked))
                                    req.getProject().incrementRoomType(req.getRoomType());
                                // Set the request status to unsuccessful due to withdrawal.
                                req.setStatus(HousingReq.REQUEST_STATUS.unsuccessful);
                                // Update the withdrawal status to approved.
                                req.setWithdrawalStatus(WITHDRAWAL_STATUS.approved);
                            } else if (strChoice.equals("Reject")){
                                // If rejected, update the withdrawal status.
                                req.setWithdrawalStatus(WITHDRAWAL_STATUS.rejected);
                            }
                        }
                ).setTransient(true))
        );

        // Selection menu for Officers (excluding Managers) to help applicants book a flat.
        this.addSelectionMenu(
                "Help applicant book",
                user_ -> user_ instanceof Officer && !(user_ instanceof Manager), // Only for Officers who are not Managers.
                () -> Main.housingReqList.stream()
                        // Filter for successful housing requests.
                        .filter(req -> req.getStatus().equals(HousingReq.REQUEST_STATUS.successful))
                        // Filter for requests belonging to projects that the current officer is assigned to.
                        .filter(req -> req.getProject().getOfficers().contains(user))
                        // Collect the filtered requests.
                        .collect(Collectors.toList()),
                HousingReq::toString, // Display each HousingReq.
                req -> { // Action when an application is selected for booking.
                    req.getProject().decrementRoomType(req.getRoomType());
                    // Set the current officer as the one who booked the flat.
                    req.setBookedBy((Officer) user);
                    // Update the status of the housing request to booked.
                    req.setStatus(HousingReq.REQUEST_STATUS.booked);
                }
        );

        // Selection menu for Officers (excluding Managers) to generate a receipt for a booked application.
        this.addSelectionMenu(
                "Generate Receipt of Applicant Booking",
                user_ -> user_ instanceof Officer && !(user_ instanceof Manager), // Only for Officers who are not Managers.
                () -> Main.housingReqList.stream()
                        // Filter for requests from projects the current officer is assigned to.
                        .filter(req -> req.getProject().getOfficers().contains((Officer) user))
                        // Filter for requests that have been successfully booked.
                        .filter(req -> req.getStatus().equals(HousingReq.REQUEST_STATUS.booked))
                        // Collect the filtered requests.
                        .collect(Collectors.toList()),
                HousingReq::toString, // Display each HousingReq.
                req -> { // Action to generate and print the receipt.
                    // Print the details of the housing request.
                    System.out.println(req.toString());
                    // Use ProjectPrinter to print the project details and the applicant's 3-room preference.
                    ProjectPrinter.printProjectDetails(req.getProject(), req.getUser().see3Rooms());
                }
        );

        // MenuGroup for managing BTO project listings (only for Managers who manage at least one project).
        this.addMenuItem(new MenuGroup(
                        "Manage BTO Project Listings",
                        user_ -> user_ instanceof Manager &&
                                Main.projectList.stream().anyMatch(project -> project.isManager((Manager) user))
                )
                        // Menu item to create a new project listing.
                        .addMenuItem("Create Project Listing", new SetUpProject(user))
                        // Selection menu to edit an existing project listing.
                        .addSelectionMenu(
                                "Edit Project Listing",
                                () -> Main.projectList.stream()
                                        // Filter for projects managed by the current manager.
                                        .filter(project -> project.isManager((Manager) user))
                                        // Collect the managed projects.
                                        .collect(Collectors.toList()),
                                Project::toString, // Display each Project.
                                project -> MenuNavigator.getInstance().pushMenu(createProjectEditMenu(project, (Manager) user)) // Push the project edit menu.
                        )
                        // Selection menu to delete a project listing.
                        .addSelectionMenu(
                                "Delete Project Listing",
                                () -> Main.projectList.stream()
                                        // Filter for projects managed by the current manager.
                                        .filter(project -> project.isManager((Manager) user))
                                        // Collect the managed projects.
                                        .collect(Collectors.toList()),
                                Project::toString, // Display each Project.
                                project -> { // Action to delete the selected project.
                                    Main.projectList.remove(project);
                                    System.out.println("Project Deleted");
                                }
                        )
                        // Selection menu to toggle the visibility of a project listing.
                        .addSelectionMenu(
                                "Toggle Visibility of project listing",
                                () -> Main.projectList.stream()
                                        // Filter for projects managed by the current manager.
                                        .filter(project -> project.isManager((Manager) user))
                                        // Collect the managed projects.
                                        .collect(Collectors.toList()),
                                project -> project.toString() + (project.getVisibility() ? " (Visible)" : " (Not visible)"), // Display project with visibility status.
                                project -> { // Action to toggle the visibility.
                                    project.setVisibility(!project.getVisibility());
                                    System.out.println("Visibility Toggled");
                                }
                        )
        );

        SelectionMenu<Project> selectProjectMenu = new SelectionMenu<>(
            "Select a Project for the Report",
            () -> Main.projectList.stream()
                .filter(project -> project.isManager((Manager) user)) // Only projects managed by the current manager
                .collect(Collectors.toList()),
            project -> project.equals(selectedProjectForReport) ? 
                project.toString() + " <-- Current" :
                project.toString(),
            project -> {
                selectedProjectForReport = project; // Store the selected project
                System.out.println("Selected project: " + project.getName());
            }
        );

        SelectionMenu<REPORT_FILTER> generateReportMenu = new SelectionMenu<>(
            "Generate Report on Applicants",
            user_ -> user_ instanceof Manager &&
                Main.projectList.stream().anyMatch(project -> project.isManager((Manager) user_)) &&
                !(generateRelevantApplicants.apply((Manager) user_).isEmpty()),
            () -> List.of(REPORT_FILTER.values()),
            reportFilter -> ((Manager) user).getReportFilter().equals(reportFilter) ?
                reportFilter.toString() + " <-- Current" :
                reportFilter.toString(),
            reportFilter -> {
                ((Manager) user).setReportFilter(reportFilter);
                if (reportFilter == REPORT_FILTER.PROJECT) {
                    // Push the project selection menu
                    MenuNavigator.getInstance().pushMenu(selectProjectMenu.setTransient(true));
                }
            }
        );

        generateReportMenu.setDynamicDesc(() -> {
            Manager manager = (Manager) user;
            List<User> relevantApplicants = generateRelevantApplicants.apply(manager);

            StringBuilder sb = new StringBuilder(); 
            sb.append("Generate report on Applicants");
            sb.append("\n=== Applicant Report ===\n");

            if (relevantApplicants.isEmpty()){
                sb.append("Sorry, no relevant applicants\n").toString();
            }else{
                relevantApplicants.stream().forEach(applicant -> {
                    sb.append("Name   : " + applicant.getName() + "\n");
                    sb.append("User type: " + applicant.getClass().getSimpleName() + "\n");
                    sb.append("Age              : " + applicant.getAge() + "\n");
                    sb.append("Marital Status   : " + (applicant.getMaritalStatus().equals(MARITAL_STATUS.Married) ? "Married\n" : "Single\n"));

                    // Get the housing requests for this applicant
                    List<HousingReq> applicantRequests = Main.housingReqList.stream()
                        .filter(req -> filterManagerReport(req, applicant, manager))
                        .collect(Collectors.toList());

                    if (applicantRequests.isEmpty()) {
                        sb.append("No flat bookings found for this applicant.\n");
                    } else {
                        sb.append("Flat Bookings:\n");
                        applicantRequests.forEach(req -> {
                            sb.append("  - Project Name : " + req.getProject().getName() + "\n");
                            sb.append("    Flat Type    : " + req.getRoomType() + "\n");
                            sb.append("    Status       : " + req.getStatus() + "\n");
                        });
                    }
                    sb.append("----------------------------------------\n");
                });
            }
            sb.append("\nPlease select your filter: \n");
            return sb.toString();
        });

        // dont add menu directly because it would cause the whole application report to be generated as description in ProjectManagerMenu
        this.addMenuItem("Generate report on Applicants", 
            () -> MenuNavigator.getInstance().pushMenu(generateReportMenu),
            (user_) -> user_ instanceof Manager);
    }

    /**
     * Creates a {@link MenuGroup} that allows a manager to edit various attributes of a specific {@link Project}.
     * The menu dynamically displays the current project details and updates them after each edit.
     *
     * @param project The {@link Project} object to be edited.
     * @return A {@link MenuGroup} containing menu items for editing project attributes.
     */
    private MenuGroup createProjectEditMenu(Project project, Manager manager) {
        MenuGroup editMenu = new MenuGroup("Which field do you want to edit? \n" +
                ProjectPrinter.getProjectDetailsString(project, true));
        editMenu.addMenuItem("Edit Project Name", () -> {
            System.out.println("Enter new project name:");
            String newName = sc.nextLine().trim();
            project.setName(newName);
            System.out.println("Project name updated successfully.");
        });
        editMenu.addMenuItem("Edit Neighborhood", () -> {
            System.out.println("Enter new neighborhood:");
            String newNeighborhood = sc.nextLine().trim();
            project.setNeighbourhood(newNeighborhood);
            System.out.println("Neighborhood updated successfully.");
        });
        editMenu.addMenuItem("Edit 2-Room Units", () -> {
            System.out.println("Enter new number of 2-room units:");
            int newUnits2Room = Integer.parseInt(sc.nextLine().trim());
            project.setUnits2Room(newUnits2Room);
            System.out.println("2-room units updated successfully.");
        });
        editMenu.addMenuItem("Edit 2-Room Price", () -> {
            System.out.println("Enter new price for 2-room units:");
            int newPrice2Room = Integer.parseInt(sc.nextLine().trim());
            project.setUnits2RoomPrice(newPrice2Room);
            System.out.println("2-room price updated successfully.");
        });
        editMenu.addMenuItem("Edit 3-Room Units", () -> {
            System.out.println("Enter new number of 3-room units:");
            int newUnits3Room = Integer.parseInt(sc.nextLine().trim());
            project.setUnits3Room(newUnits3Room);
            System.out.println("3-room units updated successfully.");
        });
        editMenu.addMenuItem("Edit 3-Room Price", () -> {
            System.out.println("Enter new price for 3-room units:");
            int newPrice3Room = Integer.parseInt(sc.nextLine().trim());
            project.setUnits3RoomPrice(newPrice3Room);
            System.out.println("3-room price updated successfully.");
        });
        editMenu.addMenuItem("Edit Application Open Date", () -> {
            System.out.println("Enter new application open date (dd/MM/yyyy):");
            String newOpenDate = sc.nextLine().trim();
            LocalDate targetDate;
            try {
                targetDate = LocalDate.parse(newOpenDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException e) {
                System.out.println("Date could not be resolved.");
                return;
            }

            // Check if the new Open Date is after the Close Date
            if (targetDate.isAfter(project.getCloseDate())) {
                System.out.println("Error: New Open Date (" + targetDate.format(DateTimeFormat.getDateFormatter()) +
                    ") cannot be after the Close Date (" + project.getCloseDate().format(DateTimeFormat.getDateFormatter()) + ").");
                return;
            }

            // Check for overlapping dates with other projects
            Main.projectList.stream()
                .filter(project_ -> project_.isManager(manager) &&
                    project_ != project &&
                    !(TimeCompare.timeSeparate(targetDate, project.getCloseDate(), project_.getOpenDate(), project_.getCloseDate())))
                .findAny()
                .ifPresent(project_ -> {
                    System.out.println("Date given intersects with " + project_ + 
                        "\nProject Open Date: " + project_.getOpenDate().format(DateTimeFormat.getDateFormatter()) + 
                        "\nProject End Date: " + project_.getCloseDate().format(DateTimeFormat.getDateFormatter()));
                    return;
                });

            project.setOpenDate(targetDate);
            System.out.println("Application open date updated successfully.");
        });
        editMenu.addMenuItem("Edit Application Close Date", () -> {
            System.out.println("Enter new application close date (dd/MM/yyyy):");
            String newCloseDate = sc.nextLine().trim();
            LocalDate targetDate;
            try {
                targetDate = LocalDate.parse(newCloseDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException e) {
                System.out.println("Date could not be resolved.");
                return;
            }
            Main.projectList.stream()
                .filter(project_ -> project_.isManager(manager) &&
                    project_ != project &&
                    !(TimeCompare.timeSeparate(project.getOpenDate(), targetDate, project_.getOpenDate(), project_.getCloseDate())))
                .findAny()
                .ifPresent(project_ -> {
                    System.out.println("Date given intersects with " + project_ + 
                        "\nProject Open Date: " + project_.getOpenDate().format(DateTimeFormat.getDateFormatter()) + 
                        "\nProject End Date: " + project_.getCloseDate().format(DateTimeFormat.getDateFormatter()));
                        return;
                });
            if (project.getOpenDate().isAfter(targetDate)){
                System.out.println("Error: Project Start Date: " + 
                    project.getOpenDate().format(DateTimeFormat.getDateFormatter()) + 
                    "\nis after Project End Date: " +
                    targetDate.format(DateTimeFormat.getDateFormatter()));
                return;
            }
            project.setCloseDate(targetDate);
            System.out.println("Application close date updated successfully.");
        });
        editMenu.addMenuItem("Edit Officer Slots", () -> {
            System.out.println("Enter new number of officer slots:");
            int newOfficerSlots = Integer.parseInt(sc.nextLine().trim());
            project.setOfficerSlots(newOfficerSlots);
            System.out.println("Officer slots updated successfully.");
        });
        editMenu.setDynamicDesc(() -> "Which field do you want to edit? \n" +
                ProjectPrinter.getProjectDetailsString(project, true));
        editMenu.setTransient(true);
        return editMenu;
    }
    
    private Function<Manager, List<User>> generateRelevantApplicants = 
            (Manager manager) -> {
                List<User> userList = new ArrayList<User>(Main.applicantList);
                userList.addAll(Main.officerList);
                userList = userList.stream()
                    .filter(user_ -> Main.housingReqList.stream()
                        .anyMatch(housingReq -> 
                            filterManagerReport(housingReq, user_, manager)
                    ))
                    .collect(Collectors.toList());

                return userList;
    };

    private boolean filterManagerReport(HousingReq req, User user, Manager manager) {
        REPORT_FILTER filter = manager.getReportFilter();

        // Check if the housing request belongs to the manager's project
        if (!req.getProject().isManager(manager)) {
            return false;
        }

        // Check if the housing request belongs to the applicant
        if (!req.getUser().equals(user)) {
            return false;
        }

        // Apply filters based on the manager's report filter
        switch (filter) {
            case MARRIED:
                return user.getMaritalStatus() == MARITAL_STATUS.Married;
            case SINGLE:
                return user.getMaritalStatus() == MARITAL_STATUS.Single;
            case PROJECT:
                return selectedProjectForReport != null &&
                       req.getProject().equals(selectedProjectForReport) &&
                       req.getUser().equals(user);
            case FLAT_TYPE_2_ROOM:
                return req.getRoomType() == ROOM_TYPE.room2 && req.getUser().equals(user);
            case FLAT_TYPE_3_ROOM:
                return req.getRoomType() == ROOM_TYPE.room3 && req.getUser().equals(user);
            default:
                return true; // No specific filter applied
        }
    }
}

