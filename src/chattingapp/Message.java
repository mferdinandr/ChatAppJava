package chattingapp;

import java.time.Instant;

public class Message {
    private String content;
    private String sender;
    private Instant timestamp;
    private int messageId;
    private boolean isDeleted;

    public Message(String content, String sender, Instant timestamp, int messageId) {
        this.content = content;
        this.sender = sender;
        this.timestamp = timestamp;
        this.messageId = messageId;
        this.isDeleted = false;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}