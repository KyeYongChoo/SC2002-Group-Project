package program.boundary.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import program.boundary.menuTemplate.MenuGroup;
import program.boundary.menuTemplate.MenuNavigator;
import program.boundary.security.PasswordResetHandler;
import program.entity.users.Manager;
import program.entity.users.User;

/**
 * <p>
 * The {@code MainMenu} class provides the primary navigation interface for users
 * after they have successfully logged into the system. It serves as a central hub
 * for accessing various functionalities and sub-menus related to user operations.
 * </p>
 *
 * <p>
 * This menu aggregates access to several sub-menus, including
 * {@link EnquiryMenu}, {@link ProjectApplicationMenu}, {@link ProjectManageMenu},
 * and {@link OfficerApplyMenu}. Additionally, it offers options for users to reset
 * their passwords and configure filter settings based on their roles within the system.
 * This design enhances user experience by providing a clear and organized way to navigate
 * through the application.
 * </p>
 *
 * @see program.boundary.menuTemplate.MenuGroup
 */
public class MainMenu extends MenuGroup {

    /**
     * <p>
     * Constructs a {@link MainMenu} instance that is personalized with a greeting
     * for the user and initializes various features that facilitate user interaction.
     * The following functionalities are included:
     * </p>
     * <ul>
     *   <li>Navigation to the enquiry menu, project application menu, project management menu, and officer application menu.</li>
     *   <li>Password reset functionality, allowing users to change their passwords securely.</li>
     *   <li>Adjustment of filter settings based on the user's role, enabling customized views of data.</li>
     * </ul>
     *
     * <p>
     * The menu options are tailored to the user's role, ensuring that users have access
     * to the appropriate functionalities. For instance, certain filter settings may be
     * restricted for non-manager users, enhancing security and usability.
     * </p>
     *
     * @param user the {@link User} accessing the system, which determines the available
     *             menu options and functionalities based on their role.
     */
    public MainMenu(User user) {
        super(user.getGreeting());

        // Adding sub-menus for various functionalities
        this.addMenuItem(new EnquiryMenu(user));
        this.addMenuItem(new ProjectApplicationMenu(user));
        this.addMenuItem(new ProjectManageMenu(user));
        this.addMenuItem(new OfficerApplyMenu(user));

        // Adding password reset functionality
        this.addMenuItem("Reset Password",
                () -> {
                    System.out.println("Please enter your new password: ");
                    String proposedPassword = sc.nextLine();
                    System.out.println("Please enter your password again: ");
                    if (proposedPassword.equals(sc.nextLine())){
                        PasswordResetHandler.resetPassword(user, proposedPassword);
                        System.out.println("Password change successful. Please login again");
                        // exit system
                        MenuNavigator.getInstance().popMenu();
                    } else {
                        System.out.println("Password change unsuccessful. ");
                    }
                    
                }
        );

        // Configuring filter options based on user role
        List<User.FILTER_SETTING> filterOptions = new ArrayList<>(Arrays.asList(User.FILTER_SETTING.values()));
        if (!(user instanceof Manager)) {
            filterOptions.remove(User.FILTER_SETTING.OWN_PROJECTS_ONLY); // Restricting filter option for non-managers
        }

        // Adding filter options to the menu
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
