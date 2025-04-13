package program.boundary.menuTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import program.boundary.AppScanner;

public class MenuGroup extends MenuItem {
    private static final Scanner sc = AppScanner.getInstance();
    private final List<MenuItem> menuItems;

    private MenuGroup(String description, Predicate<Object> visibleIf) {
        super(description, 
            null, // Temporarily pass null, will be set in Factory
            visibleIf); 
        this.menuItems = new ArrayList<>();
    }
    private MenuGroup(String description) {
        super(description, 
            null, // Temporarily pass null, will be set in Factory
            dummy -> true); 
        this.menuItems = new ArrayList<>();
    }

    public static MenuGroup create(String description){
        MenuGroup group = new MenuGroup(description);
        group.setAction(() -> MenuNavigator.getInstance().pushMenu(group));
        return group;
    }
    public static MenuGroup create(String description, Predicate<Object> visibleIf){
        MenuGroup group = new MenuGroup(description);
        group.setAction(() -> MenuNavigator.getInstance().pushMenu(group));
        return group;
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

    public MenuItem displayAndChoose(Object context) {
        List<MenuItem> visibleItems = new ArrayList<>();

        System.out.println("===== " + getDescription() + " =====");

        int index = 1;
        for (MenuItem item : menuItems) {
            if (item.isVisible(context)) {
                visibleItems.add(item);
                System.out.println(index + ". " + item.getDescription());
                index++;
            }
        }

        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();

        if (choice >= 1 && choice <= visibleItems.size()) {
            return visibleItems.get(choice - 1);
        } else {
            System.out.println("Invalid choice.");
            return null;
        }
    }
}
