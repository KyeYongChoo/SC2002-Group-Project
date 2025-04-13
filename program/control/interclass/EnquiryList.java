package program.control.interclass;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import program.boundary.console.AppScanner;
import program.boundary.enquiryIO.EnquirySelector;
import program.boundary.projectIO.ProjectSelect;
import program.control.Main;
import program.entity.project.Project;
import program.entity.users.User;

/*
 * Similar to HousingReqList, there will be a separate enquiryList for each of the User, Project, and Main classes
 */
public class EnquiryList extends ArrayList<Enquiry> {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public static Enquiry get(User client, Project project) {
        Enquiry targetEnquiry = null;
        for (Enquiry enquiry : Main.enquiryList) {
            if (enquiry.getUser() == client && enquiry.getProject() == project) {
                if (targetEnquiry == null) {
                    targetEnquiry = enquiry;
                } else {
                    targetEnquiry = getDisambiguation(client, project);
                    break;
                }
            }
        }
        return targetEnquiry;
    }

    /*
     * This function is used to select an enquiry from a user.
     * Used by Applicant to select projects to enquire on.
     * @param client The client who created the enquiry.
     * @return The selected enquiry.
     */
    public static Enquiry selectEnquiry(User client) {
        Scanner sc = AppScanner.getInstance();
        System.out.println("Please choose project to enquire about: ");
        ProjectSelect.printVisible(client);
        Project targetProject = null;
        targetProject = Main.projectList.get(sc.nextLine());
        while (targetProject == null) {
            System.out.println("Please input the project name only: ");
            targetProject = Main.projectList.get(sc.nextLine());
        }
        return get(client, targetProject);
    }

    /*
     * This function is used to select an enquiry from a project.
     * Used by Manager and Officer to select enquiries to respond to.
     * @param project The project to select an enquiry from.
     * @return The selected enquiry.
     */
    public static Enquiry selectEnquiry(Project project) {
        Scanner sc = AppScanner.getInstance();
        System.out.println("Please choose project to enquire about: ");
        EnquiryList enqList = project.getEnquiryList();
        for (int loopCounter = 0; loopCounter < enqList.size(); loopCounter++) {
            Enquiry enquiry = enqList.get(loopCounter);
            System.out.println("\n" + (loopCounter + 1) + ".\nCreated on " + enquiry.getDateCreated().format(formatter));
            System.out.println("For project " + enquiry.getProject());
            System.out.println("First Message: " + enquiry.get(0).getText());
        }
        System.out.println("\n\nPlease choose which is the relevant enquiry: ");
        int choice = 0;
        do {
            try {
                choice = Integer.parseInt(sc.nextLine()) - 1;
            } catch (Exception e) {
                continue;
            }
        } while (choice < 0 || choice >= enqList.size());
        return enqList.get(choice);
    }

    public static Enquiry getDisambiguation(User client, Project project) {
        EnquiryList enqList = new EnquiryList();
        for (Enquiry enquiry : Main.enquiryList) {
            if (enquiry.getUser() == client && enquiry.getProject() == project) {
                enqList.superAdd(enquiry);
            }
        }
        return EnquirySelector.selectEnquiry(client, Main.enquiryList, enquiry -> 
        ((Enquiry) enquiry).getUser() == client && ((Enquiry) enquiry).getProject() == project);
    }

    @Override
    public boolean add(Enquiry enquiry) {
        enquiry.getProject().getEnquiryList().add(0, enquiry);
        enquiry.getUser().getEnquiryList().add(0, enquiry);
        Main.enquiryList.add(0, enquiry);
        return true;
    }

    public static void printPastEnq(User client) {
        if (client.getEnquiryList().equals(new EnquiryList())) {
            System.out.println("You have no enquiries yet.");
            return;
        }

        for (Enquiry enquiry : client.getEnquiryList()) {
            System.out.println("\nCreated on " + enquiry.getDateCreated().format(formatter));
            System.out.println("For project " + enquiry.getProject());
            for (Message m : enquiry) {
                System.out.println(m.getTimeStamp().format(formatter) + ": " + m.getUser() + " (" + m.getUser().getClass().getSimpleName() + "): " + m.getText());
            }
        }
    }

    public boolean add(User client, String enquiryText, Project project) {
        Enquiry enq = new Enquiry(client, enquiryText, project);
        return this.add(enq);
    }

    // Used if need to modify in place ie avoid java.util.ConcurrentModificationException
    public void superAdd(Enquiry e) {
        super.add(e);
    }

    public static void delete(Enquiry enq) {
        if (enq == null) {
            System.out.println("Error: no enquiries found.");
            return;
        }
        enq.getProject().getEnquiryList().remove(enq);
        enq.getUser().getEnquiryList().remove(enq);
        Main.enquiryList.remove(enq);
        System.out.println("\nRemoval Successful");
    }
}
