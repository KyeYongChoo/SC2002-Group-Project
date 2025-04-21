package program.control.enquiry;

import java.util.ArrayList;
import java.util.Scanner;

import program.boundary.console.AppScanner;
import program.boundary.console.DateTimeFormat;
import program.boundary.enquiryIO.EnquirySelector;
import program.boundary.projectIO.ProjectSelect;
import program.control.Main;
import program.entity.project.Project;
import program.entity.users.User;

/**
 * <p>
 * The {@code EnquiryList} class manages a collection of {@link Enquiry} objects and provides
 * functionality to select, add, and delete enquiries. It is used for managing enquiries created by
 * users regarding specific projects, as well as handling the interactions between users and project managers
 * or officers responding to those enquiries.
 * </p>
 *
 * <p>
 * This class is used by both applicants (users) and staff (managers or officers) to manage
 * the enquiries related to projects. It allows the user to select enquiries, view past enquiries,
 * and handle enquiry-related actions.
 * </p>
 *
 * @see program.control.enquiry.Enquiry
 * @see program.entity.users.User
 * @see program.entity.project.Project
 */
public class EnquiryList extends ArrayList<Enquiry> {

    /**
     * <p>
     * Retrieves an {@link Enquiry} based on the user and project. If multiple enquiries
     * exist for the same user and project, the method handles disambiguation.
     * </p>
     *
     * @param client the {@link User} who created the enquiry
     * @param project the {@link Project} the enquiry is related to
     * @return the {@link Enquiry} associated with the client and project, or null if none exists
     */
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

    /**
     * <p>
     * Allows an applicant to select an enquiry based on a project. This method prints out the
     * available projects for the user to choose from and then selects the relevant enquiry.
     * </p>
     *
     * @param client the {@link User} who created the enquiry
     * @return the selected {@link Enquiry}
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

    /**
     * <p>
     * Allows a manager or officer to select an enquiry based on a project. The method lists all
     * enquiries related to the given project, including the date of creation and the first message,
     * for the user to choose the relevant enquiry.
     * </p>
     *
     * @param project the {@link Project} to select an enquiry from
     * @return the selected {@link Enquiry}
     */
    public static Enquiry selectEnquiry(Project project) {
        Scanner sc = AppScanner.getInstance();
        System.out.println("Please choose project to enquire about: ");
        EnquiryList enqList = project.getEnquiryList();
        for (int loopCounter = 0; loopCounter < enqList.size(); loopCounter++) {
            Enquiry enquiry = enqList.get(loopCounter);
            System.out.println("\n" + (loopCounter + 1) + ".\nCreated on " + enquiry.getDateCreated().format(DateTimeFormat.getDateFormatter()));
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

    /**
     * <p>
     * Handles disambiguation if there are multiple enquiries for the same user and project.
     * This method presents the user with a list of matching enquiries to select from.
     * </p>
     *
     * @param client the {@link User} who created the enquiry
     * @param project the {@link Project} the enquiry is related to
     * @return the selected {@link Enquiry}
     */
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

    /**
     * <p>
     * Adds a new enquiry to the list and updates the corresponding project and user enquiry lists.
     * The new enquiry is added to the beginning of each list.
     * </p>
     *
     * @param enquiry the {@link Enquiry} to add
     * @return true if the enquiry was successfully added
     */
    @Override
    public boolean add(Enquiry enquiry) {
        enquiry.getProject().getEnquiryList().add(0, enquiry);
        enquiry.getUser().getEnquiryList().add(0, enquiry);
        Main.enquiryList.add(0, enquiry);
        return true;
    }

    /**
     * <p>
     * Prints all past enquiries made by the given user. Each enquiry is displayed with its creation
     * date, associated project, and all related messages.
     * </p>
     *
     * @param client the {@link User} whose past enquiries are to be printed
     */
    public static void printPastEnq(User client) {
        if (client.getEnquiryList().equals(new EnquiryList())) {
            System.out.println("You have no enquiries yet.");
            return;
        }

        for (Enquiry enquiry : client.getEnquiryList()) {
            System.out.println("\nCreated on " + enquiry.getDateCreated().format(DateTimeFormat.getDateTimeFormatter()));
            System.out.println("For project " + enquiry.getProject());
            for (Message m : enquiry) {
                System.out.println(m.getTimeStamp().format(DateTimeFormat.getDateTimeFormatter()) + ": " + m.getUser() + " (" + m.getUser().getClass().getSimpleName() + "): " + m.getText());
            }
        }
    }

    /**
     * <p>
     * Adds a new enquiry created by the specified client for the specified project, with the
     * provided enquiry text.
     * </p>
     *
     * @param client the {@link User} creating the enquiry
     * @param enquiryText the text of the enquiry message
     * @param project the {@link Project} the enquiry is related to
     * @return true if the enquiry was successfully added
     */
    public boolean add(User client, String enquiryText, Project project) {
        Enquiry enq = new Enquiry(client, enquiryText, project);
        return this.add(enq);
    }

    /* </p>
     *
     * @param e the {@link Enquiry} to add
     */
    public void superAdd(Enquiry e) {
        super.add(e);
    }

    /**
     * <p>
     * Deletes the specified enquiry from the project and user enquiry lists, as well as from
     * the main enquiry list.
     * </p>
     *
     * @param enq the {@link Enquiry} to delete
     */
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
