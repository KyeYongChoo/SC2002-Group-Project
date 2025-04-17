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
import program.entity.users.Manager;
import program.entity.users.Officer;
import program.entity.users.User;
import program.entity.users.User.MARITAL_STATUS;

public class ProjectManageMenu extends MenuGroup {
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
                .filter(req -> req.getStatus() == HousingReq.REQUEST_STATUS.pending)
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
                        )
                    );
            }
        );

        this.addSelectionMenu("Process withdrawal Requests"
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
                        ))
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
            .addMenuItem("Create Project Listing", new SetUpProject((Manager) user))
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

            .addSelectionMenu("Toggle Visibility of project listing", 
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

        Function<Manager, List<User>> generateRelevantApplicants = 
            (Manager manager) -> {
                List<User> userList = new ArrayList<User>(Main.applicantList);
                userList.addAll(Main.officerList);
                return userList.stream()
                    .filter(user_ -> Main.housingReqList.stream()
                        .anyMatch(housingReq -> 
                            housingReq.getProject().isManager(manager) && 
                            housingReq.getUser().equals(user_)
                    )).collect(Collectors.toList());
            };

        this.addMenuItem("Generate report on Applicants", 
            () -> {
                List<User> relevantApplicants = generateRelevantApplicants.apply((Manager) user);

                System.out.println("=== Applicant Report ===");
                relevantApplicants.stream().forEach(applicant -> {
                    System.out.println("Name   : " + applicant.getName());
                    System.out.println("User type: " + applicant.getClass().getSimpleName());
                    System.out.println("Age              : " + applicant.getAge());
                    System.out.println("Marital Status   : " + (applicant.getMaritalStatus().equals(MARITAL_STATUS.Married) ? "Married" : "Single"));

                    // Get the housing requests for this applicant
                    List<HousingReq> applicantRequests = Main.housingReqList.stream()
                        .filter(req -> req.getUser().equals(applicant))
                        .collect(Collectors.toList());

                    if (applicantRequests.isEmpty()) {
                        System.out.println("No flat bookings found for this applicant.");
                    } else {
                        System.out.println("Flat Bookings:");
                        applicantRequests.forEach(req -> {
                            System.out.println("  - Project Name : " + req.getProject().getName());
                            System.out.println("    Flat Type    : " + req.getRoomType());
                            System.out.println("    Status       : " + req.getStatus());
                        });
                    }
                    System.out.println("----------------------------------------");
                });

                // Example filter: Generate a report for married applicants
                System.out.println("=== Married Applicants Report ===");
                relevantApplicants.stream()
                    .filter(user_ -> user_.getMaritalStatus().equals(MARITAL_STATUS.Married))
                    .forEach(applicant -> {
                        System.out.println("Applicant Name   : " + applicant.getName());
                        System.out.println("Age              : " + applicant.getAge());
                        System.out.println("Flat Choices:");
                        Main.housingReqList.stream()
                            .filter(req -> req.getUser().equals(applicant))
                            .forEach(req -> {
                                System.out.println("  - Project Name : " + req.getProject().getName());
                                System.out.println("    Flat Type    : " + req.getRoomType());
                            });
                        System.out.println("----------------------------------------");
                    });
            },
            user_ -> user_ instanceof Manager && 
                Main.projectList.stream().anyMatch(project -> project.isManager((Manager) user))
        );
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
}
