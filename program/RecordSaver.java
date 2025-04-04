package program;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RecordSaver {
    public static void save() throws Exception {
        writeCSV("ApplicantList.csv", Main.applicantList);
        writeCSV("OfficerList.csv", Main.officerList);
        writeCSV("ManagerList.csv", Main.managerList);
        writeProjectsCSV("ProjectList.csv");

        System.out.println("All data successfully saved to CSV files.");
    }

    public static void writeCSV(String fileName, List<?> list) throws IOException {
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
            bw.write("Project Name,Neighborhood,Type 1,Number of units for Type 1,Selling price for Type 1,Type 2,Number of units for Type 2,Selling price for Type 2,Application opening date,Application closing date,Manager,Officer Slot,Officer\n");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
            for (Project p : Main.projectList) {
                bw.write(String.join(",", new String[] { p.getName(), p.getNeighbourhood(), "2-Room", String.valueOf(p.getUnits2Room()), String.valueOf(p.getUnits2RoomPrice()), "3-Room", String.valueOf(p.getUnits3Room()), String.valueOf(p.getUnits3RoomPrice()), p.getOpenDate().format(formatter).toString(), p.getCloseDate().format(formatter).toString(), p.getManager().toString(), String.valueOf(p.getOfficerSlots()),p.getOfficers().toString()}));
                bw.newLine();
            }
        }
    }
}
