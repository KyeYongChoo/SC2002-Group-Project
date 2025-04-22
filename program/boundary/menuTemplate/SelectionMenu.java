package program.boundary.menuTemplate;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import program.entity.users.User;

/**
 * <p>
 * {@code SelectionMenu} is a powerful and flexible type of menu that dynamically generates a list of options for the user to choose from.
 * Unlike a regular {@link MenuGroup} where the menu items are fixed at the time of creation, a {@code SelectionMenu} gets its list of
 * items at runtime using a {@link Supplier}. Each item in this list is then converted into a user-friendly label using a {@link Function}.
 * Finally, when the user selects an item, a specific action defined by a {@link Consumer} is executed.
 * </p>
 *
 * <p>
 * Think of a {@code SelectionMenu} as a dynamic dropdown list or a set of choices that can change based on the current state of the application.
 * For example, it could display a list of available projects, a list of applicants, or any other collection of data that the user needs to select from.
 * </p>
 *
 * <p>
 * This class extends {@link MenuGroup}, inheriting its ability to be a part of a larger menu structure and to have a description and visibility conditions.
 * Crucially, {@code SelectionMenu} automatically updates its displayed menu items whenever the menu is either initially created or subsequently refreshed.
 * This ensures that the user always sees the most up-to-date list of options.
 * </p>
 *
 * @param <T> The generic type parameter {@code T} represents the type of the items that will be displayed in the menu and can be selected by the user.
 * For instance, if you want to display a list of {@code Project} objects, then {@code T} would be {@code Project}.
 *
 * @see MenuGroup A container for menu items, which {@code SelectionMenu} extends.
 */
public class SelectionMenu<T> extends MenuGroup {

    /**
     * {@code itemSupplier} is a functional interface that provides the list of items to be displayed in the selection menu.
     * When the menu needs to be populated or refreshed, the {@code get()} method of this supplier is called to retrieve the latest list of items of type {@code T}.
     */
    private Supplier<List<T>> itemSupplier;

    /**
     * {@code itemLabelFunc} is a functional interface that takes an item of type {@code T} as input and returns a {@code String}.
     * This function is responsible for converting each item in the list (obtained from {@code itemSupplier}) into a human-readable label that will be displayed in the menu.
     */
    private Function<T, String> itemLabelFunc;

    /**
     * {@code onSelect} is a functional interface that accepts an item of type {@code T} as input and performs an action.
     * When the user selects a particular item from the menu, the {@code accept(T)} method of this consumer is called with the selected item. This defines what happens upon selection.
     */
    private Consumer<T> onSelect;

    /**
     * Constructs a {@code SelectionMenu} with a fixed, static description for the menu.
     *
     * @param description The text that will be displayed as the title or description of this selection menu.
     * @param itemSupplier A {@link Supplier} that provides the list of items (of type {@code T}) to be displayed.
     * @param itemLabelFunc A {@link Function} that takes an item of type {@code T} and returns the label to display for that item.
     * @param onSelect A {@link Consumer} that accepts the selected item (of type {@code T}) and defines the action to perform.
     */
    public SelectionMenu(String description, Supplier<List<T>> itemSupplier, Function<T, String> itemLabelFunc, Consumer<T> onSelect) {
        this(description, dummy -> true, itemSupplier, itemLabelFunc, onSelect); // Calls the constructor with a static description and always-true visibility.
    }

    /**
     * Constructs a {@code SelectionMenu} where the description of the menu can change dynamically.
     *
     * @param dynamicDescription A {@link Supplier} that provides the description of the menu. The {@code get()} method of this supplier will be called to get the current description.
     * @param itemSupplier A {@link Supplier} that provides the list of items (of type {@code T}) to be displayed.
     * @param itemLabelFunc A {@link Function} that takes an item of type {@code T} and returns the label to display for that item.
     * @param onSelect A {@link Consumer} that accepts the selected item (of type {@code T}) and defines the action to perform.
     */
    public SelectionMenu(Supplier<String> dynamicDescription, Supplier<List<T>> itemSupplier, Function<T, String> itemLabelFunc, Consumer<T> onSelect) {
        this(dynamicDescription, dummy -> true, itemSupplier, itemLabelFunc, onSelect); // Calls the constructor with a dynamic description and always-true visibility.
    }

