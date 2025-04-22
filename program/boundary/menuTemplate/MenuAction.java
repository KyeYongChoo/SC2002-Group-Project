package program.boundary.menuTemplate;

/**
 * {@code MenuAction} is a functional interface that defines a contract for actions
 * that can be executed when a menu item is selected.
 *
 * <p>Think of this as defining a standard for what should happen when someone clicks
 * on a button or chooses an option in a menu. Any piece of code that needs to be run
 * as a result of a menu interaction can be implemented using this interface.</p>
 *
 * <p>Because {@code MenuAction} is a {@link FunctionalInterface}, it has only one
 * abstract method (`execute()`). This special property allows you to use concise
 * <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html">lambda expressions</a>
 * to create instances of {@code MenuAction}. For example, instead of writing a whole
 * class that implements {@code MenuAction}, you can simply provide the code to be
 * executed directly.</p>
 *
 * @author Choo Kye Yong
 * @version 1.0
 */
@FunctionalInterface
public interface MenuAction {

    /**
     * Executes the specific action associated with a menu item.
     *
     * <p>This is the core method of the {@code MenuAction} interface. Implementations
     * of this method will contain the actual logic that needs to be performed when
     * a corresponding menu item is activated. For instance, it could involve displaying
     * information, processing user input, or navigating to another menu.</p>
     */
    void execute();

    /**
     * Returns a new {@code MenuAction} that combines the current action with another
     * specified action, executing them sequentially.
     *
     * <p>This default method allows you to chain multiple actions together. When the
     * resulting composed {@code MenuAction} is executed, the {@code execute()} method
     * of the current {@code MenuAction} will be called first, and then the {@code execute()}
     * method of the {@code after} {@code MenuAction} will be called.</p>
     *
     * @param after The {@code MenuAction} to be executed *after* this current action completes.
     * Passing {@code null} will result in only the current action being executed.
     * @return A new {@code MenuAction} instance that represents the sequential execution
     * of this action followed by the {@code after} action.
     */
    default MenuAction andThen(MenuAction after) {
        return () -> {
            // First, execute the current action.
            this.execute();
            // Then, if the 'after' action is provided, execute it.
            if (after != null) {
                after.execute();
            }
        };
    }
}
