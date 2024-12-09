package chattingapp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ServerUI extends JFrame implements ActionListener {
    private JTextField text;
    private JPanel messagePanel;
    private Map<Integer, JPanel> messages = new HashMap<>();
    private ChatServer chatServer;
    private static int messageID = 0;

    public ServerUI(ChatServer chatServer) {
        this.chatServer = chatServer;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(null);
        setSize(450, 700);
        setLocation(800, 50);
        setUndecorated(true);
        getContentPane().setBackground(Color.WHITE);

        createHeader();
        createMessagePanel();
        createInputField();

        setVisible(true);
    }

    private void createHeader() {
        JPanel header = new JPanel();
        header.setBackground(new Color(7, 94, 84));
        header.setBounds(0, 0, 450, 70);
        header.setLayout(null);
        add(header);

        // Add components to header (back button, profile, etc.)
        // Similar to the original code, but extracted into methods for clarity
        // ...
    }

    private void createMessagePanel() {
        messagePanel = new JPanel();
        messagePanel.setBounds(5, 75, 440, 570);
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(messagePanel);
        scrollPane.setBounds(5, 75, 440, 570);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane);
    }

    private void createInputField() {
        text = new JTextField();
        text.setBounds(5, 655, 310, 40);
        text.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        add(text);

        JButton sendButton = new JButton("Send");
        sendButton.setBounds(320, 655, 123, 40);
        sendButton.setBackground(new Color(7, 94, 84));
        sendButton.setForeground(Color.WHITE);
        sendButton.addActionListener(this);
        sendButton.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        add(sendButton);

        text.addActionListener(this);
        text.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    actionPerformed(new ActionEvent(text, ActionEvent.ACTION_PERFORMED, ""));
                }
            }
        });
    }

    public void action
