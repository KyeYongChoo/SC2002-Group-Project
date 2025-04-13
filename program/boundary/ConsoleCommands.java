package program.boundary;

public class ConsoleCommands {

    // Open up a new page by clearing Console
    public static void clearConsole() {
        System.out.print("\033[H\033[2J\033[3J");
        System.out.flush();        
    }
}
