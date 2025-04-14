package program.boundary.menuTemplate;

import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import program.boundary.console.AppScanner;
import program.boundary.console.ConsoleCommands;
import program.entity.users.User;

/*
 * Handles view stacks, to perform "go back to previous menu" behaviour
 * Actually runned, do not make a menu out of this. If in doubt, please make a menu out of menuItem if its just 1 action, make it out of MenuGroup if it leads to more menus
 */
public class MenuNavigator {
    private final Stack<MenuGroup>menuStack = new Stack<>();
    private static final Scanner sc = AppScanner.getInstance(); 
    private Object context;

    // Singleton: i.e. ensure only one instance is created in the app
    private static MenuNavigator instance = null;
    private MenuNavigator(){}
    public static MenuNavigator getInstance(){
        if (instance == null) instance = new MenuNavigator();
        return instance;
    }
    
    public void pushMenu(MenuGroup menu){
        menuStack.push(menu);
    }
    public void start(User user) {
        while (!menuStack.isEmpty()){
            ConsoleCommands.clearConsole();
            MenuGroup currentMenu = menuStack.peek();
            int choice = -1;

            System.out.println("\n" + currentMenu.getDescription());
            List<MenuItem> items = currentMenu.getItems().stream().filter(item -> item.isVisible(context)).toList();
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

    public void setContext(Object ctx) {
        this.context = ctx;
    }

    public Object getContext() {
        return context;
    }
}
