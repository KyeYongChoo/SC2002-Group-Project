package program.boundary.enquiryIO;

import java.util.Scanner;
import java.util.function.Predicate;

import program.boundary.console.AppScanner;
import program.control.enquiry.Enquiry;
import program.control.enquiry.EnquiryList;
import program.entity.users.User;

/**
 * <p>
 * The {@code EnquirySelector} class provides functionality for users to select
 * an enquiry from a filtered list based on specific conditions. It enhances
 * user interaction by allowing for dynamic filtering of enquiries, making it
 * easier for users to find relevant enquiries.
 * </p>
 *
 * <p>
 * This class utilizes {@link program.boundary.enquiryIO.EnquiryPrinter} for
 * displaying enquiries in a formatted manner and leverages
 * {@link java.util.function.Predicate} for flexible enquiry filtering. This
 * design allows users to specify custom conditions for filtering enquiries,
 * improving the overall usability of the application.
 * </p>
 *
 */
public class EnquirySelector {
    private static final Scanner sc = AppScanner.getInstance();

    /**
     * <p>
     * Allows a user to select an enquiry from a list that has been filtered
     * using a specified condition. The method first creates a copy of the
     * original enquiry list, applying the provided filter to populate the
     * copy with matching enquiries.
     * </p>
     *
     * <p>
     * If no enquiries match the filter, a message is displayed, and {@code null}
     * is returned. If matching enquiries are found, the user is prompted to
     * select an enquiry by entering a valid number corresponding to the
     * displayed list.
     * </p>
     *
     * <p>
     * After the user makes a selection, the method checks if the user has
     * permission to view the selected enquiry. If the user lacks permission,
     * a message is displayed, and {@code null} is returned.
     * </p>
     *
     * @param user the user attempting to select an enquiry
     * @param enqList the list of all available enquiries
     * @param enquiryFilter a predicate used to filter enquiries before selection
     * @return the selected {@link program.control.enquiry.Enquiry} if successful; {@code null} otherwise
     * @see program.boundary.enquiryIO.EnquiryPrinter#printEnquiryList(User, EnquiryList)
     * @see program.control.enquiry.EnquiryList
     */
    public static Enquiry selectEnquiry(User user, EnquiryList enqList, Predicate<Enquiry> enquiryFilter) {
        EnquiryList enqListCpy = new EnquiryList();
        enqList.stream().filter(enquiryFilter).forEach(enquiry -> enqListCpy.superAdd(enquiry));
        if (enqListCpy.isEmpty()) {
            System.out.println("No enquiries available.");
            return null;
        }

        EnquiryPrinter.printEnquiryList(user, enqListCpy);

        System.out.println("\nPlease choose which enquiry to select: ");
        int choice = -1;
        do {
            try {
                choice = Integer.parseInt(sc.nextLine()) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        } while (choice < 0 || choice >= enqListCpy.size());

        Enquiry selectedEnquiry = enqListCpy.get(choice);
        if (!EnquiryPrinter.canViewEnquiry(user, selectedEnquiry)) {
            System.out.println("You do not have permission to view this enquiry.");
            return null;
        }

        return selectedEnquiry;
    }
}