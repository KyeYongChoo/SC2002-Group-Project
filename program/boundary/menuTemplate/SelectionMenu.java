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
        super(description, user_ -> visibleIf.test(user_) &&
            itemSupplier.get()!=null && 
            !itemSupplier.get().isEmpty());
        // Store these for later refresh calls
        this.itemSupplier = itemSupplier;
        this.itemLabelFunc = itemLabelFunc;
        this.onSelect = onSelect;
    }

    private void populateItems() {
        // Clear existing items first
        getItems().clear();
        getItemSuppliers().clear();
        
        List<T> items = itemSupplier.get();

        if (items == null) System.out.println("Sorry, no items");
        else {
            items.forEach(item -> {
                this.addMenuItem(itemLabelFunc.apply(item), () -> {
                    if (onSelect != null) onSelect.accept(item);
                });
            });
            // this empty check sometimes trigger because going into the menu it was visible but coming back out from another menu its no longer visible
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