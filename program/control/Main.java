package program.control;

import program.boundary.menu.*;
import program.boundary.menuTemplate.MenuNavigator;
import program.boundary.security.LoginHandler;
import program.control.enquiry.EnquiryList;
import program.control.housingApply.HousingReqList;
import program.control.officerApply.AssignReqList;
import program.entity.caching.DataInitializer;
import program.entity.caching.RecordSaver;
import program.entity.project.ProjectList;
import program.entity.users.User;
import program.entity.users.UserList;

/**
 * <p>
 * The {@code Main} class serves as the central control unit for initializing, running, and managing
 * the application lifecycle. It handles system data loading, user login, menu navigation, and data saving.
 * </p>
 *
 * <p>
 * Key components include:
 * <ul>
 *   <li>Initializing user and project lists.</li>
 *   <li>Handling login or skipping login for testing purposes.</li>
 *   <li>Starting the main menu navigation system.</li>
 *   <li>Saving all modified records before exiting the application.</li>
 * </ul>
 * </p>
 *
 * @see DataInitializer for loading system data.
 * @see RecordSaver for saving data on shutdown.
 * @see MenuNavigator for user interface control flow.
 */
public class Main {

    /** List of registered applicants. */
    public static UserList applicantList = new UserList();
    /** List of registered managers. */
    public static UserList managerList = new UserList();
    /** List of registered officers. */
    public static UserList officerList = new UserList();
    /** List of all projects. */
    public static ProjectList projectList = new ProjectList();
    /** List of all housing requests. */
    public static HousingReqList housingReqList = new HousingReqList();
    /** List of all enquiries submitted. */
    public static EnquiryList enquiryList = new EnquiryList();
    /** List of officer assignment requests. */
    public static AssignReqList assignReqList = new AssignReqList();

    /**
     * The main entry point of the program.
     * <p>
     * It initializes system data, optionally skips login for testing, starts the menu navigation, and saves data on shutdown.
     * </p>
     *
     * @param args Command-line arguments (not used).
     * @throws Exception if an unexpected error occurs during initialization or saving.
     */
    public static void main(String[] args) throws Exception {
        DataInitializer.initialise();

        // Testing mode: skip login
        boolean skipLogin = false;
        User client;
        if (skipLogin) {
            client = SkipLogin(USER.Manager);
        } else {
            client = LoginHandler.loginUser();
        }

        // Production mode example
        // User client = LoginHandler.loginUser();

        MenuNavigator.getInstance().pushMenu(new MainMenu(client));
        MenuNavigator.getInstance().start(client);

        RecordSaver.save();
    }

    /**
     * Enumeration of user types for login skipping during testing.
     */
    private enum USER {
        Applicant,
        Officer,
        Manager
    }

    /**
     * Skips the login process and retrieves a test {@link User} based on the selected user type.
     *
     * @param choice The {@link USER} type to retrieve.
     * @return A {@link User} instance corresponding to the selected role.
     */
    public static User SkipLogin(USER choice) {
        return switch (choice) {
            case Applicant -> applicantList.get(0);
            case Officer -> officerList.get(2); // Officer not assigned to any project
            case Manager -> managerList.get(0);
        };
    }
}
