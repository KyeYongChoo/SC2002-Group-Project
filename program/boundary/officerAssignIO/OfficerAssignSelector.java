package program.boundary.officerAssignIO;

import java.util.Scanner;
import program.boundary.console.AppScanner;
import program.control.housingApply.HousingReq;
import program.control.housingApply.HousingReqList;
import program.entity.project.Project;
import program.entity.users.Officer;

public class OfficerAssignSelector {
    private static final Scanner sc = AppScanner.getInstance();

    public static HousingReq selectApplication(Officer officer, HousingReqList reqList) {
        if (reqList.isEmpty()) {
            System.out.println("No applications available.");
            return null;
        }

        System.out.println("Select an application:");
        for (int i = 0; i < reqList.size(); i++) {
            HousingReq req = reqList.get(i);
            System.out.printf("%d. Applicant: %s | Project: %s | Status: %s\n",
                i + 1, req.getUser().getName(), req.getProject().getName(), req.getStatus());
        }

        int choice = -1;
        do {
            System.out.print("Enter the number of your choice: ");
            try {
                choice = Integer.parseInt(sc.nextLine()) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        } while (choice < 0 || choice >= reqList.size());

        return reqList.get(choice);
    }

    public static Project selectProject(Officer officer) {
        System.out.println("Select a project you are assigned to:");
        for (int i = 0; i < officer.getAssignReqList().size(); i++) {
            Project project = officer.getAssignReqList().get(i).getProject();
            System.out.printf("%d. %s\n", i + 1, project.getName());
        }

        int choice = -1;
        do {
            System.out.print("Enter the number of your choice: ");
            try {
                choice = Integer.parseInt(sc.nextLine()) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        } while (choice < 0 || choice >= officer.getAssignReqList().size());

        return officer.getAssignReqList().get(choice).getProject();
    }
}