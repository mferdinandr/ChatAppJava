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

    public void deleteMessage(int id) {
        messages.removeIf(message -> message.getId() == id);
    }

    public List<Message> getMessages() {
        return messages;
    }

    public Message getMessageById(int id) {
        return messages.stream()
                .filter(message -> message.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
