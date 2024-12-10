package chattingapp;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private Set<ClientHandler> clientHandlers = new HashSet<>();

    public void start() {
        System.out.println("Chat server started...");
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                
                // Read the username from the client
                username = in.readLine();
                synchronized (clientHandlers) {
                    clientHandlers.add(this);
                }

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Received: " + message);
                    broadcast(message); // Broadcast the message to all clients
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                synchronized (clientHandlers) {
                    clientHandlers.remove (this);
                }
            }
        }
    }

    public void broadcast(String message) {
        synchronized (clientHandlers) {
            for (ClientHandler client : clientHandlers) {
                client.out.println(message); // Send the message to all clients
            }
        }
    }

    public static void main(String[] args) {
        new ChatServer().start();
    }
}