package program.control.enquiry;

import java.time.LocalDateTime;

import program.entity.users.User;

/**
 * <p>
 * The {@code Message} class represents a message sent by a user in the context of an enquiry.
 * Each message contains the content of the message, the user who sent it, and the timestamp
 * indicating when the message was sent.
 * </p>
 *
 * <p>
 * This class is used to capture the communication between users (applicants) and project managers
 * or officers regarding project-related enquiries. It provides methods for accessing and modifying
 * the message content and metadata such as the sender and the timestamp.
 * </p>
 *
 * @see program.entity.users.User
 */
public class Message {

    private final User client;
    private String text;
    private final LocalDateTime timeStamp;

    /**
     * <p>
     * Constructs a new {@code Message} with the specified sender and message content.
     * The timestamp is automatically set to the current time when the message is created.
     * </p>
     *
     * @param client the {@link User} who sent the message
     * @param text the content of the message
     */
    public Message(User client, String text){
        this.client = client;
        this.text = text;
        timeStamp = LocalDateTime.now();
    }

    /**
     * <p>
     * Constructs a new {@code Message} with the specified sender, message content, and timestamp.
     * This constructor is used when loading messages from a data source, such as CSV files.
     * </p>
     *
     * @param client the {@link User} who sent the message
     * @param text the content of the message
     * @param timeStamp the timestamp when the message was sent
     */
    public Message(User client, String text, LocalDateTime timeStamp){
        this.client = client;
        this.text = text;
        this.timeStamp = timeStamp;
    }

    /**
     * <p>
     * Gets the user who sent the message.
     * </p>
     *
     * @return the {@link User} who sent the message
     */
    public User getUser(){
        return client;
    }

    /**
     * <p>
     * Gets the content of the message.
     * </p>
     *
     * @return the content of the message
     */
    public String getText(){
        return text;
    }

    /**
     * <p>
     * Gets the timestamp indicating when the message was sent.
     * </p>
     *
     * @return the {@link LocalDateTime} timestamp of when the message was sent
     */
    public LocalDateTime getTimeStamp(){
        return timeStamp;
    }

    /**
     * <p>
     * Edits the content of the message.
     * </p>
     *
     * @param newText the new content of the message
     */
    public void setText(String newText) {
        this.text = newText;
    }
}
