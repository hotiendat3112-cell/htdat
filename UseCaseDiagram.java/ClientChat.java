import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class ClientChat extends JFrame {

    private JTextArea chatArea;
    private JTextField messageField;
    private PrintWriter out;

    public ClientChat() {
        setTitle("Client Chat");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        messageField = new JTextField();
        add(messageField, BorderLayout.SOUTH);

        messageField.addActionListener(e -> sendMessage());

        setVisible(true);

        connectToServer();
    }

    private void connectToServer() {
        new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 1234);
                chatArea.append("Đã kết nối tới server!\n");

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                String message;
                while ((message = in.readLine()) != null) {
                    chatArea.append("Server: " + message + "\n");
                }

            } catch (Exception e) {
                chatArea.append("Không kết nối được!\n");
            }
        }).start();
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            chatArea.append("Client: " + message + "\n");
            out.println(message);
            messageField.setText("");
        }
    }

    public static void main(String[] args) {
        new ClientChat();
    }
}