    /**
     * Constructs a {@code SelectionMenu} with a dynamic description and a condition that determines whether the menu is visible to a particular user.
     *
     * @param dynamicDescription A {@link Supplier} that provides the description of the menu.
     * @param visibleIf A {@link Predicate} that takes a {@link User} and returns {@code true} if this menu should be visible to that user, and {@code false} otherwise.
     * @param itemSupplier A {@link Supplier} that provides the list of items (of type {@code T}) to be displayed.
     * @param itemLabelFunc A {@link Function} that takes an item of type {@code T} and returns the label to display for that item.
     * @param onSelect A {@link Consumer} that accepts the selected item (of type {@code T}) and defines the action to perform.
     */
    public SelectionMenu(Supplier<String> dynamicDescription, Predicate<User> visibleIf, Supplier<List<T>> itemSupplier, Function<T, String> itemLabelFunc, Consumer<T> onSelect) {
        this(dynamicDescription.get(), visibleIf, itemSupplier, itemLabelFunc, onSelect); // Calls the constructor with the evaluated dynamic description and the visibility predicate.
    }

    /**
     * Constructs a {@code SelectionMenu} with a fixed, static description and a condition that determines whether the menu is visible to a particular user.
     *
     * @param description The text that will be displayed as the title or description of this selection menu.
     * @param visibleIf A {@link Predicate} that takes a {@link User} and returns {@code true} if this menu should be visible to that user, and {@code false} otherwise.
     * @param itemSupplier A {@link Supplier} that provides the list of items (of type {@code T}) to be displayed.
     * @param itemLabelFunc A {@link Function} that takes an item of type {@code T} and returns the label to display for that item.
     * @param onSelect A {@link Consumer} that accepts the selected item (of type {@code T}) and defines the action to perform.
     */
    public SelectionMenu(String description, Predicate<User> visibleIf, Supplier<List<T>> itemSupplier, Function<T, String> itemLabelFunc, Consumer<T> onSelect) {
        super(description, user_ -> visibleIf.test(user_) &&
            itemSupplier.get()!=null && 
            !itemSupplier.get().isEmpty());
        // Store these for later refresh calls
        this.itemSupplier = itemSupplier;
        this.itemLabelFunc = itemLabelFunc;
        this.onSelect = onSelect;
    }

    /**
     * Fetches the latest list of items from the {@code itemSupplier}, clears any existing menu items in this group,
     * and then creates new {@link MenuItem} objects for each item in the fetched list. The label for each menu item
     * is generated using the {@code itemLabelFunc}, and the action to be performed when an item is selected is defined
     * by the {@code onSelect} consumer.
     */
    private void populateItems() {
        getItems().clear(); // Remove any existing menu items from this selection menu before adding new ones.
        getItemSuppliers().clear();

        List<T> items = itemSupplier.get(); // Get the latest list of items from the supplier.

        // Handle the case where the supplier might return null (e.g., if there are no items available).
        if (items == null) {
            System.out.println("Sorry, no items available for selection.");
        } else {
            items.forEach(item -> {
                this.addMenuItem(itemLabelFunc.apply(item), () -> {
                    if (onSelect != null) onSelect.accept(item);
                });
            });
        }
    }

    /**
     * Overrides the {@code refresh()} method of the parent {@link MenuGroup}. When a {@code SelectionMenu} is refreshed,
     * it not only updates its description (as the base {@code refresh()} does) but also re-populates its list of selectable
     * items by calling the {@link #populateItems()} method. This ensures that the menu always displays the most current data.
     */
    @Override
    public void refresh() {
        populateItems(); // Reload the list of selection items.
        super.refresh(); // Call the parent's refresh method to update the menu description.
    }
}