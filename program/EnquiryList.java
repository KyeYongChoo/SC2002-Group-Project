package program;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Similar to HousingReqList, there will be a separate enquiryList for each of the User, Project, and Main classes
 */
public class EnquiryList extends ArrayList<Enquiry>{
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public static Enquiry selectEnquiry(User client){
        Scanner sc = new Scanner(System.in);
        System.out.println("Please choose project to enquire about: ");
        ProjectList.printVisible(client);
        Project targetProject = null;
        targetProject = Main.projectList.get(sc.nextLine());
        while(targetProject == null){
            System.out.println("Please input the project name only: ");
            targetProject = Main.projectList.get(sc.nextLine());
        }
        return get(client,targetProject);
    }
    public static Enquiry get(User client, Project project){
        Enquiry targetEnquiry = null;
        for (Enquiry enquiry : Main.enquiryList){
            if (enquiry.getUser() == client && enquiry.getProject() == project){
                if (targetEnquiry == null){
                    targetEnquiry = enquiry;
                }else{
                    // if there is duplicate
                    targetEnquiry = getDisambiguation(client, project);
                    break;
                }
            }
        }
        return targetEnquiry;
    }

    public static Enquiry getDisambiguation(User client, Project project){
        EnquiryList enqList = new EnquiryList();
        for (Enquiry enquiry : Main.enquiryList){
            if (enquiry.getUser() == client && enquiry.getProject() == project){
                enqList.superAdd(enquiry);
            }
        }
        for (int loopCounter = 0; loopCounter < enqList.size(); loopCounter++){
            Enquiry enquiry = enqList.get(loopCounter);
            System.out.println("\n" + (loopCounter + 1) + ".\nCreated on " + enquiry.getDateCreated().format(formatter));
            System.out.println("For project " + enquiry.getProject());
            System.out.println("First Message: " + enquiry.get(0).getText());
        }
        System.out.println("\n\nPlease choose which is the relevant enquiry: ");
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        do { 
            try {
                choice = Integer.parseInt(sc.nextLine()) - 1;
            } catch (Exception e) {
                continue;
            }
        } while (choice < 0 || choice >= enqList.size());
        return enqList.get(choice);

    }


    @Override
    public boolean add(Enquiry enquiry){
        // Used add(index, enquiry) syntax instead of add(enquiry) syntax for 2 reasons
        // 1. This function overrides add(enquiry) signature, meaning using that will cause infinite recursion
        // 2. Newest enquiries at the front
        enquiry.getProject().getEnquiryList().add(0,enquiry);
        enquiry.getUser().getEnquiryList().add(0,enquiry);
        Main.enquiryList.add(0,enquiry);
        return true;
    }

    public static void printPastEnq(User client){
        if (client.getEnquiryList().equals(new EnquiryList())){
            System.out.println("You have no enquiries yet.");
            return;
        }
        
        for(Enquiry enquiry : client.getEnquiryList()){
            System.out.println("\nCreated on " + enquiry.getDateCreated().format(formatter));
            System.out.println("For project " + enquiry.getProject());
            for (Message m : enquiry){
                System.out.println(m.getTimeStamp().format(formatter) + ": " + m.getUser() + ": " + m.getText());
            }
        }
    }

    public boolean add(Applicant client, String enquiry, Project project){
        Enquiry enq = new Enquiry(client, enquiry, project);
        return this.add(enq);
    }

    // Used if need to modify in place ie avoid java.util.ConcurrentModificationException
    public void superAdd(Enquiry e){
        super.add(e);
    }

    public static void delete(Enquiry enq){
        if (enq == null) {
            System.out.println("Error: no Enquiries found");
            return;
        }
        enq.getProject().getEnquiryList().remove(enq);
        enq.getUser().getEnquiryList().remove(enq);
        Main.enquiryList.remove(enq);
        System.out.println("\nRemoval Successful");
    }

}
