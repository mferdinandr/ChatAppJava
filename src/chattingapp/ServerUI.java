package chattingapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServerUI extends JFrame implements ActionListener {
    private JTextField text;
    private JPanel messagePanel;
    private Map<Integer, JPanel> messages = new HashMap<>();
    private ChatServer chatServer;
    private static int messageID = 0;
    private String profilePicturePath; // Path to the profile picture

    // Constructor accepting profile picture path
    public ServerUI(ChatServer chatServer, String profilePicturePath) {
        this.chatServer = chatServer;
        this.profilePicturePath = profilePicturePath; // Store the profile picture path
        initializeUI();
    }

    private void initializeUI() {
        setLayout(null);
        setSize(450, 700);
        setLocation(800, 50);
        setUndecorated(true);
        getContentPane().setBackground(Color.WHITE);

        createHeader(); // Create the header panel
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

        // Back button
        ImageIcon backIcon = new ImageIcon(ClassLoader.getSystemResource("icons/3.png"));
        Image backImage = backIcon.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        JLabel back = new JLabel(new ImageIcon(backImage));
        back.setBounds(5, 20, 25, 25);
        header.add(back);
        back.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent ae) {
                System.exit(0); // Close the application
            }
        });

        // Profile picture from the passed path
        ImageIcon profileIcon = new ImageIcon(profilePicturePath); // Use the passed path
        Image profileImage = profileIcon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        JLabel profile = new JLabel(new ImageIcon(profileImage));
        profile.setBounds(40, 10, 50, 50);
        header.add(profile);

        // Other icons (video, phone, more options)
        addIconToHeader(header, "icons/video.png", 300, 20, 30, 30);
        addIconToHeader(header, "icons/phone.png", 360, 20, 35, 30);
        addIconToHeader(header, "icons/3icon.png", 420, 20, 10, 25);

        // Name and status labels
        JLabel name = new JLabel("Onet");
        name.setBounds(110, 15, 100, 18);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
        header.add(name);

        JLabel status = new JLabel("Active Now");
        status.setBounds(110, 35, 100, 18);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("SAN_SERIF", Font.BOLD, 14));
        header.add(status);
    }

    private void addIconToHeader(JPanel header, String iconPath, int x, int y, int width, int height) {
        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource(iconPath));
        Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
        JLabel label = new JLabel(new ImageIcon(image));
        label.setBounds(x, y, width, height);
        header.add(label);
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
