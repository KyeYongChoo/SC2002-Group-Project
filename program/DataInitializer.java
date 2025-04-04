package program;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

                if (fields.length != 5) {
                    throw new Exception("Invalid CSV format in " + fileName);
                }

                switch (type) {
                    case "Applicant" -> Main.applicantList.add(new Applicant(fields[1], fields[0], Integer.parseInt(fields[2]), fields[3], fields[4]));
                    case "Officer" -> Main.officerList.add(new Officer(fields[1], fields[0], Integer.parseInt(fields[2]), fields[3], fields[4]));
                    case "Manager" -> Main.managerList.add(new Manager(fields[1], fields[0], Integer.parseInt(fields[2]), fields[3], fields[4]));
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

    public static void writeCSV(String fileName, List<?> list) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            if (list.isEmpty()) return;

            Object firstItem = list.get(0);

            if (firstItem instanceof Applicant) {
                bw.write("Name,NRIC,Age,Marital Status,Password\n");
                for (Object obj : list) {
                    Applicant a = (Applicant) obj;
                    bw.write(a.getName() + "," + a.getUserId() + "," + a.getAge() + "," + a.getMaritalStatus() + "," + a.getPassword() + "\n");
                }
            } 
            else if (firstItem instanceof Officer) {
                bw.write("Name,NRIC,Age,Marital Status,Password\n");
                for (Object obj : list) {
                    Officer o = (Officer) obj;
                    bw.write(o.getName() + "," + o.getUserId() + "," + o.getAge() + "," + o.getMaritalStatus() + "," + o.getPassword() + "\"\n");
                }
            } 
            else if (firstItem instanceof Manager) {
                bw.write("Name,NRIC,Age,Marital Status,Password\n");
                for (Object obj : list) {
                    Manager m = (Manager) obj;
                    bw.write(m.getName() + "," + m.getUserId() + "," + m.getAge() + "," + m.getMaritalStatus() + "," + m.getPassword() + "\n");
                }
            }
        }
    }
    

    public static void writeProjectsCSV(String fileName) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write("Project Name,Neighborhood,Type 1,Number of units for Type 1,Selling price for Type 1,Type 2,Number of units for Type 2,Selling price for Type 2,Application opening date,Application closing date,Manager,Officer Slot,Officer\n");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
            for (Project p : Main.projectList) {
                bw.write(String.join(",", new String[] { p.getName(), p.getNeighbourhood(), "2-Room", String.valueOf(p.getUnits2Room()), String.valueOf(p.getUnits2RoomPrice()), "3-Room", String.valueOf(p.getUnits3Room()), String.valueOf(p.getUnits3RoomPrice()), p.getOpenDate().format(formatter).toString(), p.getCloseDate().format(formatter).toString(), p.getManager().toString(), String.valueOf(p.getOfficerSlots()), p.getOfficers().toString() }));
                bw.newLine();
            }
        }
    }
}
