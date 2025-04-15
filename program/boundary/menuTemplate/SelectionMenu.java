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

        for (int i = 0; i < items.size(); i++) {
            T item = items.get(i);
            String label = String.format("%d. %s", i + 1, itemLabelFunc.apply(item));
            this.addMenuItem(label, () -> {
                if (onSelect != null) onSelect.accept(item);
            });
        }
    }
}
