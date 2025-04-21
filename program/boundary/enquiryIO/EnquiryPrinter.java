


package program.boundary.enquiryIO;

import program.boundary.console.DateTimeFormat;
import program.control.enquiry.Enquiry;
import program.control.enquiry.EnquiryList;
import program.control.enquiry.Message;
import program.entity.users.Manager;
import program.entity.users.Officer;
import program.entity.users.User;

/**
 * <p>
 * The {@code EnquiryPrinter} class provides utility methods for printing details
 * about enquiries and enquiry lists to the console in a formatted manner. It is
 * designed to enhance the user experience by presenting information clearly and
 * in an organized way.
 * </p>
 *
 * <p>
 * This class includes methods to print individual enquiries, which display
 * relevant details such as creation timestamps, associated projects, and
 * messages. Additionally, it can print lists of enquiries that a specific user
 * is permitted to view, filtering the enquiries based on user roles and
 * permissions.
 * </p>
 *
 * <p>
 * For more information on the data structures used, please refer to
 * {@link program.control.enquiry.Enquiry} and
 * {@link program.control.enquiry.EnquiryList}.
 * </p>
 */
public class EnquiryPrinter {

    /**
     * <p>
     * Prints the details of a single enquiry, including its creation timestamp,
     * the related project, and all associated messages. The output is formatted
     * for clarity, showing the time of each message, the user who sent it, and
     * the message text.
     * </p>
     *
     * @param enquiry the enquiry object to be printed
     * @see program.control.enquiry.Enquiry
     */
    public static void print(Enquiry enquiry) {
        System.out.println("Created on " + enquiry.getDateCreated().format(DateTimeFormat.getDateTimeFormatter()));
        System.out.println("For project " + enquiry.getProject());
        for (Message m : enquiry) {
            System.out.println("\nTime: " + m.getTimeStamp().format(DateTimeFormat.getDateTimeFormatter()) +
                    "\n(" + (m.getUser ().getClass().getSimpleName()) + ") " + m.getUser () + ": " +
                    "\n" + m.getText());
        }
    }

    /**
     * <p>
     * Prints a list of enquiries that a specific user is permitted to view.
     * For each enquiry, the creation date, related project, first message,
     * and creator are displayed. The method filters the enquiries based on
     * the user's permissions, ensuring that only accessible enquiries are shown.
     * </p>
     *
     * @param user the user requesting to view the enquiry list
     * @param enqList the list of enquiries to be filtered and printed
     * @see #canViewEnquiry(User, Enquiry)
     */
    public static void printEnquiryList(User user, EnquiryList enqList) {
        for (int i = 0; i < enqList.size(); i++) {
            Enquiry enquiry = enqList.get(i);
            if (canViewEnquiry(user, enquiry)) {
                System.out.println("\n" + (i + 1) + ".\nCreated on " + enquiry.getDateCreated().format(DateTimeFormat.getDateTimeFormatter()));
                System.out.println("For project " + enquiry.getProject());
                System.out.println("First Message: " + enquiry.get(0).getText());
                System.out.println("Created By: " + enquiry.getUser () + " (" + enquiry.getUser ().getClass().getSimpleName() + ") ");
            }
        }
    }

    /**
     * <p>
     * Determines whether a user has permission to view a specific enquiry.
     * Managers can view all enquiries, Officers can view enquiries they created
     * or those related to their current project, and Applicants can only view
     * enquiries they created themselves. This method enforces access control
     * based on user roles.
     * </p>
     *
     * @param user the user attempting to view the enquiry
     * @param enquiry the enquiry in question
     * @return {@code true} if the user can view the enquiry, {@code false} otherwise
     * @see program.entity.users.Manager
     * @see program.entity.users.Officer
     */
    public static boolean canViewEnquiry(User user, Enquiry enquiry) {
        if (user instanceof Manager) {
            return true;
        }
        if (user instanceof Officer officer) {
            return enquiry.getUser ().equals(user) || enquiry.getProject().equals(officer.getCurProject());
        }
        return enquiry.getUser ().equals(user);
    }
}
