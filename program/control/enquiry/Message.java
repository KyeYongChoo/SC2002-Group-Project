package program.control.enquiry;

import java.time.LocalDateTime;

import program.entity.users.User;

public class Message {
    private final User client;
    private String text;
    private final LocalDateTime timeStamp;

    /*
     * Constructor for Message without timestamp
     * Used when creating a new message
     * @param client The user who sent the message
     * @param text The content of the message
     */
    public Message(User client, String text){
        this.client = client;
        this.text = text;
        timeStamp = LocalDateTime.now();
    }

    /*
     * Constructor for Message with timestamp
     * Used by DataInitializer to load messages from CSV
     * @param client The user who sent the message
     * @param text The content of the message
     * @param timeStamp The time when the message was sent
     */
    public Message(User client, String text, LocalDateTime timeStamp){
        this.client = client;
        this.text = text;
        this.timeStamp = timeStamp;
    }

    /*
     * Get the user who sent the message
     * @return The user who sent the message
     */
    public User getUser(){
        return client;
    }

    /*
     * Get the content of the message
     * @return The content of the message
     */
    public String getText(){
        return text;
    }

    /*
     * Get the time when the message was sent
     * @return The time when the message was sent
     */
    public LocalDateTime getTimeStamp(){
        return timeStamp;
    }

    /*
     * Edit the content of the message
     * @param newText The new content of the message
     */
    public void setText(String newText) {
        this.text = newText;
    }
}
