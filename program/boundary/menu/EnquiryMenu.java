package program.boundary.menu;

import program.boundary.menuTemplate.MenuGroup;
import program.boundary.menuTemplate.MenuItem;
import program.control.interclass.Enquiry;
import program.entity.project.ProjectList;
import program.entity.users.User;

public class EnquiryMenu extends MenuGroup {
    public EnquiryMenu(User user) {
        super("Create, view, reply, delete enquiries");

        this.addMenuItem("Create Enquiry", 
            () -> {   
                System.out.println("Please choose project to enquire about: ");
                ProjectList.printVisible(officer);
                targetProject = null;
                while(targetProject == null){
                    targetProject = projectList.get(sc.nextLine());
                } 
                System.out.println("Please enter your enquiry");
                Enquiry newEnquiry = new Enquiry(officer,sc.nextLine(),targetProject);
                enquiryList.add(newEnquiry);
                System.out.println("\nEnquiry saved. \nTime: " + newEnquiry.get(0).getTimeStamp() + "\nMessage: " + newEnquiry.get(0).getText());
        }));
        MenuItem viewEnquiry = new MenuItem("View Enquiry", () -> {
            // Logic for viewing an enquiry
        });
        MenuItem replyEnquiry = new MenuItem("Reply Enquiry", () -> {
            // Logic for replying to an enquiry
        });
        MenuItem deleteEnquiry = new MenuItem("Delete Enquiry", () -> {
            // Logic for deleting an enquiry
        });
        this.addMenuItem(createEnquiry);
    }
}
