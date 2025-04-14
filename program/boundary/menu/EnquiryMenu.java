package program.boundary.menu;

import program.boundary.enquiryIO.EnquiryPrinter;
import program.boundary.enquiryIO.EnquirySelector;
import program.boundary.menuTemplate.MenuGroup;
import program.boundary.projectIO.ProjectSelect;
import program.control.Main;
import program.control.enquiry.Enquiry;
import program.control.enquiry.EnquiryList;
import program.entity.project.Project;
import program.entity.users.Manager;
import program.entity.users.Officer;
import program.entity.users.User;

public class EnquiryMenu extends MenuGroup {
    public EnquiryMenu(User user) {
        super("Manage Enquiries");

        this.addMenuItem("Create Enquiry", () -> {
            Project targetProject = ProjectSelect.chooseVisibleProjectWithoutConflict(user);
            if (targetProject == null) {
                System.out.println("No valid project selected.");
                return;
            }
            System.out.println("Please enter your enquiry:");
            String enquiryText = sc.nextLine();
            Enquiry newEnquiry = new Enquiry(user, enquiryText, targetProject);
            Main.enquiryList.add(newEnquiry);
            System.out.println("\nEnquiry saved. \nTime: " + newEnquiry.get(0).getTimeStamp() + "\nMessage: " + newEnquiry.get(0).getText());
        }, user_ -> !(user_ instanceof Manager));

        this.addMenuItem("View Enquiry", () -> {
            Enquiry selectedEnquiry = EnquirySelector.selectEnquiry(user, Main.enquiryList, enquiry -> ((Enquiry) enquiry).getUser().equals(user));
            if (selectedEnquiry != null) {
                EnquiryPrinter.print(selectedEnquiry);
            }
        });

        this.addMenuItem("Edit Enquiry", () -> {
            // Use a predicate to filter only editable enquiries
            Enquiry selectedEnquiry = EnquirySelector.selectEnquiry(
                user,
                Main.enquiryList,
                enquiry -> EnquirySelector.canEditOrDeleteEnquiry(user, (Enquiry) enquiry)
            );

            if (selectedEnquiry == null) {
                System.out.println("No editable enquiries available.");
                return;
            }

            System.out.println("Enter the new text for the enquiry:");
            String newText = sc.nextLine();
            if (selectedEnquiry.editMessage(0, newText)) {
                System.out.println("Enquiry updated successfully.");
            } else {
                System.out.println("Failed to update enquiry.");
            }
        }, user_ -> !(user_ instanceof Manager));

        this.addMenuItem("Delete Enquiry", () -> {
            // Use a predicate to filter only deletable enquiries
            Enquiry selectedEnquiry = EnquirySelector.selectEnquiry(
                user,
                Main.enquiryList,
                enquiry -> EnquirySelector.canEditOrDeleteEnquiry(user, (Enquiry) enquiry)
            );

            if (selectedEnquiry == null) {
                System.out.println("No deletable enquiries available.");
                return;
            }

            EnquiryList.delete(selectedEnquiry);
        }, user_ -> !(user_ instanceof Manager));

        this.addMenuItem("Reply to Enquiry", () -> {
            Enquiry selectedEnquiry = EnquirySelector.selectEnquiry(
                user,
                Main.enquiryList,
                enquiry -> EnquirySelector.canReplyToEnquiry(user, (Enquiry) enquiry)
            );

            if (selectedEnquiry == null) {
                System.out.println("No enquiries available for replying.");
                return;
            }

            System.out.println("Enter your reply:");
            String replyText = sc.nextLine();
            selectedEnquiry.add(user, replyText);
            System.out.println("Reply saved successfully.");
        }, user_ -> !(user_ instanceof Officer));// officer and Manager can reply to Enquiries but not Applicants
    }
}
