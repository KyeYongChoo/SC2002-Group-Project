package program.control.enquiry;

import java.time.LocalDateTime;
import java.util.ArrayList;

import program.entity.project.Project;
import program.entity.users.User;

/**
 * <p>
 * The {@code Enquiry} class represents an enquiry made by a {@link User} regarding a
 * specific {@link Project}. Each enquiry is identified by a unique ticket ID and contains
 * a series of messages exchanged between the applicant and potentially other users (e.g., staff).
 * </p>
 *
 * <p>
 * The class provides methods for adding new messages, retrieving details about the enquiry,
 * and managing the status of the enquiry, such as editing messages and checking for staff replies.
 * </p>
 *
 * @see program.entity.users.User
 * @see program.entity.project.Project
 * @see program.control.enquiry.Message
 */
public class Enquiry extends ArrayList<Message> {

    private final User applicant;
    private final int ticketId;
    public static int totalTickets = 1;
    private final Project project;
    private final LocalDateTime dateCreated;

    /**
     * <p>
     * Constructor for creating a new enquiry. This constructor assigns a unique ticket ID,
     * initializes the enquiry with the provided user and project, and records the current time
     * as the creation date.
     * </p>
     *
     * @param client the {@link User} creating the enquiry
     * @param enquiry the initial message of the enquiry
     * @param project the {@link Project} the enquiry relates to
     */
    public Enquiry(User client, String enquiry, Project project) {
        this.applicant = client;
        this.project = project;
        ticketId = totalTickets;
        this.add(new Message(client, enquiry));
        totalTickets++;
        dateCreated = LocalDateTime.now();
    }

    /**
     * <p>
     * Constructor for creating an enquiry with a specific ticket ID and date created.
     * This constructor is typically used when loading existing enquiries from external data sources,
     * such as CSV files.
     * </p>
     *
     * @param ticketId the unique ticket ID for the enquiry
     * @param applicant the {@link User} who created the enquiry
     * @param project the {@link Project} the enquiry is related to
     * @param dateCreated the date and time when the enquiry was created
     */
    public Enquiry(int ticketId, User applicant, Project project, LocalDateTime dateCreated) {
        this.applicant = applicant;
        this.project = project;
        this.ticketId = ticketId;
        this.dateCreated = dateCreated;
    }

    /**
     * <p>
     * Adds a new message to the enquiry. This method allows the addition of messages after
     * the enquiry has been created.
     * </p>
     *
     * @param client the {@link User} sending the message
     * @param enquiry the content of the message being added
     */
    public void add(User client, String enquiry) {
        this.add(new Message(client, enquiry));
    }

    /**
     * <p>
     * Retrieves the user who created the enquiry.
     * </p>
     *
     * @return the {@link User} who created the enquiry
     */
    public User getUser() {
        return applicant;
    }

    /**
     * <p>
     * Retrieves the project associated with the enquiry.
     * </p>
     *
     * @return the {@link Project} related to the enquiry
     */
    public Project getProject() {
        return project;
    }

    /**
     * <p>
     * Retrieves the unique ticket ID assigned to the enquiry.
     * </p>
     *
     * @return the ticket ID of the enquiry
     */
    public int getId() {
        return ticketId;
    }

    /**
     * <p>
     * Retrieves the date and time when the enquiry was created.
     * </p>
     *
     * @return the date and time of creation
     */
    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    /**
     * <p>
     * Retrieves the replies to the enquiry. This excludes the original enquiry message and
     * only includes responses from others (e.g., staff).
     * </p>
     *
     * @return a list of {@link Message} objects that represent replies to the enquiry
     */
    public ArrayList<Message> getReplies() {
        ArrayList<Message> replies = new ArrayList<>();
        for (Message m : this) {
            if (!(m.getUser().equals(applicant))) {
                replies.add(m);
            }
        }
        return replies;
    }

    /**
     * <p>
     * Edits an existing message in the enquiry. Only the applicant can edit their original message,
     * and the edit will not occur if there is already a staff reply.
     * </p>
     *
     * @param index the index of the message to edit
     * @param newText the new content to set for the message
     * @return true if the message was successfully edited, false otherwise
     */
    public boolean editMessage(int index, String newText) {
        if (index < 0 || index >= this.size()) return false;
        if (isStaffReplyPresent()) return false;

        Message m = this.get(index);
        if (!m.getUser().equals(applicant)) return false;

        m.setText(newText);
        return true;
    }

    /**
     * <p>
     * Checks whether a staff reply has been made to the enquiry. If there are any replies that
     * were not made by the applicant, it indicates that a staff reply is present.
     * </p>
     *
     * @return true if a staff reply is present, false otherwise
     */
    public boolean isStaffReplyPresent() {
        return !getReplies().isEmpty();
    }

    /**
     * <p>
     * Returns a string representation of the enquiry, specifically the ticket ID.
     * </p>
     *
     * @return the ticket ID of the enquiry as a string
     */
    @Override
    public String toString() {
        return Integer.toString(ticketId);
    }
}
