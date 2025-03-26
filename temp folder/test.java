// package program-Group-Project.program ;
package program;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Test {
    public static void main(String[] args) {
        // Define the DateTimeFormatter with a pattern that matches both formats
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");

        // Test input string
        String date1 = "15/2/2025";  // Format: day/month/year
        String date2 = "15/02/2025"; // Format: day/month/year
        
        try {
            // Parse the dates
            LocalDate parsedDate1 = LocalDate.parse(date1, formatter);
            LocalDate parsedDate2 = LocalDate.parse(date2, formatter);

            // Print the parsed dates
            System.out.println("Parsed date 1: " + parsedDate1);
            System.out.println("Parsed date 2: " + parsedDate2);
        } catch (DateTimeParseException e) {
            // Catch parse exception and print message
            System.out.println("Error parsing date: " + e.getMessage());
        }
    }
}
