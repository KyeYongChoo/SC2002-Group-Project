package program.entity.caching;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import program.control.Main;
import program.control.enquiry.Enquiry;
import program.control.enquiry.Message;
import program.control.housingApply.HousingReq;
import program.control.officerApply.AssignReq;
import program.control.security.Password;
import program.control.security.UserFetcher;
import program.entity.project.Project;
import program.entity.users.Applicant;
import program.entity.users.Manager;
import program.entity.users.Officer;
import program.entity.users.User;


public class DataInitializer {
    public static void initialise() {
        try{
            readAssignReqCSV("AssignReqList.csv");
            readUserCSV("ApplicantList.csv", Main.applicantList, "Applicant");
            readUserCSV("OfficerList.csv", Main.officerList, "Officer");
            readUserCSV("ManagerList.csv", Main.managerList, "Manager");
            readProjectsCSV("ProjectList.csv");
            readEnquiryCSV();
            readHousingReqCSV("HousingReqList.csv");

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

    private static Project createProject(String[] fields, String fileName) throws Exception {
        try {
            if (fields.length == 12) {
                fields = Arrays.copyOf(fields, 13);
                fields[12] = "";
            }
            if (fields.length != 13) {
                throw new Exception("Invalid CSV format in " + fileName);
            }

            if ("".equals(fields[2]) && "2-Room".equals(fields[5])) {
                return new Project(fields[0], fields[1], fields[6], fields[7], fields[4], fields[5], fields[8], fields[9], fields[10], fields[11], fields[12]);
            } else if ("2-Room".equals(fields[2]) && "".equals(fields[5])) {
                return new Project(fields[0], fields[1], fields[3], fields[4], fields[6], fields[7], fields[8], fields[9], fields[10], fields[11], fields[12]);
            } else if ("".equals(fields[2]) && "3-Room".equals(fields[5])) {
                return new Project(fields[0], fields[1], fields[3], fields[4], fields[6], fields[7], fields[8], fields[9], fields[10], fields[11], fields[12]);
            } else if ("3-Room".equals(fields[2]) && "".equals(fields[5])) {
                return new Project(fields[0], fields[1], fields[6], fields[7], fields[4], fields[5], fields[8], fields[9], fields[10], fields[11], fields[12]);
            } else if ("2-Room".equals(fields[2]) && "3-Room".equals(fields[5])) {
                return new Project(fields[0], fields[1], fields[3], fields[4], fields[6], fields[7], fields[8], fields[9], fields[10], fields[11], fields[12]);
            } else if ("3-Room".equals(fields[2]) && "2-Room".equals(fields[5])) {
                return new Project(fields[0], fields[1], fields[6], fields[7], fields[4], fields[5], fields[8], fields[9], fields[10], fields[11], fields[12]);
            } else if (fields[2].equals(fields[5])) {
                throw new Exception("Overlapping Room types in project: " + fields[0]);
            } else {
                throw new Exception("Unexpected room type combination in project: " + fields[0]);
            }
        } catch (Exception e) {
            System.out.println("Error creating project for line: " + Arrays.toString(fields));
            throw e; // Re-throw the exception to be handled by the caller
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

                // Properly trim and remove surrounding quotes from all fields
                for (int i = 0; i < fields.length; i++) {
                    fields[i] = fields[i].trim().replaceAll("^\"|\"$", "");
                }

                if (fields.length == 12) {
                    fields = Arrays.copyOf(fields, 13);
                    fields[12] = "";
                }

                try {
                    Project project = createProject(fields, fileName);
                    Main.projectList.add(project);

                    // Check for existing successful assignments
                    String[] officers = fields[12].split(",");
                    for (String officerName : officers) {
                        officerName = officerName.trim();
                        if (!officerName.isEmpty()) {
                            final String officerNameFinal = officerName;
                            Officer officer = (Officer) Main.officerList.stream().filter(user -> user.getName().equals(officerNameFinal)).findAny().orElse(null);
                            boolean hasSuccessfulAssignment = Main.assignReqList.stream()
                                .anyMatch(req -> req.getOfficer().equals(officer) && req.getProject().equals(project) && req.getApplicationStatus() == AssignReq.APPLICATION_STATUS.accepted);

                            // Create a successful assignment if none exists
                            if (!hasSuccessfulAssignment) {
                                AssignReq req = new AssignReq(officer, project);
                                req.setApplicationStatus(AssignReq.APPLICATION_STATUS.accepted);
                                Main.assignReqList.add(req);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Skipping invalid project entry: " + Arrays.toString(fields) + "\n" + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading " + fileName + ": " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error parsing " + fileName + ": " + e.getMessage());
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

    private static void readHousingReqCSV(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader("data/" + fileName))) {
            String line;

            // Skip the header
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");

                if (fields.length == 5){
                    // in case approvedBy field empty
                    fields = Arrays.copyOf(fields,6);
                    fields[5] = "";
                }

                if (fields.length != 6) {
                    throw new IOException("Invalid CSV format in " + fileName);
                }

                User user = Main.applicantList.get(fields[0]);
                Project project = Main.projectList.get(fields[1]);
                Project.ROOM_TYPE roomType = Project.ROOM_TYPE.valueOf(fields[2]);
                HousingReq.REQUEST_STATUS requestStatus = HousingReq.REQUEST_STATUS.valueOf(fields[3]);
                HousingReq.WITHDRAWAL_STATUS withdrawalStatus = HousingReq.WITHDRAWAL_STATUS.valueOf(fields[4]);
                Officer approvedBy = fields[5].isEmpty() ? null : (Officer) Main.officerList.get(fields[5]);

                HousingReq req = new HousingReq(user, project, roomType);
                req.setStatus(requestStatus);
                req.setWithdrawalStatus(withdrawalStatus);
                req.setApprovedBy(approvedBy);

                Main.reqList.add(req);
            }
        } catch (IOException e) {
            System.out.println("Error reading " + fileName + ": " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error parsing " + fileName + ": " + e.getMessage());
        }
    }

    private static void readAssignReqCSV(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader("data/" + fileName))) {
            String line;

            // Skip the header
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length != 3) {
                    throw new IOException("Invalid CSV format in " + fileName);
                }

                Officer officer = (Officer) Main.officerList.get(fields[0]);
                Project project = Main.projectList.get(fields[1]);
                AssignReq.APPLICATION_STATUS applicationStatus = AssignReq.APPLICATION_STATUS.valueOf(fields[2]);

                AssignReq req = new AssignReq(officer, project);
                req.setApplicationStatus(applicationStatus);

                Main.assignReqList.add(req);
            }
        } catch (IOException e) {
            System.out.println("Error reading " + fileName + ": " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error parsing " + fileName + ": " + e.getMessage());
        }
    }
}
