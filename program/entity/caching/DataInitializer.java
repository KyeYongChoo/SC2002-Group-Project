package program.entity.caching;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import program.control.Main;
import program.control.UserFetcher;
import program.control.interclass.Enquiry;
import program.control.interclass.Message;
import program.control.security.Password;
import program.entity.project.Project;
import program.entity.project.ProjectList;
import program.entity.users.Applicant;
import program.entity.users.Manager;
import program.entity.users.Officer;


public class DataInitializer {
    public static void initialise() {
        try{
            readUserCSV("ApplicantList.csv", Main.applicantList, "Applicant");
            readUserCSV("OfficerList.csv", Main.officerList, "Officer");
            readUserCSV("ManagerList.csv", Main.managerList, "Manager");
            readProjectsCSV("ProjectList.csv");
            readEnquiryCSV();

        }catch (Exception e){
            // Handle exceptions related to file reading and parsing
            System.out.println("Error loading data from CSV files: " + e.getMessage());
        }

        System.out.println("All data successfully loaded from CSV files.");
    }

    private static void readUserCSV(String fileName, List<?> list, String type) {
        try (BufferedReader br = new BufferedReader(new FileReader("data/" + fileName))) {
            String line;
            
            // Skip the header
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                for (int i = 0; i < fields.length; i++) {
                    fields[i] = fields[i].trim();
                }

                if (fields.length < 5 || fields.length > 6) { // Expecting 5 or 6 fields
                    throw new IOException("Invalid CSV format in " + fileName + ": " + Arrays.toString(fields));
                }

                Password password;
                if (fields.length == 6) {
                    // If salt is provided
                    password = new Password(fields[4], fields[5]);
                } else {
                    // If salt is missing, generate a new salt
                    password = new Password(fields[4]);
                }

                switch (type) {
                    case "Applicant" -> Main.applicantList.add(new Applicant(fields[1], fields[0], Integer.parseInt(fields[2]), fields[3], password));
                    case "Officer" -> Main.officerList.add(new Officer(fields[1], fields[0], Integer.parseInt(fields[2]), fields[3], password));
                    case "Manager" -> Main.managerList.add(new Manager(fields[1], fields[0], Integer.parseInt(fields[2]), fields[3], password));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading " + fileName + " " + e.getMessage());
        }  catch (Exception e) {
            System.out.println("Error parsing " + fileName + " " + e.getMessage());
        }
    }

    private static void readProjectsCSV(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader("data/" + fileName))) {
            String line;
            
            // Skip the header
            br.readLine();

            while ((line = br.readLine()) != null) {
                // Handle quoted commas by using a regex to split, while considering quotes
                String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                for (int i = 0; i < fields.length; i++) {
                    // Remove leading/trailing spaces and quotes
                    fields[i] = fields[i].trim().replaceAll("^\"|\"$", "");
                }

                if (fields.length != 13) {
                    throw new Exception("Invalid CSV format in " + fileName);
                }

                if ("".equals(fields[2]) && "2-Room".equals(fields[5])) 
                    Main.projectList.add(new Project(fields[0], fields[1], fields[6], fields[7], fields[4], fields[5], fields[8], fields[9], fields[10], fields[11], fields[12]));
                if ("2-Room".equals(fields[2]) && "".equals(fields[5])) 
                    Main.projectList.add(new Project(fields[0], fields[1], fields[3], fields[4], fields[6], fields[7], fields[8], fields[9], fields[10], fields[11], fields[12]));
                if ("".equals(fields[2]) && "3-Room".equals(fields[5])) 
                    Main.projectList.add(new Project(fields[0], fields[1], fields[3], fields[4], fields[6], fields[7], fields[8], fields[9], fields[10], fields[11], fields[12]));
                if ("3-Room".equals(fields[2]) && "".equals(fields[5])) 
                    Main.projectList.add(new Project(fields[0], fields[1], fields[6], fields[7], fields[4], fields[5], fields[8], fields[9], fields[10], fields[11], fields[12]));
                if ("2-Room".equals(fields[2]) && "3-Room".equals(fields[5])) 
                    Main.projectList.add(new Project(fields[0], fields[1], fields[3], fields[4], fields[6], fields[7], fields[8], fields[9], fields[10], fields[11], fields[12]));
                if ("3-Room".equals(fields[2]) && "2-Room".equals(fields[5])) 
                    Main.projectList.add(new Project(fields[0], fields[1], fields[6], fields[7], fields[4], fields[5], fields[8], fields[9], fields[10], fields[11], fields[12]));

                if (fields[2].equals(fields[5])) throw new Exception("Overlapping Room types");
            }
        } catch (IOException e) {
            System.out.println("Error reading " + fileName + " " + e.getMessage());
        }  catch (Exception e) {
            System.out.println("Error parsing " + fileName + " " + e.getMessage());
        }
    }

    private static void readEnquiryCSV() {
        File file = new File("data/EnquiryList.csv");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HH:mm:ss");

        // Check if the file exists
        if (!file.exists()){
            try{
                file.createNewFile(); // Create the file if it doesn't exist
            } catch (IOException e) {
                System.out.println("Error creating EnquiryList.csv: " + e.getMessage());
                System.exit(1);
            }
            return; // Exit if the file doesn't exist - new file is empty
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader("data/EnquiryList.csv"))) {
            String line;
            Enquiry currentEnquiry = null; // Initialize currentEnquiry
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");

                if (parts[0].equals("MESSAGE")) {
                    // Parse a Message
                    if (currentEnquiry != null) {
                        Message message = new Message(
                            UserFetcher.fetch(parts[1]), // User
                            parts[3], // Message Text
                            LocalDateTime.parse(parts[2], formatter) // Timestamp
                        );
                        currentEnquiry.add(message); 
                    }
                } else {
                    // Parse an Enquiry
                    currentEnquiry = new Enquiry(
                        Integer.parseInt(parts[0]), // Enquiry ID
                        UserFetcher.fetch(parts[1]), // Assuming a method to fetch user by ID
                        Main.projectList.get(parts[2]), // Assuming a method to fetch project by name
                        LocalDateTime.parse(parts[3], formatter)
                    );
                    Main.enquiryList.add(currentEnquiry); // Add to the main enquiry list
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading EnquiryList.csv :" + e.getMessage());
        }  catch (Exception e) {
            System.out.println("Error parsing EnquiryList.csv :" + e.getMessage());
        }
    }
}
