package program.boundary.menuTemplate;

import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.stream.Collectors;

import program.boundary.console.AppScanner;
import program.boundary.console.ConsoleCommands;
import program.entity.users.User;

/**
 * <p>
 * {@code MenuNavigator} plays the crucial role of managing how users move between different menus within the application.
 * It implements the Singleton design pattern, ensuring that only one central instance of {@code MenuNavigator} exists
 * throughout the entire application lifecycle. This single instance is responsible for keeping track of the current menu
 * and the history of visited menus.
 * </p>
 *
 * <p>
 * Internally, {@code MenuNavigator} uses a stack data structure called {@code menuStack}. This stack stores instances
 * of {@link MenuGroup}. When a user navigates into a submenu, that submenu's {@code MenuGroup} is pushed onto the stack.
 * This allows the application to remember the path the user has taken. To go back to the previous menu, the top
 * {@code MenuGroup} is simply popped off the stack.
 * </p>
 *
 * @see MenuGroup Represents a collection of menu items.
 * @see MenuItem Represents a single selectable option within a menu.
 */
public class MenuNavigator {

    /**
     * {@code menuStack} is a stack that holds the {@link MenuGroup} objects that the user has navigated through.
     * The top of the stack always represents the currently displayed menu. Pushing a new menu onto the stack
     * moves the user forward into a submenu, while popping a menu from the stack moves the user backward to the
     * previously viewed menu.
     */
    private final Stack<MenuGroup>menuStack = new Stack<>();
    /**
     * {@code sc} is a static and final instance of the {@link Scanner} class, obtained from {@link AppScanner}.
     * This single scanner instance is used throughout the menu navigation process to read user input from the console,
     * such as the number corresponding to their menu choice.
     */
    private static final Scanner sc = AppScanner.getInstance(); 

    /**
     * {@code user} stores the {@link User} object representing the currently logged-in user. This information is
     * important because the visibility and behavior of menu items can be tailored based on the user's role and
     * permissions.
     */
    private User user;

    /**
     * {@code instance} holds the single instance of the {@code MenuNavigator} class, as per the Singleton pattern.
     * It is initialized to {@code null} and created only when the {@link #getInstance()} method is called for the first time.
     */
    private static MenuNavigator instance = null;

    /**
     * Private constructor to prevent direct instantiation of {@code MenuNavigator} from outside the class.
     * This ensures that the Singleton pattern is enforced, and the only way to access the {@code MenuNavigator}
     * is through the static {@link #getInstance()} method.
     */
    private MenuNavigator(){}

    /**
     * Returns the single instance of the {@code MenuNavigator}. If an instance does not yet exist, a new one is created
     * and then returned. Subsequent calls to this method will always return the same instance.
     *
     * @return The singleton instance of {@code MenuNavigator} that manages menu navigation.
     */
    public static MenuNavigator getInstance(){
        if (instance == null) {
            instance = new MenuNavigator();// Create a new MenuNavigator only if one doesn't already exist.
        }
        return instance; // Return the single instance of the MenuNavigator.
    }

    /**
     * Pushes a new {@link MenuGroup} onto the top of the {@code menuStack}. This action simulates the user navigating
     * into a new submenu. The newly pushed menu will become the currently displayed menu. The previous menu remains
     * on the stack, allowing the user to navigate back later.
     *
     * @param menu The {@code MenuGroup} object that the user is navigating into. This menu will be displayed next.
     */
    public void pushMenu(MenuGroup menu){
        menuStack.push(menu);
    }
   
    /**
     * Starts the process of displaying menus and handling user interactions. This method continues to run as long as
     * there are menus on the {@code menuStack}. It retrieves the current menu, displays its visible options to the
     * user, prompts for input, and then either executes the selected menu item's action or navigates to a different menu.
     *
     * @param user The {@link User} object representing the currently logged-in user. This user object is used to determine
     * the visibility of menu items.
     */
    public void start(User user) {
        this.user = user; // Set the current user for the navigation session.
        while (!menuStack.isEmpty()) { // Continue as long as there are menus in the stack.
            ConsoleCommands.clearConsole(); // Clear the console to provide a clean display for the new menu.
            MenuGroup currentMenu = menuStack.peek(); // Get the current menu from the top of the stack without removing it.
            currentMenu.refresh(); // Update the description of the current menu, in case it's dynamic.
            int choice = -1; // Initialize the user's menu choice.

            System.out.println("\n" + currentMenu.getDescription()); // Display the description of the current menu.
            List<MenuItem> items = currentMenu.getItems().stream() // Get all menu items from the current menu.
                    .filter(item -> item.isVisible(user)) // Filter out items that are not visible to the current user.
                    .collect(Collectors.toList()); // Collect the visible menu items into a new list.

            if (items.isEmpty()) System.out.println("Sorry, no items");
            // Display the visible menu items with numbered options.
            for (int i = 0; i < items.size(); i++){
                System.out.printf("%d. %s\n", i + 1, items.get(i).getDescription());
            }
            System.out.printf("%d. Go back\n", items.size() + 1);// Add an option to go back to the previous menu.

            try{
                choice = Integer.parseInt(sc.nextLine()) - 1; // Read the user's input and adjust it to be zero-based.

                // Validate the user's input to ensure it's within the valid range of menu options.
                if (choice < 0 || choice > items.size()) throw new Exception("Please enter a number between 0 and " + String.valueOf(items.size()) + " inclusive. \n");
                // If the user selected the "Go back" option.
                if (choice == items.size() || currentMenu.isTransient()) menuStack.pop(); // Remove the current menu from the stack, navigating back to the previous one.
                if (choice == items.size()) continue; // Skip to the next iteration of the loop to display the previous menu.
                else {
                    MenuItem selected = items.get(choice); // Get the MenuItem object corresponding to the user's choice.
                    selected.execute(); // Execute the action associated with the selected menu item.
                    // If the selected item is not another menu group (i.e., it's an action), wait for the user to press Enter before continuing.
                    if (!(selected instanceof MenuGroup)){
                        System.out.println("Press enter to continue...");
                        sc.nextLine();
                    }
                }

            } catch (NumberFormatException e){
                System.out.println("Please enter a number only");
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Sets the {@link User} object representing the currently logged-in user. This user information can be used
     * by {@link MenuItem} and {@link MenuGroup} to determine visibility and behavior.
     *
     * @param user The {@link User} object to be set as the current user.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Retrieves the {@link User} object representing the currently logged-in user.
     *
     * @return The current {@link User} object.
     */
    public User getUser() {
        return user;
    }

    // currently unused
    public MenuGroup popMenu(){
        return menuStack.pop();
    }
}
