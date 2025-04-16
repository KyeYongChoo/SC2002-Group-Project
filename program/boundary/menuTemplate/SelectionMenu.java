package program.boundary.menuTemplate;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import program.entity.users.User;

public class SelectionMenu<T> extends MenuGroup {

    public SelectionMenu(String description, List<T> items, Function<T, String> itemLabelFunc, Consumer<T> onSelect) {
        this(description, dummy -> true ,items, itemLabelFunc, onSelect);
    }
    public SelectionMenu(String description, Predicate<User> visibleIf, List<T> items, Function<T, String> itemLabelFunc, Consumer<T> onSelect) {
        super(description, visibleIf);

        if (items == null) System.out.println("Sorry, no " + items.getClass().getSimpleName() + "applicable");
        for (int i = 0; i < items.size(); i++) {
            T item = items.get(i);
            this.addMenuItem(itemLabelFunc.apply(item), () -> {
                if (onSelect != null) onSelect.accept(item);
            });
        }
    }
}
