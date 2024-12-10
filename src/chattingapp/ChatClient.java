package chattingapp;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatClient extends JFrame {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private JPanel messagePanel;
    private JTextField textField;
    private String username;

    public ChatClient(String serverAddress) {
        // Prompt for username
        username = JOptionPane.showInputDialog("Enter your username:");
        setupUI();
        try {
            socket = new Socket(serverAddress, 12345);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Send the username to the server
            out.println(username);

            // Start the incoming reader thread
            new Thread(new IncomingReader()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

private void setupUI() {
    setTitle(username);
    setSize(400, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    // Create a header panel
    JPanel headerPanel = new JPanel();
    headerPanel.setBackground(new Color(0, 102, 204)); // Header color
    headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

    // Add profile picture
    ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icons/logo.png")); // Update with your image path
    if (icon.getIconWidth() == -1) {
        System.out.println("Image not found or could not be loaded.");
    }
    JLabel profilePicture = new JLabel(icon);
    profilePicture.setPreferredSize(new Dimension(50, 50));
    headerPanel.add(profilePicture);

    // Add title to the header
    JLabel titleLabel = new JLabel("Kelompok 1 PBO");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    titleLabel.setForeground(Color.WHITE);
    headerPanel.add(titleLabel);

    add(headerPanel, BorderLayout.NORTH);

    // Create the message panel
    messagePanel = new JPanel();
    messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
    JScrollPane scrollPane = new JScrollPane(messagePanel);
    add(scrollPane, BorderLayout.CENTER);

    // Create input and button panel
    JPanel inputPanel = new JPanel();
    inputPanel.setLayout(new BorderLayout());

    textField = new JTextField();
    textField.setFont(new Font("Arial", Font.PLAIN, 16));
    inputPanel.add(textField, BorderLayout.CENTER);

    JButton sendButton = new JButton("Send");
    sendButton.setFont(new Font("Arial", Font.BOLD, 16));
    sendButton.setBackground(new Color(0, 153, 51));
    sendButton.setForeground(Color.WHITE);
    inputPanel.add(sendButton, BorderLayout.EAST);

    // Add action listeners
    textField.addActionListener(e -> sendMessage());
    sendButton.addActionListener(e -> sendMessage());

    add(inputPanel, BorderLayout.SOUTH);

    setVisible(true);
}

    private void addMessage(String sender, String message, boolean isSender) {
        // Container for the message
        JPanel messageContainer = new JPanel();
        messageContainer.setLayout(new BoxLayout(messageContainer, BoxLayout.Y_AXIS));
        messageContainer.setAlignmentX(isSender ? Component.RIGHT_ALIGNMENT : Component.LEFT_ALIGNMENT);

        // Add username label
        JLabel usernameLabel = new JLabel(sender);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        usernameLabel.setForeground(Color.GRAY);
        messageContainer.add(usernameLabel);

        // Add message bubble
        JLabel messageLabel = new JLabel("<html>" + message + "</html>");
        messageLabel.setOpaque(true);
        messageLabel.setBackground(isSender ? new Color(0, 204, 102) : Color.WHITE); // Green for sender
        messageLabel.setForeground(Color.BLACK);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        messageContainer.add(messageLabel);

        // Add padding between messages
        messageContainer.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Add the container to the main message panel
        messagePanel.add(messageContainer);
        messagePanel.revalidate();
        messagePanel.repaint();
    }

private void sendMessage() {
    String message = textField.getText();
    if (!message.isEmpty()) {
        String formattedMessage = username + ": " + message;
        out.println(formattedMessage); // Send the message to the server
        addMessage(username, message, true); // Display the message on the right
        textField.setText(""); // Clear the input field
    }
}

   private class IncomingReader implements Runnable {
    public void run() {
        String message;
        try {
            while ((message = in.readLine()) != null) {
                // Extract username and message
                int colonIndex = message.indexOf(":");
                if (colonIndex != -1) {
                    String sender = message.substring(0, colonIndex); // Extract sender username
                    String messageContent = message.substring(colonIndex + 1).trim(); // Extract message content

                    // Skip displaying the message if it's from the current user
                    if (!sender.equals(username)) {
                        addMessage(sender, messageContent, false); // Display only messages from others
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


    public static void main(String[] args) {
        new ChatClient("localhost"); // Replace with your server address
    }
}
