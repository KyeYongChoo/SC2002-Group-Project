package program.boundary.menuTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import program.entity.users.User;

public class MenuGroup extends MenuItem{
    private final List<MenuItem> menuItems;
    private Supplier<String> dynamicDesc;
    private ArrayList<Supplier<MenuItem>> menuItemSuppliers = new ArrayList<>();
    private boolean isTransient = false;

    
    /*
     * Constructor for MenuGroup
     * Both functions as a Factory and a actual Menu in its own right. 
     * @param description the description of the menu group
     * @param visibleIf a predicate that determines if the menu group is visible
     */
    public MenuGroup(String description, Predicate<User> visibleIf) {
        super(description, 
            null,
            visibleIf); 
        this.menuItems = new ArrayList<>();
        dynamicDesc = () -> description;
        // need to set Action here because keyword "this" is not allowed when calling the super constructor
        this.setAction(() -> MenuNavigator.getInstance().pushMenu(this));
    }

    /*
     * Constructor for MenuGroup visible to everyone
     * @param description the description of the menu group
     */
    public MenuGroup(String description) {
        this(description, user -> true); 
    }

    // must be separate from Refresh. Because refresh recursively refreshes through all the lists, but instantiation should only occur if parent menu chosen, due to class cast probelms when applicants instantiate manager menus
    // Used by MenuNavigator
    public void lazyInstantiate(){
        if (menuItems.isEmpty()){
            menuItemSuppliers.forEach(menuItemSupplier -> menuItems.add(menuItemSupplier.get()));
        }
    }
    /**
     * Refreshes the menu items in this group.
     * Override this method in subclasses that need dynamic refreshing.
     */
    public void refresh() {
        // Base implementation refreshes the description
        description = dynamicDesc.get();
        lazyInstantiate();
        // Subclasses can override to refresh their content
        
        // DISABLED FEATURE: Recursively refresh any sub-menu groups. If activate recursive refreshing below, must deactivate above lazyInstantiate
        // If enabled below feature, must implement lazyInstantiate in MenuNavigator for pushMenu. 
        // antithetical to lazy instantiation. 
        // for (MenuItem item : menuItems) {
        //     if (item instanceof MenuGroup) {
        //         ((MenuGroup) item).refresh();
        //     }
        // }
    }

    private MenuGroup addMenuItem(Supplier<MenuItem> menuItemSupplier){
        menuItemSuppliers.add(menuItemSupplier);
        return this;
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
        addMenuItem(() -> menuItem);
        return this;
    }

    /*
     * Adds a menu item to the menu group
     * @param description the description of the menu item
     * @param action the action to perform when the menu item is selected
     * @return MenuGroup, supports method chaining
     */
    public MenuGroup addMenuItem(String description, MenuAction action){
        return this.addMenuItem(() -> new MenuItem (description, action));
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
        return this.addMenuItem(() -> new MenuItem (description, action, visibleIf));
    }

    public <T> MenuGroup addSelectionMenu (String description, Predicate<User> visibleIf, Supplier<List<T>> itemListSupplier, Function<T, String> itemLabelFunc, Consumer<T> onSelect){
        return this.addMenuItem(() -> new SelectionMenu<>(description, visibleIf, itemListSupplier, itemLabelFunc, onSelect));
    }
    public <T> MenuGroup addSelectionMenu (String description, Supplier<List<T>> itemListSupplier, Function<T, String> itemLabelFunc, Consumer<T> onSelect){
        return this.addMenuItem(() -> new SelectionMenu<>(description, itemListSupplier, itemLabelFunc, onSelect));
    }

    public <T> MenuGroup addTransientSelectionMenu(String description, Predicate<User> visibleIf, Supplier<List<T>> itemListSupplier, Function<T, String> itemLabelFunc, Consumer<T> onSelect){
        return this.addMenuItem(() -> (new SelectionMenu<>(description, visibleIf, itemListSupplier, itemLabelFunc, onSelect)).setTransient(true));
    }

    /*
     * Returns the list of menu items in the menu group
     * @return the list of menu items in the menu group
     */
    public List<MenuItem> getItems() {
        return menuItems;
    }

    public ArrayList<Supplier<MenuItem>> getItemSuppliers(){
        return menuItemSuppliers;
    }

    public Supplier<String> getDynamicDesc(){
        return dynamicDesc;
    }

    public void setDynamicDesc(Supplier<String> descSupplier){
        dynamicDesc = descSupplier;
    }
    public MenuGroup setTransient(boolean isTransient) {
        this.isTransient = isTransient;
        return this;
    }
    public boolean isTransient() {
        return isTransient;
    }    
}
