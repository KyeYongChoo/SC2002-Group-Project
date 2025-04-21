package program.boundary.menuTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import program.entity.users.User;

/**
 * {@code MenuGroup} represents a collection of interactive options (menu items) within the application's user interface.
 * It acts as a container, allowing you to organize related functionalities under a common heading.
 *
 * <p>Think of a {@code MenuGroup} like a submenu in a desktop application or a section in a web page's navigation bar.
 * It holds individual {@link MenuItem} objects, which represent specific actions or further navigation points.</p>
 *
 * <p>A key feature of {@code MenuGroup} is its support for dynamic descriptions. The text displayed for the group
 * can be determined at runtime based on the current context, such as the logged-in user. This allows for more
 * informative and personalized menus.</p>
 *
 * <p>This class extends {@link MenuItem}, meaning a {@code MenuGroup} itself can be treated as a menu item,
 * allowing for the creation of nested menu structures (submenus within submenus). It provides convenient methods
 * for adding various types of menu items, including simple actions, nested groups, and selection-based menus.
 * These methods are designed to be chainable, making menu construction more readable and concise.</p>
 *
 * @author Choo Kye Yong
 * @version 1.0
 */
public class MenuGroup extends MenuItem{
    private final List<MenuItem> menuItems; // A list to hold all the menu items contained within this group.
    // default description is the description when is MenuItem of another menuGroup
    private Supplier<String> dynamicDesc; // A function that provides the description of the menu group, potentially changing over time.

