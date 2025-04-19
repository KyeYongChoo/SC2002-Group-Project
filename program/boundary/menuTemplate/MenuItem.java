package program.boundary.menuTemplate;

import java.util.Scanner;
import java.util.function.Predicate;

import program.boundary.console.AppScanner;
import program.entity.users.User;

/*
 * MenuItem.java
 * This class represents a menu item in the application. It implements the MenuAction interface and provides a description for the menu item.
 */
public class MenuItem{
    protected static final Scanner sc = AppScanner.getInstance();
    /*
     * description: A string that describes the menu item.
     * package private because MenuGroup etc will update it dynamically, but closed to other packages
     */
    String description;

    /*
     * action: A MenuAction that defines the action to be performed when the menu item is selected.
     * @link MenuAction
     */
    private MenuAction action;

    /*
     * visibleIf: A predicate that determines if the menu item is visible based on the context.
     *            The predicate takes an Object as input and returns a boolean indicating visibility.
     *            This allows for dynamic visibility based on the context in which the menu is displayed.
     * @link Predicate
     */
    private final Predicate<User> visibleIf;

    /*
     * Constructor: Initializes the menu item with a description.
     * @param description: A string that describes the menu item.
     * @param action: A MenuAction that defines the action to be performed when the menu item is selected.
     * @link MenuAction
     * @param visibleIf: A predicate that determines if the menu item is visible based on the context.
     *                  The predicate takes an Object as input and returns a boolean indicating visibility.
     *                 This allows for dynamic visibility based on the context in which the menu is displayed.
     */
    public MenuItem(String description, MenuAction action, Predicate<User> visibleIf) {
        this.description = description;
        this.action = action;
        this.visibleIf = visibleIf;
    }

    /*
     * Constructor: Initializes the menu item with a description and an action.
     * @param description: A string that describes the menu item.
     * @param action: A MenuAction that defines the action to be performed when the menu item is selected.
     * @link MenuAction
     * @param visibleIf: A predicate that determines if the menu item is visible based on the context.
     *                   The predicate takes an Object as input and returns a boolean indicating visibility.
     *                   This allows for dynamic visibility based on the context in which the menu is displayed.
     */
    public MenuItem(String description, MenuAction action) {
        this(description, action, x -> true); // visible by default
    }

    /*
     * getDescription: Returns the description of the menu item.
     * @return: A string that describes the menu item.
     */
    public String getDescription() {
        return description;
    }

    /*
     * isVisible: Checks if the menu item is visible based on the context.
     * Used by MenuNavigator to determine if the menu item should be displayed.
     * @param user: User who is looking at the option
     * @return: A boolean indicating if the menu item is visible based on the context.
     *          The predicate takes an Object as input and returns a boolean indicating visibility.
     */
    public boolean isVisible(User user) {
        return visibleIf.test(user);
    }

    /*
     * execute: Executes the action associated with the menu item.
     * This method is called when the menu item is selected by the user.
     */
    public void execute(){
        action.execute();
    }; 

    /*
     * setAction: Sets the action for the menu item.
     * @param action: A MenuAction that defines the action to be performed when the menu item is selected.
     */
    public void setAction(MenuAction action) {
        this.action = action;
    }

    public MenuAction getAction(){
        return action;
    }

    /*
     * addAction: Adds an action to the existing action for the menu item.
     * This allows for chaining multiple actions together.
     * @param action: A MenuAction that defines the action to be performed when the menu item is selected.
     * @link MenuAction
     * @return: A MenuAction that represents the combined action of the existing action and the new action.
     */
    public void addAction(MenuAction action) {
        this.action = this.action.andThen(action);
    }

    public Predicate<User> getVisibleIf(){
        return visibleIf;
    }
}
