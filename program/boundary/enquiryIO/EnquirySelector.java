package program.boundary.enquiryIO;

import java.util.Scanner;
import java.util.function.Predicate;

import program.boundary.console.AppScanner;
import program.control.interclass.Enquiry;
import program.control.interclass.EnquiryList;
import program.entity.users.Manager;
import program.entity.users.Officer;
import program.entity.users.User;

public class EnquirySelector {
    private static final Scanner sc = AppScanner.getInstance();


    // Should not be used by anyone but manager, who looks though everything
    public static Enquiry selectEnquiry(User user, EnquiryList enqList) {
        return selectEnquiry(user, enqList, dummy -> true);
    }
    public static Enquiry selectEnquiry(User user, EnquiryList enqList, Predicate<Object> filter) {
        EnquiryList enqListCpy = new EnquiryList();
        enqList.stream().filter(filter).forEach(enquiry -> enqListCpy.superAdd(enquiry));
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

    public static boolean canEditOrDeleteEnquiry(User user, Enquiry enquiry) {
        return enquiry.getUser().equals(user) && !enquiry.isStaffReplyPresent();
    }

    public static boolean canReplyToEnquiry(User user, Enquiry enquiry) {
        if (user instanceof Manager){
            return enquiry.getProject().getManager().equals(user);
        }
        if (user instanceof Officer) {
            return enquiry.getProject().getOfficers().contains(user);
        }
        return false; // Applicants cannot reply
    }
}