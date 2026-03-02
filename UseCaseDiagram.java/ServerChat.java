import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class ServerChat extends JFrame {

    private JTextArea chatArea;
    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ServerChat() {
        setTitle("Server Chat");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        setVisible(true);

        startServer();
    }

   private void startServer() {
    new Thread(() -> {
        try {
            serverSocket = new ServerSocket(1234);
            chatArea.append("Server đang chờ kết nối...\n");

            socket = serverSocket.accept();
            chatArea.append("Client đã kết nối!\n");

            // KHÔNG khai báo lại kiểu dữ liệu
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(
                    socket.getOutputStream(), true);

            String message;

            while ((message = in.readLine()) != null) {
                chatArea.append("Client: " + message + "\n");
            }

        } catch (Exception e) {
            chatArea.append("Lỗi server!\n");
            e.printStackTrace();
        }
    }).start();
}

    public static void main(String[] args) {
        new ServerChat();
    }
}