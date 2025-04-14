package program.boundary.officerAssignIO;

import program.control.housingApply.HousingReq;
import program.control.housingApply.HousingReqList;
import program.entity.project.Project;
import program.entity.users.Officer;
import program.entity.users.User;

public class OfficerAssignPrinter {

    public static void printOfficerProfile(Officer officer) {
        System.out.println("=== Officer Profile ===");
        System.out.println("Name         : " + officer.getName());
        System.out.println("NRIC         : " + officer.getUserId());
        System.out.println("Age          : " + officer.getAge());
        System.out.println("Marital Status: " + officer.getMaritalStatus());
    }

    public static void printReceipt(HousingReq application) {
        System.out.println("=== Receipt ===");
        System.out.println("Name         : " + application.getUser().getName());
        System.out.println("NRIC         : " + application.getUser().getUserId());
        System.out.println("Age          : " + application.getUser().getAge());
        System.out.println("Marital Status: " + application.getUser().getMaritalStatus());
        System.out.println("Flat Type Booked: " + application.getRoomType());
        System.out.println("Project Name: " + application.getProject().getName());
        System.out.println("Project Location: " + application.getProject().getNeighbourhood());
    }

    public static void printEnquiries(Project project) {
        System.out.println("=== Enquiries for Project: " + project.getName() + " ===");
        project.getEnquiryList().forEach(enquiry -> {
            System.out.println("Enquiry ID: " + enquiry.getId() + " | Author: " + enquiry.getUser().getName() + " | Content: " + enquiry.get(0).getText());
        });
    }

    public static void printProjectDetails(Project project) {
        System.out.println("=== Project Details ===");
        System.out.println("Project Name     : " + project.getName());
        System.out.println("Neighborhood     : " + project.getNeighbourhood());
        System.out.println("Available 2-Room : " + project.getUnits2Room());
        System.out.println("Available 3-Room : " + project.getUnits3Room());
    }

    public static void printPastApplications(User client, HousingReqList reqList) {
        if (reqList.isEmpty()) {
            System.out.println("You have not applied for any room yet.");
            return;
        }

        for (int i = 0; i < reqList.size(); i++) {
            HousingReq req = reqList.get(i);
            Project project = req.getProject();
            if (i == 0 && !client.hasActiveApplication()) {
                System.out.println("\nUnsuccessful Applications\n");
            }
            if (i == 0 && client.hasActiveApplication()) {
                if (req.getWithdrawalStatus() == HousingReq.WITHDRAWAL_STATUS.requested) {
                    System.out.println("\n(Processing Withdrawal Request)");
                }
                System.out.println("Active Application\n");
            }
            if (i == 1 && !client.hasActiveApplication()) {
                System.out.println("\nUnsuccessful Applications\n");
            }

            System.out.println("Project: " + project.getName() + " | Flat Type: " + req.getRoomType() + " | Status: " + req.getStatus());
        }
    }
}
