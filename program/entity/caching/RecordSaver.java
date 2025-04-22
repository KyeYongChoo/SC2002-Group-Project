package program.entity.caching;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import program.boundary.console.DateTimeFormat;
import program.control.Main;
import program.control.enquiry.Enquiry;
import program.control.enquiry.Message;
import program.control.housingApply.HousingReq;
import program.control.officerApply.AssignReq;
import program.entity.project.Project;
import program.entity.users.Applicant;
import program.entity.users.Manager;
import program.entity.users.Officer;

/**
 * <p>
 * The {@code RecordSaver} class provides functionality to save various types of
 * data into CSV files. It includes methods for saving information about users
 * (Applicants, Officers, Managers), projects, enquiries, housing requests,
 * and officer assignment requests. Each method writes the corresponding data
 * into separate CSV files stored in the "data/" directory.
 * </p>
 *
 * <p>
 * The primary purpose of this class is to facilitate the export of critical
 * data to CSV format for persistent storage, ensuring the system's data can
 * be easily accessed and analyzed in the future.
 * </p>
 *
 * <p>
 * The {@code RecordSaver} uses the {@link BufferedWriter} class to write data
 * to files and handles potential I/O exceptions by throwing {@link IOException}
 * or general {@link Exception}.
 * </p>
 *
 * @see program.entity.users.Applicant
 * @see program.entity.users.Manager
 * @see program.entity.users.Officer
 * @see program.entity.project.Project
 * @see program.control.enquiry.Enquiry
 * @see program.control.housingApply.HousingReq
 * @see program.control.officerApply.AssignReq
 */
public class RecordSaver {

    /**
     * <p>
     * Saves all the necessary data to CSV files. This includes saving the
     * lists of Applicants, Officers, Managers, Projects, Enquiries, Housing
     * Requests, and Officer Assignment Requests to separate CSV files in the
     * "data/" directory.
     * </p>
     *
     * <p>
     * The following files are created:
     * </p>
     * <ul>
     *     <li>ApplicantList.csv</li>
     *     <li>OfficerList.csv</li>
     *     <li>ManagerList.csv</li>
     *     <li>ProjectList.csv</li>
     *     <li>EnquiryList.csv</li>
     *     <li>HousingReqList.csv</li>
     *     <li>AssignReqList.csv</li>
     * </ul>
     *
     * @throws Exception if any I/O error occurs while writing to the CSV files
     */
    public static void save() throws Exception {
        writeUserCSV("ApplicantList.csv", Main.applicantList);
        writeUserCSV("OfficerList.csv", Main.officerList);
        writeUserCSV("ManagerList.csv", Main.managerList);
        writeProjectsCSV("ProjectList.csv");
        writeEnquiryCSV();
        writeHousingReqCSV("HousingReqList.csv");
        writeAssignReqCSV("AssignReqList.csv");

        System.out.println("All data successfully saved to CSV files.");
    }

    /**
     * <p>
     * Writes the provided list of users (Applicants, Officers, or Managers) to a CSV file.
     * The CSV file includes the user's name, NRIC (National Registration Identity Card),
     * age, marital status, password hash, and salt.
     * </p>
     *
     * @param fileName the name of the CSV file to write to
     * @param list the list of users to write to the file
     * @throws IOException if an I/O error occurs while writing the file
     */
    public static void writeUserCSV(String fileName, List<?> list) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/" + fileName))) {
            if (list.isEmpty()) return;

            Object firstItem = list.get(0);

