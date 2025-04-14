package program.boundary.enquiryIO;

import java.util.Scanner;
import java.util.function.Predicate;

import program.boundary.console.AppScanner;
import program.control.enquiry.Enquiry;
import program.control.enquiry.EnquiryList;
import program.entity.users.User;

public class EnquirySelector {
    private static final Scanner sc = AppScanner.getInstance();

    public static Enquiry selectEnquiry(User user, EnquiryList enqList, Predicate<Object> enquiryFilter) {
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