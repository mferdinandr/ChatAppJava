package chattingapp;

import java.util.ArrayList;
import java.util.List;

public class MessageManager {
    private List<Message> messages;

    public MessageManager() {
        messages = new ArrayList<>();
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void clearMessages() {
        messages.clear();
    }
}