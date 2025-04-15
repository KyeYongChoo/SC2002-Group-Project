package program.boundary.menuTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import program.entity.users.User;

public class MenuGroup extends MenuItem {
    private final List<MenuItem> menuItems;
    
    /*
     * Constructor for MenuGroup
     * @param description the description of the menu group
     * @param visibleIf a predicate that determines if the menu group is visible
     */
    public MenuGroup(String description, Predicate<User> visibleIf) {
        super(description, 
            null,
            visibleIf); 
        this.menuItems = new ArrayList<>();
        this.setAction(() -> MenuNavigator.getInstance().pushMenu(this));
    }

    /*
     * Constructor for MenuGroup visible to everyone
     * @param description the description of the menu group
     */
    public MenuGroup(String description) {
        this(description, user -> true); 
    }

    /*
     * There will be 3 kinds of adding here:
     * 1. addMenuItem adds 1 single choice
     * 2. addMenuItem(new MenuGroup(...)) when clicking on a choice leads to another Menu
     * 3. addSelectionMenu() when you have a list of indeterminate length and u want the user to click on one of them
     * 
     * All of these can be chained indefinitely. 
     * 2 in particular, highly requires chaining. 
     */

    /*
     * Adds a menu item to the menu group
     * return MenuGroup, supports method chaining 
     * @param menuItem the menu item to add
     * @return MenuGroup, supports method chaining
     */
    public MenuGroup addMenuItem(MenuItem menuItem) {
        menuItems.add(menuItem);
        return this;
    }

    /*
     * Adds a menu item to the menu group
     * @param description the description of the menu item
     * @param action the action to perform when the menu item is selected
     * @return MenuGroup, supports method chaining
     */
    public MenuGroup addMenuItem(String description, MenuAction action){
        return this.addMenuItem(new MenuItem (description, action));
    }

    /*
     * Adds a menu item to the menu group
     * return MenuGroup, supports method chaining
     * @param description the description of the menu item
     * @param action the action to perform when the menu item is selected
     * @param visibleIf a predicate that determines if the menu item is visible
     * @return MenuGroup, supports method chaining
     */
    public MenuGroup addMenuItem(String description, MenuAction action, Predicate<User> visibleIf){
        return this.addMenuItem(new MenuItem (description, action, visibleIf));
    }

    public <T> MenuGroup addSelectionMenu (String description, Predicate<User> visibleIf, List<T> items, Function<T, String> itemLabelFunc, Consumer<T> onSelect){
        return this.addMenuItem(new SelectionMenu<>(description, visibleIf, items, itemLabelFunc, onSelect));
    }
    public <T> MenuGroup addSelectionMenu (String description, List<T> items, Function<T, String> itemLabelFunc, Consumer<T> onSelect){
        return this.addMenuItem(new SelectionMenu<>(description, items, itemLabelFunc, onSelect));
    }

    /*
     * Returns the list of menu items in the menu group
     * @return the list of menu items in the menu group
     */
    public List<MenuItem> getItems() {
        return menuItems;
    }
}
