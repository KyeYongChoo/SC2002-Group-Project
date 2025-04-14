package program.boundary.menuTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class MenuGroup extends MenuItem {
    private final List<MenuItem> menuItems;
    
    public MenuGroup(String description, Predicate<Object> visibleIf) {
        super(description, 
            null,
            visibleIf); 
        this.menuItems = new ArrayList<>();
        this.setAction(() -> MenuNavigator.getInstance().pushMenu(this));
    }
    public MenuGroup(String description) {
        this(description, dummy -> true); 
    }

    // return line supports method chaining 
    public MenuItem addMenuItem(MenuItem menuItem) {
        menuItems.add(menuItem);
        return this;
    }

    // Centralise all the addMenuItem logic
    public MenuItem addMenuItem(String description, MenuAction action){
        return this.addMenuItem(new MenuItem (description, action));
    }

    // Centralise all the addMenuItem logic
    public MenuItem addMenuItem(String description, MenuAction action, Predicate<Object> visibleIf){
        return this.addMenuItem(new MenuItem (description, action, visibleIf));
    }

    public List<MenuItem> getItems() {
        return menuItems;
    }
}
