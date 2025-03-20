package Program;

import java.util.HashMap;
import java.util.Scanner;

/*
 * Example Text:
 * Name	NRIC	Age	Marital Status	Password
John	S1234567A	35	Single	password
Sarah	T7654321B	40	Married	password
Grace	S9876543C	37	Married	password
James	T2345678D	30	Married	password
Rachel	S3456789E	25	Single	password
 */
public class MainActivity {
    public static void main(String[] args) throws Exception{
        System.out.println("\nInitialization: ");
        System.out.println("Enter user data");
        System.out.println("\n1. Please access the Excel file");
        System.out.println("2. Press Ctrl+A then Ctrl+C");
        System.out.println("3. Navigate back to this java terminal");
        System.out.println("4. Press Ctrl+V");
        System.out.println("5a. (If on Windows) Press Ctrl+Z then Enter");
        System.out.println("5b. (If on Linux/MacOS) Press Ctrl+D");

        Scanner scanner = new Scanner(System.in).useDelimiter("\\A");
        String allInput = scanner.hasNext() ? scanner.next() : "";

        HashMap <String, User> UserList = new HashMap<>();;
        for (String line : allInput.split("\n")) {
            String[] fields = line.split("\t");
            if (fields[0].equals("Name") || fields[0].equals("\n")){ 
                // If in the first row full of column names or empty, skip
                continue;
            }
            if (fields.length != 5) throw new Exception ("Please enter all 5 fields");
            UserList.put(fields[1],new User(fields[1].trim(), fields[0].trim(), Integer.parseInt(fields[2].trim()), fields[3].trim(), fields[4].trim()));
        }
        

    }
}
