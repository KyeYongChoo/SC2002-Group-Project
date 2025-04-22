package program.entity.caching;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import program.boundary.console.DateTimeFormat;
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

/**
 * The {@code DataInitializer} class is responsible for loading data from CSV files into various
 * in-memory lists of users, projects, assignments, and housing requests. It reads CSV files
 * and parses them into appropriate objects for use within the application.
 * <p>
 * The {@code initialise} method orchestrates the entire process of reading the CSV files, while
 * various helper methods are responsible for reading and parsing specific types of data (users,
 * projects, enquiries, etc.).
 * </p>
 *
 * @see Main for the application’s main data structures like lists of users, projects, and requests.
 * @see UserFetcher for methods related to user fetching.
 */
public class DataInitializer {

    /**
     * Initializes the application by reading all the required data from CSV files and populating
     * various in-memory lists. This includes users (Applicants, Officers, Managers), projects,
     * assignments, and housing requests.
     * <p>
     * The method also ensures that the required data is read in the correct order:
     * <ul>
     *   <li>Users (Applicants, Officers, Managers) are loaded first.</li>
     *   <li>Projects are loaded next, since they may involve users.</li>
     *   <li>Assignments and Housing requests are loaded last to ensure proper referencing.</li>
     * </ul>
     * </p>
     *
     * @throws Exception If there are errors during reading or parsing any of the CSV files.
     */
    public static void initialise() {
        try{
            // reorder at your own risk
            readUserCSV("ApplicantList.csv", Main.applicantList, "Applicant");
            readUserCSV("OfficerList.csv", Main.officerList, "Officer");
            readUserCSV("ManagerList.csv", Main.managerList, "Manager");

            // must happen after loading Officer and Manager due to ProjectList.csv also holding officer and Manager info
            readProjectsCSV("ProjectList.csv");
            readEnquiryCSV();

            // must be read after projectList due to identify via projects
            readAssignReqCSV("AssignReqList.csv");
            readHousingReqCSV("HousingReqList.csv");

        }catch (Exception e){
            // Handle exceptions related to file reading and parsing
            System.out.println("Error loading data from CSV files: " + e.getMessage());
        }

        System.out.println("Completed loading of CSV files.");
    }

    /**
     * Reads a CSV file containing user data (Applicants, Officers, or Managers) and populates the
     * corresponding list with user objects.
     * <p>
     * This method handles both the case where the CSV contains a password salt (6 fields) or
     * when the salt is missing (5 fields). It also validates the format of the CSV file.
     * </p>
     *
     * @param fileName The name of the CSV file to read.
     * @param list The list to populate with user objects (Applicants, Officers, or Managers).
     * @param type The type of user to read ("Applicant", "Officer", or "Manager").
     * @throws IOException If an I/O error occurs during reading the file.
     * @throws Exception If there is an error parsing the user data.
     */
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

    /**
     * Creates a {@link Project} object from the fields extracted from a CSV file.
     * <p>
     * This method validates the format of the fields and handles various combinations of room
     * types to correctly instantiate a project.
     * </p>
     *
     * @param fields The fields from the CSV file corresponding to a single project.
     * @param fileName The name of the CSV file for logging errors.
     * @return A newly created {@link Project} object.
     * @throws Exception If the project data is in an invalid format or contains unexpected room types.
     */
    private static Project createProject(String[] fields, String fileName) throws Exception {
        try {
            if (fields.length != 14) {
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

    /**
     * Reads a CSV file containing project data and populates the project list.
     * <p>
     * This method reads the CSV file and creates {@link Project} objects, linking officers to
     * projects and handling assignments.
     * </p>
     *
     * @param fileName The name of the CSV file to read.
     */
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
                    fields = Arrays.copyOf(fields, 14); // Extend to include visibility
                    fields[12] = ""; // Officer list
                    fields[13] = ""; // Visibility
                } else if (fields.length == 13) {
                    fields = Arrays.copyOf(fields, 14); // Extend to include visibility
                    fields[13] = ""; // Visibility
                }

                try {
                    Project project = createProject(fields, fileName);
                    Main.projectList.add(project);

                    // Set visibility if provided
                    if (!fields[13].isEmpty()) {
                        project.setVisibility(Boolean.parseBoolean(fields[13]));
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

    /**
     * Reads the enquiry CSV file and populates the list of enquiries.
     * <p>
     * This method creates enquiry objects and adds messages if they exist.
     * </p>
     *
     * @throws IOException If there is an issue reading the enquiry file.
     */
    private static void readEnquiryCSV() {
        File file = new File("data/EnquiryList.csv");
        DateTimeFormatter formatter = DateTimeFormat.getDateTimeFormatter();

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

    /**
     * Reads housing request data from a CSV file and adds the housing requests to the main list.
     * <p>
     * This method processes each housing request and handles scenarios like missing "approvedBy"
     * fields.
     * </p>
     *
     * @param fileName The name of the housing request CSV file to read.
     */
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
                req.setApprovedBy((Manager) approvedBy);

                Main.housingReqList.add(req);
            }
        } catch (IOException e) {
            System.out.println("Error reading " + fileName + ": " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error parsing " + fileName + ": " + e.getMessage());
        }
    }

    /**
     * Reads assignment request data from a CSV file and adds the assignment requests to the
     * main list.
     * <p>
     * This method processes each assignment request, checking for valid officer and project
     * references.
     * </p>
     *
     * @param fileName The name of the assignment request CSV file to read.
     */
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
                if (project == null) throw new Exception("Hey the project " + fields[0] + " is missing.\n" + "Projects available for reference: " + Main.projectList.stream());
                AssignReq.APPLICATION_STATUS applicationStatus = AssignReq.APPLICATION_STATUS.valueOf(fields[2]);

                AssignReq req = new AssignReq(officer, project);
                req.setApplicationStatus(applicationStatus);
                
                Main.assignReqList.superAdd(req);
            }
            Main.projectList.forEach(project_ -> {
                // Stream through the officers assigned to the project
                project_.getOfficers().stream()
                    // Filter out officers who already have an accepted assignment request for this project
                    .filter(officer_ -> {
                        boolean hasAcceptedRequest = Main.assignReqList.stream()
                            .anyMatch(req -> 
                                req.getOfficer().equals(officer_) &&
                                req.getProject().equals(project_) &&
                                req.getApplicationStatus() == AssignReq.APPLICATION_STATUS.accepted
                            );
                        // System.out.println("Officer: " + officer_ + " has accepted request: " + hasAcceptedRequest);
                        return !hasAcceptedRequest;
                    })
                    // Peek to log officers who pass the filter
                    // .peek(officer_ -> System.out.println("Adding new assignment request for officer: " + officer_))
                    // For each officer who passes the filter, create a new accepted assignment request
                    .forEach(officer_ -> {
                        AssignReq req = new AssignReq((Officer) officer_, project_);
                        req.setApplicationStatus(AssignReq.APPLICATION_STATUS.accepted);
                        Main.assignReqList.superAdd(req);
                        // System.out.println("New assignment request added: " + req);
                    });
            });
        } catch (IOException e) {
            System.out.println("Error reading " + fileName + ": " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error parsing " + fileName + ": " + e.getMessage());
        }
    }
}
