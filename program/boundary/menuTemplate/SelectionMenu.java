package program.boundary.menuTemplate;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import program.entity.users.User;

public class SelectionMenu<T> extends MenuGroup {
    private Supplier<List<T>> itemSupplier;
    private Function<T, String> itemLabelFunc;
    private Consumer<T> onSelect;

    public SelectionMenu(String description, Supplier<List<T>> itemSupplier, Function<T, String> itemLabelFunc, Consumer<T> onSelect) {
        this(description, dummy -> true, itemSupplier, itemLabelFunc, onSelect);
    }

    public SelectionMenu(Supplier<String> dynamicDescription, Supplier<List<T>> itemSupplier, Function<T, String> itemLabelFunc, Consumer<T> onSelect) {
        this(dynamicDescription, dummy -> true, itemSupplier, itemLabelFunc, onSelect);
    }

    public SelectionMenu(Supplier<String> dynamicDescription, Predicate<User> visibleIf, Supplier<List<T>> itemSupplier, Function<T, String> itemLabelFunc, Consumer<T> onSelect) {
        this(dynamicDescription.get(), visibleIf, itemSupplier, itemLabelFunc, onSelect);
    }

    public SelectionMenu(String description, Predicate<User> visibleIf, Supplier<List<T>> itemSupplier, Function<T, String> itemLabelFunc, Consumer<T> onSelect) {
        super(description, visibleIf);
        
        // Store these for later refresh calls
        this.itemSupplier = itemSupplier;
        this.itemLabelFunc = itemLabelFunc;
        this.onSelect = onSelect;

        // cannot populateItems now. Need lazy Instantiation. If always instatiate at start, a lot of problems will occur with applicants trying to instantiate Manager menus and things not working out 
    }

    private void populateItems() {
        // Clear existing items first
        getItems().clear();
        
        List<T> items = itemSupplier.get();

        if (items == null) System.out.println("Sorry, no items");
        else {
            for (int i = 0; i < items.size(); i++) {
                T item = items.get(i);
                this.addMenuItem(itemLabelFunc.apply(item), () -> {
                    if (onSelect != null) onSelect.accept(item);
                });
            }
        }
    }

    @Override
    public void refresh() {
        // Refresh this menu by repopulating its items
        populateItems();
        
        // Call super to refresh any sub-menus
        super.refresh();
    }
}