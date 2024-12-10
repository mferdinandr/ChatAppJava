package chattingapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.Instant;

public class ServerUI extends JFrame {
    private JTextArea messageArea;
    private JTextField textField;
    private final ChatServer chatServer; // Instance of ChatServer
    private final MessageManager messageManager; // Instance of MessageManager

    public ServerUI(ChatServer chatServer) {
        this.chatServer = chatServer;
        this.messageManager = new MessageManager();
        setupUI();
    }

    private void setupUI() {
        setTitle("Chat Server");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the message area
        messageArea = new JTextArea();
 messageArea.setEditable(false);
        add(new JScrollPane(messageArea), BorderLayout.CENTER);

        // Create the input field
        textField = new JTextField();
        add(textField, BorderLayout.SOUTH);

        // Add action listener for sending messages
        textField.addActionListener((ActionEvent e) -> {
            sendMessage();
        });

        // Add a button to launch the ChatClient
        JButton launchClientButton = new JButton("Launch Client");
        launchClientButton.addActionListener((ActionEvent e) -> {
            launchChatClient();
        });
        add(launchClientButton, BorderLayout.EAST);
    }

    private void sendMessage() {
        String content = textField.getText();
        if (!content.isEmpty()) {
            String sender = "Server";
            Instant timestamp = Instant.now();
            int messageId = messageManager.getMessages().size() + 1;

            Message message = new Message(content, sender, timestamp, messageId);
            messageManager.addMessage(message);
            messageArea.append("You: " + content + "\n");
            textField.setText("");

            // Broadcast the message to clients
            chatServer.broadcast(content);
        }
    }

    private void launchChatClient() {
        // Launch a new ChatClient instance
        new Thread(() -> {
            ChatClient client = new ChatClient("localhost");
        }).start();
    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        new Thread(server::start).start(); // Start the server in a new thread
        SwingUtilities.invokeLater(() -> new ServerUI(server).setVisible(true)); // Start the UI
    }
}