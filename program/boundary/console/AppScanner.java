package program.boundary.console;
import java.util.Scanner;

public class AppScanner {
    private static final Scanner sc = new Scanner(System.in);

    public static Scanner getInstance() {
        return sc;
    } 
}
