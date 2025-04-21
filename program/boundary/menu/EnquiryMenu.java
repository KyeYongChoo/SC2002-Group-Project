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

/**
 * <p>
 * The {@code EnquiryMenu} class provides a structured set of menu options for users
 * to manage {@link Enquiry} objects within the system. This includes functionalities
 * for creating, viewing, editing, deleting, and replying to enquiries.
 * </p>
 *
 * <p>
 * The menu options are customized based on the type of {@link User} (e.g., Applicant,
 * Officer, or Manager), ensuring that access to certain features is controlled according
 * to user roles. This design enhances security and usability by providing relevant
 * options based on the user's permissions.
 * </p>
 *
 * @see program.boundary.menuTemplate.MenuGroup
 * @see program.control.enquiry.Enquiry
 */
public class EnquiryMenu extends MenuGroup {

    /**
     * <p>
     * Constructs an {@link EnquiryMenu} for the specified {@link User} and sets up
     * various menu options that allow the user to manage enquiries. The available
     * options include:
     * </p>
     * <ul>
     *   <li>Create Enquiry: Allows users to create a new enquiry related to a project.</li>
     *   <li>View Enquiry: Enables users to view details of a selected enquiry.</li>
     *   <li>Edit Enquiry: Provides functionality for users to modify an existing enquiry.</li>
     *   <li>Delete Enquiry: Allows users to remove an enquiry from the system.</li>
     *   <li>Reply to Enquiry: Lets users respond to an existing enquiry.</li>
     * </ul>
     *
     * <p>
     * Access to these options is determined by the role of the user. For example,
     * Managers may have different permissions compared to Officers or Applicants.
     * This ensures that users can only perform actions that are appropriate for their
     * roles within the system.
     * </p>
     *
     * @param user the {@link User} interacting with the enquiry system, which determines
     *             the available menu options based on their role.
     */
    public EnquiryMenu(User user) {
        super("Manage Enquiries");

        // Menu option to create a new enquiry
        this.addMenuItem("Create Enquiry", () -> {
            Project targetProject = ProjectSelect.chooseVisibleProject(user);
            if (targetProject == null) {
                System.out.println("No valid project selected.");
                return;
            }
            System.out.println("Please enter your enquiry:");
            String enquiryText = sc.nextLine();
            Enquiry newEnquiry = new Enquiry(user, enquiryText, targetProject);
            Main.enquiryList.add(newEnquiry);
            System.out.println("\nEnquiry saved. \nTime: " + newEnquiry.get(0).getTimeStamp() + "\nMessage: " + newEnquiry.get(0).getText());
        }, user_ -> !(user_ instanceof Manager)); // Restricting access for Managers

        // Menu option to view an existing enquiry
        this.addMenuItem("View Enquiry", () -> {
            Enquiry selectedEnquiry = EnquirySelector.selectEnquiry(
                    user,
                    Main.enquiryList,
                    user.getEnquiryViewFilter());
            if (selectedEnquiry != null) {
                EnquiryPrinter.print(selectedEnquiry);
            }
        });

        this.addMenuItem("Edit Enquiry", () -> {
            // Use a predicate to filter only editable enquiries
            Enquiry selectedEnquiry = EnquirySelector.selectEnquiry(
                user,
                Main.enquiryList,
                user.getEnquiryEditDeleteFilter()
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
        }, user_ -> !(user_ instanceof Manager)); // Restricting access for Managers

        // Menu option to delete an existing enquiry
        this.addMenuItem("Delete Enquiry", () -> {
            Enquiry selectedEnquiry = EnquirySelector.selectEnquiry(
                    user,
                    Main.enquiryList,
                    user.getEnquiryEditDeleteFilter()
            );

            if (selectedEnquiry == null) {
                System.out.println("No deletable enquiries available.");
                return;
            }

            EnquiryList.delete(selectedEnquiry);
        }, user_ -> !(user_ instanceof Manager));

        // Menu option to reply to an existing enquiry
        this.addMenuItem("Reply to Enquiry", () -> {
            Enquiry selectedEnquiry = EnquirySelector.selectEnquiry(
                    user,
                    Main.enquiryList,
                    user.getEnquiryReplyFilter()
            );

            if (selectedEnquiry == null) return;

            System.out.println("Enter your reply:");
            String replyText = sc.nextLine();
            selectedEnquiry.add(user, replyText);
            System.out.println("Reply saved successfully.");
        }, user_ -> user_ instanceof Officer);// officer and Manager can reply to Enquiries but not Applicants
    }
}