    private ArrayList<Supplier<MenuItem>> menuItemSuppliers = new ArrayList<>();
    private boolean isTransient = false;

    
    /**
     * Constructs a {@code MenuGroup} with a specific display text and a condition that determines when it should be visible to a user.
     *
     * @param description The text that will be displayed for this menu group to the user.
     * @param visibleIf A {@link Predicate} (a function that returns a boolean) that takes a {@link User} object as input.
     * This predicate determines whether this menu group should be visible to that particular user.
     * If the predicate returns {@code true}, the group is visible; otherwise, it's hidden.
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

    /**
     * Constructs a {@code MenuGroup} with a specified display text that is visible to all users.
     *
     * @param description The text that will be displayed for this menu group. Since no visibility
     * predicate is provided, this group will always be visible.
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
     // Calls the main constructor with a predicate that always returns true, making it visible to any user.
    }

    /**
     * Updates the display text of this menu group.
     *
     * <p>This method is called to refresh the description of the menu group, potentially using the
     * {@link #dynamicDesc} supplier if it has been set. It also recursively calls the {@code refresh()}
     * method on any nested {@code MenuGroup} items within this group, ensuring their descriptions are also updated.</p>
     */
    public void refresh() {
        description = dynamicDesc.get(); // Gets the current description from the dynamic description supplier.
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

    /**
     * Adds an existing {@link MenuItem} object to this menu group.
     *
     * @param menuItem The {@link MenuItem} to be added to this group. This could be a simple action, another {@code MenuGroup} (for creating submenus), or a {@link SelectionMenu}.
     * @return The current {@code MenuGroup} instance. This allows you to chain multiple {@code addMenuItem} calls together in a fluent style.
     */
    public MenuGroup addMenuItem(MenuItem menuItem) {
        addMenuItem(() -> menuItem);
        return this;
    }

    /**
     * Creates a new {@link MenuItem} with the given display text and action, and adds it to this menu group.
     *
     * @param description The text to be displayed for this new menu item.
     * @param action The {@link MenuAction} to be executed when this menu item is selected.
     * @return The current {@code MenuGroup} instance for method chaining.
     */
    public MenuGroup addMenuItem(String description, MenuAction action){
        return this.addMenuItem(() -> new MenuItem (description, action));
    }

    /**
     * Creates a new {@link MenuItem} with the given display text, action, and visibility condition, and adds it to this menu group.
     *
     * @param description The text to be displayed for this new menu item.
     * @param action The {@link MenuAction} to be executed when this menu item is selected.
     * @param visibleIf A {@link Predicate} that determines if this menu item should be visible to a given {@link User}.
     * @return The current {@code MenuGroup} instance for method chaining.
     */
    public MenuGroup addMenuItem(String description, MenuAction action, Predicate<User> visibleIf){
        return this.addMenuItem(() -> new MenuItem (description, action, visibleIf));
    }

    /**
     * Creates a new {@link SelectionMenu} with the specified parameters and adds it to this menu group.
     * A {@code SelectionMenu} presents a list of items to the user for selection, and performs an action based on their choice.
     *
     * @param <T> The type of the items that will be displayed in the selection menu.
     * @param description The text to be displayed as the title or prompt for the selection menu.
     * @param visibleIf A {@link Predicate} that determines if this selection menu should be visible to a given {@link User}.
     * @param itemListSupplier A {@link Supplier} (a function that returns a value) that provides the list of items to be displayed in the selection menu. This allows the list to be generated dynamically.
     * @param itemLabelFunc A {@link Function} that takes an item of type {@code T} and returns a {@code String} that will be used as the display label for that item in the menu.
     * @param onSelect A {@link Consumer} (a function that accepts a single input and performs an operation) that defines the action to be performed when a user selects an item of type {@code T} from the menu.
     * @return The current {@code MenuGroup} instance for method chaining.
     */
    public <T> MenuGroup addSelectionMenu (String description, Predicate<User> visibleIf, Supplier<List<T>> itemListSupplier, Function<T, String> itemLabelFunc, Consumer<T> onSelect){
        return this.addMenuItem(() -> new SelectionMenu<>(description, visibleIf, itemListSupplier, itemLabelFunc, onSelect));
    }
    /**
     * Creates a new {@link SelectionMenu} with the specified parameters and adds it to this menu group.
     * This is an overloaded version that assumes the selection menu is always visible.
     *
     * @param <T> The type of the items in the selection menu.
     * @param description The description of the selection menu.
     * @param itemListSupplier A {@link Supplier} that provides the list of items for selection.
     * @param itemLabelFunc A {@link Function} to get the display label for each item.
     * @param onSelect A {@link Consumer} that defines the action when an item is selected.
     * @return The current {@code MenuGroup} instance for method chaining.
     */
    public <T> MenuGroup addSelectionMenu (String description, Supplier<List<T>> itemListSupplier, Function<T, String> itemLabelFunc, Consumer<T> onSelect){
        return this.addMenuItem(() -> new SelectionMenu<>(description, itemListSupplier, itemLabelFunc, onSelect));
    }

    public <T> MenuGroup addTransientSelectionMenu(String description, Predicate<User> visibleIf, Supplier<List<T>> itemListSupplier, Function<T, String> itemLabelFunc, Consumer<T> onSelect){
        return this.addMenuItem(() -> (new SelectionMenu<>(description, visibleIf, itemListSupplier, itemLabelFunc, onSelect)).setTransient(true));
    }

    /**
     * Returns the list of {@link MenuItem} objects that are currently contained within this menu group.
     *
     * @return An unmodifiable {@link List} of {@link MenuItem} objects. This list represents the items that will be displayed when this menu group is navigated into.
     */
    public List<MenuItem> getItems() {
        return menuItems; // Returns the internal list of menu items.
    }

    /**
     * Returns the {@link Supplier} that provides the dynamic description for this menu group.
     *
     * @return The {@link Supplier<String>} that, when its {@code get()} method is called, returns the current dynamic description of the menu group.
     */
    public ArrayList<Supplier<MenuItem>> getItemSuppliers(){
        return menuItemSuppliers;
    }

    public Supplier<String> getDynamicDesc(){
        return dynamicDesc; // Returns the supplier for the dynamic description.
    }

    /**
     * Sets a new {@link Supplier} to provide the dynamic description for this menu group.
     * This allows the displayed text of the menu group to change based on certain conditions or events.
     *
     * @param descSupplier A {@link Supplier<String>} that will be used to get the description of this menu group whenever it needs to be displayed or refreshed.
     */
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
