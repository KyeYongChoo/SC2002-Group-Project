package program.boundary.menu;

import program.boundary.menuTemplate.MenuGroup;
import program.boundary.security.PasswordResetHandler;
import program.entity.users.User;

public class MainMenu extends MenuGroup{
    public MainMenu (User user){
        super(user.getGreeting());

        this.addMenuItem(new EnquiryMenu(user));
        this.addMenuItem(new ProjectApplicationMenu(user));
        this.addMenuItem(new ProjectManageMenu(user));
        this.addMenuItem(new OfficerApplyMenu(user));

        this.addMenuItem("Reset Password",
            () -> {
                System.out.println("Please enter your new password: ");
                PasswordResetHandler.resetPassword(user, sc.nextLine());
            }
        );

    }
}
