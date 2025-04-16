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

public class RecordSaver {
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
    

    public static void writeProjectsCSV(String fileName) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/" + fileName))) {
            // Write the CSV header
            bw.write("Project Name,Neighborhood,Type 1,Number of units for Type 1,Selling price for Type 1,Type 2,Number of units for Type 2,Selling price for Type 2,Application opening date,Application closing date,Manager,Officer Slot,Officer\n");
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
                    officerList
                }));
                bw.newLine();
            }
        }
    }

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
