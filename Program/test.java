// package Program-Group-Project.Program ;
package Program;

public class Test {
    private static String password = "Hello";
    public static void main(String[] args) {
        String formatTableRef = String.format("%-20s %-20s %-15s %-15s %-30s %-10s %-15s %-30s","Project Name","Neighbourhood","No. Units","Selling Price","Application Opening Date","Manager","Officer Slot","Officer");
        System.out.printf(formatTableRef);
        
    }
}
