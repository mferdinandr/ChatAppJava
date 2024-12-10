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
            messageManager.addMessage(new Message(content, sender, timestamp, messageId));
            messageArea.append(sender + ": " + content + "\n");
            textField.setText(""); // Clear the input field
            chatServer.broadcast(sender + ": " + content); // Broadcast the message to all clients
        }
    }

    private void launchChatClient() {
    // Launch a new ChatClient instance
        SwingUtilities.invokeLater(() -> {
        new ChatClient("localhost").setVisible(true);
    });
}
    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        ServerUI serverUI = new ServerUI(chatServer);
        serverUI.setVisible(true);
        chatServer.start(); // Start the chat server
    }
}
