package chattingapp;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.time.Instant;

public class ChatServer {
    private ServerUI serverUI;
    private DataOutputStream dout;
    private MessageManager messageManager;
    private int messageID = 0;

    public ChatServer() {
        serverUI = new ServerUI(this);
        serverUI.setVisible(true);
        messageManager = new MessageManager(); // Initialize MessageManager
        startServer();
    }

    private void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(6001);
            while (true) {
                Socket socket = serverSocket.accept();
                DataInputStream din = new DataInputStream(socket.getInputStream());
                dout = new DataOutputStream(socket.getOutputStream());

                new Thread(() -> handleClientMessages(din)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClientMessages(DataInputStream din) {
        try {
            while (true) {
                String msg = din.readUTF();
                Message message = new Message(msg, "ClientName", Instant.now(), ++messageID);
                messageManager.addMessage(message);
                serverUI.addMessageToUI(message); // Update UI with the new message
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DataOutputStream getDataOutputStream() {
        return dout;
    }

    public static void main(String[] args) {
        new ChatServer();
    }
}
