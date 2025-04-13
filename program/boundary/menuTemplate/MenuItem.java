package program.boundary.menuTemplate;

import java.util.Scanner;
import java.util.function.Predicate;

import program.boundary.console.AppScanner;

/*
 * MenuItem.java
 * This class represents a menu item in the application. It implements the MenuAction interface and provides a description for the menu item.
 */
public class MenuItem{
    protected static final Scanner sc = AppScanner.getInstance();
    /*
     * description: A string that describes the menu item.
     */
    private final String description;

    private MenuAction action;

    private final Predicate<Object> visibleIf;

    /*
     * Constructor: Initializes the menu item with a description.
     * @param description: A string that describes the menu item.
     * @param action: A MenuAction that defines the action to be performed when the menu item is selected.
     * @link MenuAction
     * @param visibleIf: A predicate that determines if the menu item is visible based on the context.
     *                  The predicate takes an Object as input and returns a boolean indicating visibility.
     */
    public MenuItem(String description, MenuAction action, Predicate<Object> visibleIf) {
        this.description = description;
        this.action = action;
        this.visibleIf = visibleIf;
    }

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

    public boolean isVisible(Object context) {
        return visibleIf.test(context);
    }

    public void execute(){
        action.execute();
    }; 

    public void setAction(MenuAction action) {
        this.action = action;
    }

    public void addAction(MenuAction action) {
        this.action = this.action.andThen(action);
    }
}
