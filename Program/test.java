// package Program-Group-Project.Program ;
package Program;
import java.util.Scanner;

public class test {
    private static String password = "Hello";
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in).useDelimiter("\\A");
        String allInput = scanner.hasNext() ? scanner.next() : "";
        System.out.println("All input: " + allInput);
        
    }
}
