package Program;

import java.time.LocalDateTime;

public class Message {
    private final User client;
    private final String text;
    private final LocalDateTime timeStamp;

    public Message(User client, String text){
        this.client = client;
        this.text = text;
        timeStamp = LocalDateTime.now();
    }

    public User getUser(){
        return client;
    }

    public String getText(){
        return text;
    }
    public LocalDateTime getTimeStamp(){
        return timeStamp;
    }
}
