package program;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import program.security.Password;

public class DataInitializer {
    public static void initialise() throws Exception {
        readCSV("ApplicantList.csv", Main.applicantList, "Applicant");
        readCSV("OfficerList.csv", Main.officerList, "Officer");
        readCSV("ManagerList.csv", Main.managerList, "Manager");
        readProjectsCSV("ProjectList.csv");

        System.out.println("All data successfully loaded from CSV files.");
    }

    private static void readCSV(String fileName, List<?> list, String type) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader("data/" + fileName))) {
            String line;
            boolean isFirstRow = true;

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                for (int i = 0; i < fields.length; i++) {
                    fields[i] = fields[i].trim();
                }

                if (isFirstRow) {
                    isFirstRow = false; // Skip header row
                    continue;
                }

                if (fields.length < 5 || fields.length > 6) { // Expecting 5 or 6 fields
                    throw new Exception("Invalid CSV format in " + fileName);
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
            throw new Exception("Error reading " + fileName, e);
        }
    }

    private static void readProjectsCSV(String fileName) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader("data/" + fileName))) {
            String line;
            boolean isFirstRow = true;

            while ((line = br.readLine()) != null) {
                // Handle quoted commas by using a regex to split, while considering quotes
                String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                for (int i = 0; i < fields.length; i++) {
                    // Remove leading/trailing spaces and quotes
                    fields[i] = fields[i].trim().replaceAll("^\"|\"$", "");
                }

                if (isFirstRow) {
                    isFirstRow = false; // Skip header row
                    continue;
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
            throw new Exception("Error reading " + fileName, e);
        }
    }
}
