package program.boundary.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import program.boundary.menuTemplate.MenuGroup;
import program.boundary.security.PasswordResetHandler;
import program.entity.users.Manager;
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

        List<User.FILTER_SETTING> filterOptions = new ArrayList<>(Arrays.asList(User.FILTER_SETTING.values()));
        if (!(user instanceof Manager)) {
            filterOptions.remove(User.FILTER_SETTING.OWN_PROJECTS_ONLY);
        }

        this.addTransientSelectionMenu(
            "Filter options",
            dummyVar -> true,
            () -> (user instanceof Manager)? Arrays.asList(User.FILTER_SETTING.values()):
            filterOptions, 
            filterSetting -> user.getFilterSetting().equals(filterSetting)?
                filterSetting.toString() + " <-- Current":
                filterSetting.toString(),
            user::setFilterSetting);

    }
}
