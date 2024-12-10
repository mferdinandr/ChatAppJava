package chattingapp;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private JFrame frame;
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

            new Thread(new IncomingReader()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupUI() {
        frame = new JFrame("Chat Client - " + username);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        frame.add(new JScrollPane(messageArea), BorderLayout.CENTER);

        textField = new JTextField();
        frame.add(textField, BorderLayout.SOUTH);

        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        frame.setVisible(true);
    }

    private void sendMessage() {
        String message = textField.getText();
        if (!message.isEmpty()) {
            out.println(username + ": " + message); // Send the message with the username
            textField.setText(""); // Clear the input field
        }
    }

    private class IncomingReader implements Runnable {
        public void run() {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    messageArea.append(message + "\n"); // Display messages from the server
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new ChatClient("localhost");
    }
}