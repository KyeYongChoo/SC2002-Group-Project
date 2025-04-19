package program.boundary.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.time.LocalDate;

import program.boundary.console.DateTimeFormat;
import program.boundary.menuTemplate.MenuGroup;
import program.boundary.menuTemplate.MenuNavigator;
import program.boundary.menuTemplate.SelectionMenu;
import program.boundary.officerAssignIO.OfficerAssignSelector;
import program.boundary.projectIO.ProjectPrinter;
import program.boundary.projectIO.SetUpProject;
import program.control.Main;
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

public class ProjectManageMenu extends MenuGroup {
    private Project selectedProjectForReport = null;

    public ProjectManageMenu(User user) {
        super("Manage your Projects", 
            // only officers and above may manage projects
            user_ -> user_ instanceof Officer);

        this.addMenuItem("View/Approve incoming Officer requests", () -> {
            Manager manager = (Manager) user;
            AssignReq req = OfficerAssignSelector.selectByProject(manager.getCurProject());
            if (req == null) return;
            if (req.getApplicationStatus() != AssignReq.APPLICATION_STATUS.applied){
                System.out.println("Request has already been accepted/rejected. ");
                return;
            }
            OfficerAssignSelector.selectAcceptOrReject(req);
            // Manager and above
        }, user_ -> user_ instanceof Manager);
        
        this.addSelectionMenu(
            "Approve HDB Applications", 
            user_ -> user_ instanceof Manager,
            () -> Main.housingReqList.stream()
                .filter(req -> req.getProject().getManager().equals(user))
                // .peek(req -> System.out.println("Reqsame manager: " + req))
                .filter(req -> req.getStatus() == HousingReq.REQUEST_STATUS.pending)
                // .peek(req -> System.out.println("Req pending status: " + req))
                .collect(Collectors.toList()), 
            HousingReq::toString, 
            req -> {
                        boolean hasVacancy = req.getProject().getVacancy(req.getRoomType()) > 0;
                        String menuName = hasVacancy
                            ? "Please choose to accept or reject this application"
                            : "Sorry, not enough vacancy to accept application";
                        
                        List<String> choices = hasVacancy
                            ? List.of("Accept", "Reject")
                            : List.of("Reject");

                        MenuNavigator.getInstance().pushMenu(new SelectionMenu<>(
                            menuName,
                            () -> choices,
                            String::toString,
                            strChoice -> {
                                if (strChoice.equals("Accept")){
                                    req.getProject().decrementRoomType(req.getRoomType());
                                    req.setApprovedBy((Manager) user);
                                    req.setStatus(HousingReq.REQUEST_STATUS.successful);
                                }else if (strChoice.equals("Reject")){
                                    req.setApprovedBy((Manager) user);
                                    req.setStatus(HousingReq.REQUEST_STATUS.unsuccessful);
                                }
                            }
                        ).setTransient(true)
                    );
            }
        );

        this.addSelectionMenu("Process Withdrawal Requests"
            , user_ -> user_ instanceof Manager
            , () -> Main.housingReqList.stream()
                .filter(project -> project.getManager().equals(user))
                .filter(req -> req.getWithdrawalStatus().equals(HousingReq.WITHDRAWAL_STATUS.requested))
                .collect(Collectors.toList())
            , HousingReq::toString
            , req -> MenuNavigator.getInstance().pushMenu(new SelectionMenu<>(
                            "Please choose to accept or reject this withdrawal",
                            () -> List.of("Accept", "Reject"),
                            String::toString,
                            strChoice -> {
                                if (strChoice.equals("Accept")){
                                    req.setApprovedBy(null);
                                    req.setBookedBy(null);
                                    if (req.getStatus().equals(HousingReq.REQUEST_STATUS.successful))
                                        req.getProject().incrementRoomType(req.getRoomType());
                                    req.setStatus(HousingReq.REQUEST_STATUS.unsuccessful);
                                    req.setWithdrawalStatus(WITHDRAWAL_STATUS.approved);
                                }else if (strChoice.equals("Reject")){
                                    req.setWithdrawalStatus(WITHDRAWAL_STATUS.rejected);
                                }
                            }
                        ).setTransient(true))
        );

        this.addSelectionMenu("Help applicant book", 
            user_ -> user_ instanceof Officer && !(user_ instanceof Manager),
            () -> Main.housingReqList.stream()
                .filter(req -> req.getStatus().equals(HousingReq.REQUEST_STATUS.successful))
                .filter(req -> req.getProject().getOfficers().contains(user))
                .collect(Collectors.toList()), 
            HousingReq::toString, 
            req -> {
                req.setBookedBy((Officer) user);
                req.setStatus(HousingReq.REQUEST_STATUS.booked);
            }
        );

        this.addSelectionMenu("Generate Receipt of Applicant Booking"
            , user_ -> user_ instanceof Officer && !(user_ instanceof Manager)
            , () -> Main.housingReqList.stream()
                .filter(req -> req.getProject().getOfficers().contains((Officer) user))
                .filter(req -> req.getStatus().equals(HousingReq.REQUEST_STATUS.booked))
                .collect(Collectors.toList())
            , HousingReq::toString
            , req -> {
                System.out.println(req.toString());
                ProjectPrinter.printProjectDetails(req.getProject(), req.getUser().see3Rooms());;
            }
        );

        this.addMenuItem(new MenuGroup("Manage BTO Project Listings"
            , user_ -> user_ instanceof Manager && 
                Main.projectList.stream().anyMatch(project -> 
                project.isManager((Manager) user_))
            )
            .addMenuItem("Create Project Listing", new SetUpProject(user))
            .addSelectionMenu("Edit Project Listing",
                () -> Main.projectList.stream()
                    .filter(project -> project.isManager((Manager) user))
                    .collect(Collectors.toList()),
                Project::toString,
                project -> MenuNavigator.getInstance().pushMenu(createProjectEditMenu(project))
            )
            

            .addSelectionMenu("Delete Project Listing", 
                () -> Main.projectList.stream()
                    .filter(project -> project.isManager((Manager) user))
                    .collect(Collectors.toList()), 
                Project::toString, 
                project -> {
                    Main.projectList.remove(project);
                    System.out.println("Project Deleted");
                }
            )

            .addSelectionMenu("Toggle Visibility of Project Listing", 
                () -> Main.projectList.stream()
                    .filter(project -> project.isManager((Manager) user))
                    .collect(Collectors.toList()), 
                project -> project.toString() 
                    + (project.getVisibility() ? " (Visible)" : " (Not visible)"), 
                project -> {
                    project.setVisibility(!project.getVisibility());
                    System.out.println("Visibility Toggled");
                }
        ));

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
                Main.projectList.stream().anyMatch(project -> project.isManager((Manager) user_)),
            () -> List.of(REPORT_FILTER.values()),
            reportFilter -> ((Manager) user).getReportFilter().equals(reportFilter) ?
                reportFilter.toString() + " <-- Current" :
                reportFilter.toString(),
            reportFilter -> {
                ((Manager) user).setReportFilter(reportFilter);
                if (reportFilter == REPORT_FILTER.PROJECT) {
                    // Push the project selection menu
                    MenuNavigator.getInstance().pushMenu(selectProjectMenu);
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
        this.addMenuItem("Generate report on Applicants", () -> MenuNavigator.getInstance().pushMenu(generateReportMenu));
    }

    // the problem is, the content gets refreshed 
    private MenuGroup createProjectEditMenu(Project project) {
        // Create a new MenuGroup with the current project details
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
                project.setOpenDate(LocalDate.parse(newOpenDate, DateTimeFormat.getDateFormatter()));
                System.out.println("Application open date updated successfully.");
        });
        editMenu.addMenuItem("Edit Application Close Date", () -> {
                System.out.println("Enter new application close date (dd/MM/yyyy):");
                String newCloseDate = sc.nextLine().trim();
                project.setCloseDate(LocalDate.parse(newCloseDate, DateTimeFormat.getDateFormatter()));
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
