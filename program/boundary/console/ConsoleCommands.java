package program.boundary.console;

/**
 * <p>
 * The {@code ConsoleCommands} class provides utility methods for manipulating console output
 * in a console-based application. It is designed to enhance the user experience by offering
 * functionalities such as clearing the console screen.
 * </p>
 *
 */
public class ConsoleCommands {

    /**
     * <p>
     * Clears the console screen by printing 30 empty lines. This method creates the visual
     * effect of opening a "new page" in the console-based user interface, effectively pushing
     * any existing content out of view.
     * </p>
     */
    public static void clearConsole() {
        for (int i = 0; i < 30; i++) {
            System.out.println();
        }
    }
}