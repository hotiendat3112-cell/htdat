import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientChat extends JFrame {

    private JPanel chatPanel;
    private JTextField messageField;
    private PrintWriter out;

    public ClientChat() {
        setTitle("💻 CLIENT CHAT");
        setSize(450, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(new Color(240, 240, 240));

        JScrollPane scrollPane = new JScrollPane(chatPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        JButton sendButton = new JButton("Gửi");

        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());

        setVisible(true);

        connectToServer();
    }

    private void connectToServer() {
        new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 1234);
                addMessage("Đã kết nối tới server!", false);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                String message;
                while ((message = in.readLine()) != null) {
                    addMessage(message, false);
                }

            } catch (Exception e) {
                addMessage("Không kết nối được!", false);
            }
        }).start();
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            addMessage(message, true);
            out.println(message);
            messageField.setText("");
        }
    }

    private void addMessage(String text, boolean isSender) {
        JPanel messagePanel = new JPanel(new FlowLayout(
                isSender ? FlowLayout.RIGHT : FlowLayout.LEFT));

        JLabel messageLabel = new JLabel("<html><p style='width:150px'>" + text + "</p></html>");
        messageLabel.setOpaque(true);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(8,12,8,12));

        if (isSender) {
            messageLabel.setBackground(new Color(33,150,243));
            messageLabel.setForeground(Color.WHITE);
        } else {
            messageLabel.setBackground(Color.WHITE);
            messageLabel.setForeground(Color.BLACK);
        }

        messagePanel.setBackground(new Color(240,240,240));
        messagePanel.add(messageLabel);

        chatPanel.add(messagePanel);
        chatPanel.revalidate();
    }

    public static void main(String[] args) {
        new ClientChat();
    }
}