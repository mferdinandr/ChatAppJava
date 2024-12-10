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
    private JTextArea messageArea;
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
    setTitle("Chat Client - " + username);
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

    // Create the message area
    messageArea = new JTextArea();
    messageArea.setEditable(false);
    messageArea.setLineWrap(true);
    messageArea.setWrapStyleWord(true);
    JScrollPane scrollPane = new JScrollPane(messageArea);
    add(scrollPane, BorderLayout.CENTER);

    // Create the input field
    textField = new JTextField();
    textField.setFont(new Font("Arial", Font.PLAIN, 16));
    add(textField, BorderLayout.SOUTH);

    // Add action listener for sending messages
    textField.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            sendMessage();
        }
    });

    setVisible(true); // Make the client visible
}

    private void sendMessage() {
        String message = textField.getText();
        if (!message.isEmpty()) {
            String formattedMessage = username + ": " + message;
            out.println(formattedMessage); // Send the message to the server
            messageArea.append(formattedMessage + "\n"); // Display the message in the chat area
            textField.setText(""); // Clear the input field
        }
    }

    private class IncomingReader implements Runnable {
        public void run() {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    // Check if the message is from the current user
                    if (!message.startsWith(username + ":")) {
                        messageArea.append(message + "\n"); } // Display messages from the server that are not from the current user
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
