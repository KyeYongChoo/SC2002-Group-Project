package program.boundary.console;

import java.util.Scanner;

/**
 * <p>
 * The {@code AppScanner} class provides a centralized mechanism for handling user input
 * through a singleton instance of {@link java.util.Scanner}. This design ensures that
 * all user input is managed consistently across the application.
 * </p>
 *
 * <p>
 * By implementing the Singleton design pattern, {@code AppScanner} guarantees that there
 * is only one instance of the {@code Scanner} object reading from {@code System.in}. This
 * prevents potential resource conflicts that could occur if multiple {@code Scanner} instances
 * were created, which might lead to unexpected behavior such as input being skipped or read
 * incorrectly.
 * </p>
 *
 * <p>
 * The centralized input handling provided by this class simplifies the process of obtaining
 * user input, as all components of the application can access the same {@code Scanner} instance
 * through the {@code getInstance()} method. This promotes code reusability and reduces the
 * complexity associated with managing multiple input streams.
 * </p>
 *
 * </p>
 */
public class AppScanner {

    /**
     * The singleton {@code Scanner} instance used for reading user input from {@code System.in}.
     * This instance is shared across the application to maintain consistent input handling.
     */
    private static final Scanner sc = new Scanner(System.in);

    /**
     * <p>
     * Returns the shared {@link java.util.Scanner} instance for user input.
     * This method provides access to the singleton {@code Scanner}, ensuring that
     * all parts of the application use the same instance for reading input.
     * </p>
     *
     * @return the singleton {@code Scanner} instance
     */
    public static Scanner getInstance() {
        return sc;
    }
}