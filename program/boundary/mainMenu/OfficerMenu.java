package program.boundary.mainMenu;

import java.util.Scanner;

import program.boundary.console.AppScanner;
import program.boundary.menu.PrintVisible;
import program.boundary.menuTemplate.MenuGroup;
import program.control.Main;
import program.entity.project.Project;
import program.entity.project.ProjectList;
import program.entity.users.Officer;

public class OfficerMenu extends MenuGroup {
    private final Scanner sc = AppScanner.getInstance();
    public OfficerMenu(Officer officer) {
        super("Officer Menu" + " (" + officer.getName() + ")");

        addMenuItem(new PrintVisible(officer));

        addMenuItem(new ApplyProject(officer));

        addMenuItem("View project you have applied for, including application status", () -> {
            officer.printPastReq();
        });

        addMenuItem("Request application withdrawal", () -> {
            Main.reqList.reqWithdrawal(officer);
        });

        addMenuItem("Create, view, delete enquiries", () -> {
            // Logic for handling enquiries
        });

        addMenuItem("Register to join a project", () -> {
            Project targetProject = null;
            Main.projectList.printVisible(officer);
            do {
                System.out.println("Please enter name of project: ");
                targetProject = Main.projectList.get(sc.nextLine());
            } while (targetProject == null);

            Main.assignReqList.add(officer, targetProject);
        });

        addMenuItem("Change password", () -> {
            System.out.println("Please enter your new password: ");
            String newPassword = sc.nextLine();
            officer.setPassword(newPassword);
            System.out.println("Password changed successfully.");
        });

        addMenuItem("Log out", () -> {
            System.out.println("Logging out...");
        });
    }
}