            if (firstItem instanceof Applicant) {
                bw.write("Name,NRIC,Age,Marital Status,Password,Salt\n");
                for (Object obj : list) {
                    Applicant a = (Applicant) obj;
                    bw.write(a.getName() + "," + a.getUserId() + "," + a.getAge() + "," + a.getMaritalStatus() + "," + a.getPassword().getHash() + "," + a.getPassword().getSalt() + "\n");
                }
            }
            else if (firstItem instanceof Officer) {
                bw.write("Name,NRIC,Age,Marital Status,Password,Salt\n");
                for (Object obj : list) {
                    Officer o = (Officer) obj;
                    bw.write(o.getName() + "," + o.getUserId() + "," + o.getAge() + "," + o.getMaritalStatus() + "," + o.getPassword().getHash() + "," + o.getPassword().getSalt() + "\n");
                }
            }
            else if (firstItem instanceof Manager) {
                bw.write("Name,NRIC,Age,Marital Status,Password,Salt\n");
                for (Object obj : list) {
                    Manager m = (Manager) obj;
                    bw.write(m.getName() + "," + m.getUserId() + "," + m.getAge() + "," + m.getMaritalStatus() + "," + m.getPassword().getHash() + "," + m.getPassword().getSalt() + "\n");
                }
            }
        }
    }

    /**
     * <p>
     * Writes the list of projects to a CSV file, including details such as project
     * name, neighborhood, room types, unit numbers, selling prices, application
     * dates, and manager and officer information.
     * </p>
     *
     * @param fileName the name of the CSV file to write to
     * @throws IOException if an I/O error occurs while writing the file
     */
    public static void writeProjectsCSV(String fileName) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/" + fileName))) {
            // Write the CSV header
            bw.write("Project Name,Neighborhood,Type 1,Number of units for Type 1,Selling price for Type 1,Type 2,Number of units for Type 2,Selling price for Type 2,Application opening date,Application closing date,Manager,Officer Slot,Officer,Visibility\n");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");

            // Write each project to the CSV file
            for (Project p : Main.projectList) {
                // Convert the officer list to a comma-separated string
                String officerList = p.getOfficers().isEmpty() 
                    ? "" 
                    : String.join(",", p.getOfficers().stream().map(Object::toString).toArray(CharSequence[]::new));

                // Wrap the officer list in quotes if it contains more than one officer
                if (p.getOfficers().size() > 1) {
                    officerList = "\"" + officerList + "\"";
                }

                // Write the project details to the CSV file
                bw.write(String.join(",", new String[] {
                    p.getName(),
                    p.getNeighbourhood(),
                    "2-Room",
                    String.valueOf(p.getUnits2Room()),
                    String.valueOf(p.getUnits2RoomPrice()),
                    "3-Room",
                    String.valueOf(p.getUnits3Room()),
                    String.valueOf(p.getUnits3RoomPrice()),
                    p.getOpenDate().format(formatter),
                    p.getCloseDate().format(formatter),
                    p.getManager().toString(),
                    String.valueOf(p.getOfficerSlots()),
                    officerList,
                    String.valueOf(p.getVisibility()) // Save visibility
                }));
                bw.newLine();
            }
        }
    }

    /**
     * <p>
     * Writes the list of enquiries and their corresponding messages to a CSV file.
     * Each enquiry is written along with its ID, user ID, project ID, and creation
     * date. Messages within each enquiry are also written, with details such as the
     * user ID, timestamp, and message text.
     * </p>
     *
     * @throws IOException if an I/O error occurs while writing the file
     */
    public static void writeEnquiryCSV() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/EnquiryList.csv"))) {
            DateTimeFormatter formatter = DateTimeFormat.getDateTimeFormatter();
            bw.write("Enquiry ID|User ID|Project ID|Date Created\n");
            for (Enquiry e : Main.enquiryList) {
                bw.write(String.join("|", new String[] {
                        String.valueOf(e.getId()),
                        e.getUser().getUserId(),
                        e.getProject().getName(),
                        e.getDateCreated().format(formatter).toString()
                }));
                bw.newLine();
                for (Message m : e) {
                    bw.write(String.join("|", new String[] {
                            "MESSAGE",
                            m.getUser().getUserId(),
                            m.getTimeStamp().format(formatter).toString(),
                            m.getText()
                    }));
                    bw.newLine();
                }
            }
        }
    }

    /**
     * <p>
     * Writes the list of housing requests to a CSV file. Each request includes details
     * such as user ID, project name, room type, request status, withdrawal status,
     * and the officer who approved the request.
     * </p>
     *
     * @param fileName the name of the CSV file to write to
     * @throws IOException if an I/O error occurs while writing the file
     */
    public static void writeHousingReqCSV(String fileName) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/" + fileName))) {
            bw.write("User ID,Project Name,Room Type,Request Status,Withdrawal Status,Approved By\n");
            for (HousingReq req : Main.housingReqList) {
                bw.write(String.join(",", new String[] {
                        req.getUser().getUserId(),
                        req.getProject().getName(),
                        req.getRoomType().toString(),
                        req.getStatus().toString(),
                        req.getWithdrawalStatus().toString(),
                        req.getApprovedBy() == null ? "" : req.getApprovedBy().getUserId()
                }));
                bw.newLine();
            }
        }
    }

    /**
     * <p>
     * Writes the list of officer assignment requests to a CSV file. Each request
     * includes the officer ID, project name, and application status.
     * </p>
     *
     * @param fileName the name of the CSV file to write to
     * @throws IOException if an I/O error occurs while writing the file
     */
    public static void writeAssignReqCSV(String fileName) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/" + fileName))) {
            bw.write("Officer ID,Project Name,Application Status\n");
            for (AssignReq req : Main.assignReqList) {
                bw.write(String.join(",", new String[] {
                        req.getOfficer().getUserId(),
                        req.getProject().getName(),
                        req.getApplicationStatus().toString()
                }));
                bw.newLine();
            }
        }
    }
}
