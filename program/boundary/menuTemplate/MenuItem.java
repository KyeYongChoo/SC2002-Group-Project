package program.boundary.menuTemplate;

import java.util.Scanner;
import java.util.function.Predicate;
import program.boundary.console.AppScanner;
import program.entity.users.User;

/**
 * {@code MenuItem} represents a single interactive option that can be displayed within a menu in the application.
 * Each menu item has three core attributes:
 * <ul>
 * <li>A **description**: The text that is shown to the user in the menu.</li>
 * <li>A **visibility condition**: A rule that determines whether this menu item should be displayed to a particular user.</li>
 * <li>An **action**: The specific task or operation that is performed when the user selects this menu item.</li>
 * </ul>
 *
 * <p>Think of a {@code MenuItem} as a button or a selectable choice in a list. When the user interacts with it, something happens.</p>
 *
 * <p>{@code MenuItem} objects are typically organized within {@link MenuGroup} objects, which act as containers for multiple menu options.
 * The navigation between different menus and the display of menu items are handled by the {@link MenuNavigator} class.</p>
 */
public class MenuItem {

    /**
     * {@code sc} is a shared instance of the {@link Scanner} class, specifically obtained through {@link AppScanner}.
     * This scanner is used to read input from the user's console when an action associated with a menu item requires it.
     * By sharing a single instance, we ensure consistent input handling throughout the menu system.
     */
    protected static final Scanner sc = AppScanner.getInstance();

    /**
     * {@code description} is the text that will be displayed to the user for this menu item.
     * It should be clear and concise, indicating the purpose or action associated with this option.
     * This field has package-private access, allowing classes within the same package (like {@link MenuGroup})
     * to potentially modify it dynamically if needed (though this is not the primary way to change descriptions;
     * consider using dynamic descriptions in {@link MenuGroup} for more complex scenarios).
     */
    String description;

    /**
     * {@code action} is an instance of the {@link MenuAction} interface. It encapsulates the code that will be executed
     * when this menu item is selected by the user. The {@code MenuAction} interface defines a single method, {@code execute()},
     * which is called when the menu item is activated. This allows for a clean separation between the menu structure
     * and the actual operations performed.
     */
    private MenuAction action;

    /**
     * {@code visibleIf} is a {@link Predicate} that takes a {@link User} object as input and returns a boolean value.
     * A predicate is essentially a test. In this case, it tests whether this menu item should be visible to the given user.
     * This allows for context-aware menus where certain options are only shown to users who meet specific criteria (e.g., users with certain roles or permissions).
     */
    private final Predicate<User> visibleIf;

    /**
     * Constructs a new {@code MenuItem} with a specified description, an action to perform, and a condition for its visibility.
     *
     * @param description The text that will be displayed for this menu item.
     * @param action The {@link MenuAction} object whose {@code execute()} method will be called when this item is selected.
     * @param visibleIf A {@link Predicate<User>} that determines whether this menu item is visible to a given user.
     * The {@code test(User)} method of this predicate will be called to check visibility.
     */
    public MenuItem(String description, MenuAction action, Predicate<User> visibleIf) {
        this.description = description;
        this.action = action;
        this.visibleIf = visibleIf;
    }

    /**
     * Constructs a new {@code MenuItem} with a specified description and an action to perform. This constructor creates a menu item that is always visible to all users.
     *
     * @param description The text that will be displayed for this menu item.
     * @param action The {@link MenuAction} object to be executed upon selection.
     * This constructor implicitly sets the visibility predicate to always return {@code true}.
     */
    public MenuItem(String description, MenuAction action) {
        this(description, action, user -> true); // Calls the main constructor with a predicate that always evaluates to true.
    }

    /**
     * Returns the text description of this menu item. This is the string that is displayed to the user in the menu.
     *
     * @return The description text of the menu item.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Checks if this menu item should be visible to a specific user. This method uses the {@link #visibleIf} predicate
     * to determine the visibility based on the user's attributes or context.
     *
     * @param user The {@link User} object for whom the visibility of this menu item is being checked.
     * @return {@code true} if the menu item should be displayed to the given user (the predicate returns {@code true});
     * {@code false} otherwise (the predicate returns {@code false}).
     */
    public boolean isVisible(User user) {
        return visibleIf.test(user); // Calls the test method of the visibility predicate with the given user.
    }

    /**
     * Executes the action that is associated with this menu item. This method calls the {@code execute()} method
     * of the {@link MenuAction} object that was set for this menu item. This is the core functionality of a menu item –
     * performing an operation when it is selected.
     */
    public void execute() {
        if (action != null) {
            action.execute(); // Calls the execute method of the associated MenuAction if one is set.
        }
    }

    /**
     * Sets a new action to be performed when this menu item is selected. This replaces any action that was previously associated with this item.
     *
     * @param action The new {@link MenuAction} object to be executed upon selection of this menu item.
     */
    public void setAction(MenuAction action) {
        this.action = action;
    }

    /**
     * Chains a new action to the existing action of this menu item. When this menu item is selected, the original action
     * will be executed first, and then the {@code execute()} method of the provided {@code action} will be called.
     * This allows you to add additional behavior to a menu item without modifying its original action.
     *
     * @param action The additional {@link MenuAction} to be executed after the current action when this menu item is selected.
     */
    public void addAction(MenuAction action) {
        if (this.action == null) {
            this.action = action; // If there's no existing action, just set the new one.
        } else {
            this.action = this.action.andThen(action); // Use the andThen method of MenuAction to create a composite action.
        }
    }
}
