package program.boundary.enquiryIO;

import java.time.format.DateTimeFormatter;
import program.control.interclass.Enquiry;
import program.control.interclass.EnquiryList;
import program.entity.users.Manager;
import program.entity.users.Officer;
import program.entity.users.User;

public class EnquiryPrinter {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public static void print(Enquiry enquiry) {
        System.out.println("Created on " + enquiry.getDateCreated().format(formatter));
        System.out.println("For project " + enquiry.getProject());
        System.out.println("First Message: " + enquiry.get(0).getText());
    }

    public static void printEnquiryList(User user, EnquiryList enqList) {
        for (int i = 0; i < enqList.size(); i++) {
            Enquiry enquiry = enqList.get(i);
            if (canViewEnquiry(user, enquiry)) {
                System.out.println("\n" + (i + 1) + ".\nCreated on " + enquiry.getDateCreated().format(formatter));
                System.out.println("For project " + enquiry.getProject());
                System.out.println("First Message: " + enquiry.get(0).getText());
            }
        }
    }

    public static boolean canViewEnquiry(User user, Enquiry enquiry) {
        if (user instanceof Manager) {
            return true; // Managers can view all enquiries
        }
        if (user instanceof Officer officer) {
            return enquiry.getUser().equals(user) || enquiry.getProject().equals(officer.getCurProject());
        }
        // Applicants can only view enquiries they created
        return enquiry.getUser().equals(user);
    }
}