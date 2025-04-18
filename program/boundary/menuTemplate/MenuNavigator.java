package program.boundary.menuTemplate;

import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import program.boundary.console.AppScanner;
import program.boundary.console.ConsoleCommands;
import program.entity.users.User;

/*
 * MenuNavigator.java
 * This class is responsible for managing the navigation between different menus in the application.
 * Singleton pattern is used to ensure that only one instance of MenuNavigator exists throughout the application.
 * It maintains a stack of MenuGroup objects, allowing users to navigate back to previous menus.
 */
public class MenuNavigator {

    /*
     * menuStack: A stack that holds the menu groups. It allows for navigating back to previous menus.
     */
    private final Stack<MenuGroup>menuStack = new Stack<>();
    private static final Scanner sc = AppScanner.getInstance(); 

    
    private User user;

    private static MenuNavigator instance = null;
    private MenuNavigator(){}

    /*
     * getInstance: A static method that returns the singleton instance of MenuNavigator.
     *              If the instance is null, it creates a new instance.
     * @return: The singleton instance of MenuNavigator.
     */
    public static MenuNavigator getInstance(){
        if (instance == null) instance = new MenuNavigator();
        return instance;
    }
    
    /*
     * pushMenu: A method that pushes a new menu group onto the stack.
     *           This allows the user to navigate to a new menu while keeping track of the previous menus.
     * @param menu: The menu group to be pushed onto the stack.
     */
    public void pushMenu(MenuGroup menu){
        // lazy instantiation needs to be started
        menu.lazyInstantiate();
        menuStack.push(menu);
    }
    /*
     * start: A method that starts the menu navigation process.
     *        It displays the current menu and allows the user to select an option.
     *       The user can navigate through the menu items and execute the corresponding actions.
     * @param user: The user object representing the current user of the application.
     */
    public void start(User user) {
        while (!menuStack.isEmpty()){
            ConsoleCommands.clearConsole();
            MenuGroup currentMenu = menuStack.peek();
            currentMenu.refresh();
            int choice = -1;

            System.out.println("\n" + currentMenu.getDescription());
            List<MenuItem> items = currentMenu.getItems().stream().filter(item -> item.isVisible(user)).toList();
            for (int i = 0; i < items.size(); i++){
                System.out.printf("%d. %s\n", i + 1, items.get(i).getDescription());
            }
            System.out.printf("%d. Go back\n", items.size() + 1);

            try{
                choice = Integer.parseInt(sc.nextLine()) - 1;

                if (choice < 0 || choice > items.size()) throw new Exception("Please enter a number between 0 and " + String.valueOf(items.size()) + " inclusive. \n");
                // pop the newest layer and get to previous view
                if (choice == items.size()) {menuStack.pop(); continue;}
                else {
                    MenuItem selected = items.get(choice);
                    selected.execute();
                    if (!(selected instanceof MenuGroup)){
                        System.out.println("Press enter to continue...");
                        sc.nextLine();
                    }
                }

            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }


    public void setUser(User user) {
        this.user = user;
    }


    public User getUser() {
        return user;
    }
}
