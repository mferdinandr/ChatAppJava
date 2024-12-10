package chattingapp;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ChatClient extends JFrame {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private JPanel messagePanel;
    private JTextField textField;
    private String username;
    private List<JPanel> messageContainers = new ArrayList<>();
    
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

 private JPanel createMessageContainer(String sender, String message, boolean isSender, String time) {
        // Container for the message
        JPanel messageContainer = new JPanel();
        messageContainer.setLayout(new BorderLayout());
        messageContainer.setAlignmentX(isSender ? Component.RIGHT_ALIGNMENT : Component.LEFT_ALIGNMENT);

        // Message details panel
        JPanel messageDetailsPanel = new JPanel();
        messageDetailsPanel.setLayout(new BoxLayout(messageDetailsPanel, BoxLayout.Y_AXIS));

        // Add username label
        JLabel usernameLabel = new JLabel(sender);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        usernameLabel.setForeground(Color.GRAY);
        messageDetailsPanel.add(usernameLabel);

        // Add message bubble
        JLabel messageLabel = new JLabel("<html>" + message + "</html>");
        messageLabel.setOpaque(true);
        messageLabel.setBackground(isSender ? new Color(0, 204, 102) : Color.WHITE); // Green for sender
        messageLabel.setForeground(Color.BLACK);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        messageDetailsPanel.add(messageLabel);

        // Add timestamp label (HH:mm format)
        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        timeLabel.setForeground(Color.GRAY);
        messageDetailsPanel.add(timeLabel);

        messageContainer.add(messageDetailsPanel, BorderLayout.CENTER);

        // Add delete button for sender's messages
        if (isSender) {
            JButton deleteButton = new JButton("Delete");
            deleteButton.setFont(new Font("Arial", Font.PLAIN, 10));
            deleteButton.setBackground(Color.RED);
            deleteButton.setForeground(Color.WHITE);
            deleteButton.addActionListener(e -> {
                // Send delete request to server
                out.println("DELETE_MESSAGE:" + sender + ":" + message + ":" + time);
                messageContainer.removeAll();
                JLabel deletedLabel = new JLabel("Message deleted");
                deletedLabel.setForeground(Color.GRAY);
                deletedLabel.setFont(new Font("Arial", Font.ITALIC, 14));
                messageContainer.add(deletedLabel);
                messageContainer.revalidate();
                messageContainer.repaint();
            });
            messageContainer.add(deleteButton, BorderLayout.EAST);
        }

        // Add padding between messages
        messageContainer.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        return messageContainer;
    }

    private void addMessage(String sender, String message, boolean isSender, String time) {
        JPanel messageContainer = createMessageContainer(sender, message, isSender, time);
        
        // Add the container to the main message panel
        messagePanel.add(messageContainer);
        messageContainers.add(messageContainer);
        messagePanel.revalidate();
        messagePanel.repaint();
    }

    
private String getCurrentTime() {
    return java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
}


private void sendMessage() {
    String message = textField.getText();
    if (!message.isEmpty()) {
        String time = getCurrentTime(); // Mendapatkan waktu saat ini
        String formattedMessage = username + ": " + message + " [" + time + "]"; // Tambahkan waktu ke pesan
        out.println(formattedMessage); // Kirim pesan ke server
        addMessage(username, message, true, time); // Display the message on the right with time
        textField.setText(""); // Clear the input field
    }
}

 private class IncomingReader implements Runnable {
        public void run() {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    // Check if it's a delete message
                    if (message.startsWith("DELETE_MESSAGE:")) {
                        handleDeleteMessage(message);
                    } else {
                        // Regular message handling
                        int colonIndex = message.indexOf(":");
                        if (colonIndex != -1) {
                            String sender = message.substring(0, colonIndex);
                            String messageContent = message.substring(colonIndex + 1).trim();

                            // Extract the time from the message
                            int timeIndex = messageContent.lastIndexOf(" [");
                            String time = messageContent.substring(timeIndex + 2, messageContent.length() - 1);

                            // Skip displaying the message if it's from the current user
                            if (!sender.equals(username)) {
                                addMessage(sender, messageContent.substring(0, timeIndex), false, time);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
          private void handleDeleteMessage(String deleteMessage) {
            // Format: DELETE_MESSAGE:sender:message:time
            String[] parts = deleteMessage.split(":", 4);
            if (parts.length == 4) {
                String sender = parts[1];
                String originalMessage = parts[2];
                String time = parts[3];

                SwingUtilities.invokeLater(() -> {
                    for (JPanel container : messageContainers) {
                        Component[] components = container.getComponents();
                        for (Component comp : components) {
                            if (comp instanceof JPanel) {
                                JPanel detailsPanel = (JPanel) comp;
                                for (Component innerComp : detailsPanel.getComponents()) {
                                    if (innerComp instanceof JLabel) {
                                        JLabel label = (JLabel) innerComp;
                                        if (label.getText().contains(originalMessage)) {
                                            container.removeAll();
                                            JLabel deletedLabel = new JLabel("Message deleted");
                                            deletedLabel.setForeground(Color.GRAY);
                                            deletedLabel.setFont(new Font("Arial", Font.ITALIC, 14));
                                            container.add(deletedLabel);
                                            container.revalidate();
                                            container.repaint();
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }
    }


    public static void main(String[] args) {
        new ChatClient("localhost"); // Replace with your server address
    }
}