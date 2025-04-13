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

    public void addMenuItem(MenuItem menuItem) {
        menuItems.add(menuItem);
    }

    public void addMenuItem(String description, MenuAction action){
        menuItems.add(new MenuItem (description, action));
    }

    public void addMenuItem(String description, MenuAction action, Predicate<Object> visibleIf){
        menuItems.add(new MenuItem (description, action, visibleIf));
    }

    public List<MenuItem> getItems() {
        return menuItems;
    }
}
