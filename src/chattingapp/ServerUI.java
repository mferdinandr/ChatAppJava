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
    // Set frame properties
    setTitle("Chat Server");
    setSize(500, 400);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    

    // Create a header panel
    JPanel headerPanel = new JPanel();
    headerPanel.setBackground(new Color(0, 102, 204)); // Blue background
    headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

    // Add title label
    JLabel titleLabel = new JLabel("Chat Server Application");
    titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
    titleLabel.setForeground(Color.WHITE);
    headerPanel.add(titleLabel);

    add(headerPanel, BorderLayout.NORTH);

    // Create main content panel with GridBagLayout
    JPanel mainPanel = new JPanel();
    mainPanel.setBackground(new Color(0, 153, 255)); // Light blue
    mainPanel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10); // Add some spacing between elements
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.CENTER;

    // Add logo
    JLabel logoLabel = new JLabel();
    ImageIcon logoIcon = new ImageIcon(ClassLoader.getSystemResource("icons/logo.png"));
    // Resize the logo
    Image scaledImage = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Resize to 100x100
    logoLabel.setIcon(new ImageIcon(scaledImage));
    mainPanel.add(logoLabel, gbc);

    // Add instruction text
    gbc.gridy++; // Move to the next row
    JLabel instructionLabel = new JLabel("Klik tombol untuk memulai chat");
    instructionLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
    instructionLabel.setForeground(Color.WHITE);
    mainPanel.add(instructionLabel, gbc);

    add(mainPanel, BorderLayout.CENTER);

    // Create footer panel with a button
    JPanel footerPanel = new JPanel();
    footerPanel.setBackground(new Color(230, 230, 230)); // Light gray for button background contrast
    footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

    // Add a button to start chat
    JButton startChatButton = new JButton("Mulai Chat");
    startChatButton.setFont(new Font("Arial", Font.PLAIN, 16));
    startChatButton.setBackground(new Color(255, 69, 0)); // Bright orange
    startChatButton.setForeground(Color.WHITE);
    startChatButton.setFocusPainted(false);
    startChatButton.setBorderPainted(false);
    startChatButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

    // Add action listener to launch chat client
    startChatButton.addActionListener((ActionEvent e) -> {
        launchChatClient();
    });

    footerPanel.add(startChatButton);
    add(footerPanel, BorderLayout.SOUTH);

    // Set the frame visible
    setVisible(true);
    setLocationRelativeTo(null); // This will center the frame on the screen
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
