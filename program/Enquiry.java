package program;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Enquiry extends ArrayList<Message>{
    private final User applicant;
    private final int  ticketId;
    static int totalTickets = 1;
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
    @Override
    public String toString(){
        return Integer.toString(ticketId);
    }
}
