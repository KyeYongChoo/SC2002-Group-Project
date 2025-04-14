package program.boundary.menuTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class MenuGroup extends MenuItem {
    private final List<MenuItem> menuItems;
    
    /*
     * Constructor for MenuGroup
     * @param description the description of the menu group
     * @param visibleIf a predicate that determines if the menu group is visible
     */
    public MenuGroup(String description, Predicate<Object> visibleIf) {
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
        this(description, dummy -> true); 
    }

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
    public MenuGroup addMenuItem(String description, MenuAction action, Predicate<Object> visibleIf){
        return this.addMenuItem(new MenuItem (description, action, visibleIf));
    }

    /*
     * Returns the list of menu items in the menu group
     * @return the list of menu items in the menu group
     */
    public List<MenuItem> getItems() {
        return menuItems;
    }
}
