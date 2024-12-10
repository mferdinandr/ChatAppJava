package chattingapp;

import java.time.Instant;

public class Message {
    private String content;
    private String sender;
    private Instant timestamp;
    private int messageId;

    public Message(String content, String sender, Instant timestamp, int messageId) {
        this.content = content;
        this.sender = sender;
        this.timestamp = timestamp;
        this.messageId = messageId;
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

    public int getMessageId() {
        return messageId;
    }
}