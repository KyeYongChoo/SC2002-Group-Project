package program.boundary.enquiryIO;


import program.boundary.console.DateTimeFormat;
import program.control.enquiry.Enquiry;
import program.control.enquiry.EnquiryList;
import program.control.enquiry.Message;
import program.entity.users.Manager;
import program.entity.users.Officer;
import program.entity.users.User;

public class EnquiryPrinter {

    public static void print(Enquiry enquiry) {
        System.out.println("Created on " + enquiry.getDateCreated().format(DateTimeFormat.getDateTimeFormatter()));
        System.out.println("For project " + enquiry.getProject());
        for (Message m : enquiry){
            System.out.println("\nTime: " + m.getTimeStamp().format(DateTimeFormat.getDateTimeFormatter()) + 
                "\n(" + (m.getUser().getClass().getSimpleName())  + ") " + m.getUser() + ": " +
                "\n" + m.getText());

        }
    }

    public static void printEnquiryList(User user, EnquiryList enqList) {
        for (int i = 0; i < enqList.size(); i++) {
            Enquiry enquiry = enqList.get(i);
            if (canViewEnquiry(user, enquiry)) {
                System.out.println("\n" + (i + 1) + ".\nCreated on " + enquiry.getDateCreated().format(DateTimeFormat.getDateTimeFormatter()));
                System.out.println("For project " + enquiry.getProject());
                System.out.println("First Message: " + enquiry.get(0).getText());
                System.out.println("Created By: " + enquiry.getUser() + " (" + enquiry.getUser().getClass().getSimpleName() + ") ");
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