package program.control.interclass;

import java.time.LocalDateTime;
import java.util.ArrayList;

import program.entity.project.Project;
import program.entity.users.User;

public class Enquiry extends ArrayList<Message>{
    private final User applicant;
    private final int  ticketId;
    public static int totalTickets = 1;
    private final Project project;
    private final LocalDateTime dateCreated;

    public Enquiry(User client, String enquiry, Project project) {
        this.applicant = client;
        this.project = project;
        ticketId = totalTickets;
        this.add(new Message(client, enquiry));
        totalTickets ++;
        dateCreated = LocalDateTime.now();
    }

    /*
     * Constructor for creating an enquiry with a specific ticket ID and date created.
     * Used by caching.DataInitializer to load existing enquiries from CSV files.
     * @param ticketId The ticket ID of the enquiry.
     * @param applicant The user who created the enquiry.
     * @param project The project associated with the enquiry.
     * @param dateCreated The date and time when the enquiry was created.
     */
    public Enquiry(int ticketId, User applicant, Project project, LocalDateTime dateCreated) {
        this.applicant = applicant;
        this.project = project;
        this.ticketId = ticketId;
        this.dateCreated = dateCreated;
    }
    // private boolean ticketOpen = true;
    // closeTicket functionality maybe for later 
    // public boolean closeTicket(Officer officer){}

    

    // alternative shorthand syntax 
    public void add(User client, String enquiry){
        this.add(new Message(client, enquiry));
    }
    public User getUser(){
        return applicant;
    }
    public Project getProject(){
        return project;
    }
    public int getId(){
        return ticketId;
    }
    public LocalDateTime getDateCreated(){
        return dateCreated;
    }

    public ArrayList<Message> getReplies() {
        ArrayList<Message> replies = new ArrayList<>();
        for (Message m : this) {
            if (!(m.getUser().equals(applicant))) {
                replies.add(m);
            }
        }
        return replies;
    }
    
    public boolean editMessage(int index, String newText) {
        if (index < 0 || index >= this.size()) return false;
        if (isStaffReplyPresent()) return false;

        Message m = this.get(index);
        if (!m.getUser().equals(applicant)) return false;

        m.setText(newText);
        return true;
    }

    public boolean isStaffReplyPresent() {
        return !getReplies().isEmpty();
    }
    
    @Override
    public String toString(){
        return Integer.toString(ticketId);
    }
}
