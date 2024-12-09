package chattingapp;

import java.time.Instant;

public class Message {
    private String content;
    private String sender;
    private Instant timestamp;
    private int id;

    public Message(String content, String sender, Instant timestamp, int id) {
        this.content = content;
        this.sender = sender;
        this.timestamp = timestamp;
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public int getId() {
        return id;
    }
}
