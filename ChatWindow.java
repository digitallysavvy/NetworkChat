
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.util.Observable;
import java.util.Observer;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ChatWindow extends JFrame implements Observer {

    private ClientConnector clientConnection;
    private JTextArea chatTextArea;
    private JTextField clientMsg;
    private JButton sendButton;


    public ChatWindow(ClientConnector client) {
        clientConnection = client;
        clientConnection.addObserver(this);
        createChatWindow();
    }


    //Update chat text area
    public void update(Observable o, Object arg) {
        final Object finalArg = arg;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                chatTextArea.append(finalArg.toString());
                chatTextArea.append("\n");
            }
        });
    }

    //Create the chat window UI
    private void createChatWindow() {
        chatTextArea = new JTextArea(20, 50);
        chatTextArea.setEditable(false);
        chatTextArea.setLineWrap(true);
        chatTextArea.setText("Please enter your name\n");
        add(new JScrollPane(chatTextArea), BorderLayout.CENTER);

        Box box = Box.createHorizontalBox();
        add(box, BorderLayout.SOUTH);
        clientMsg = new JTextField();
        sendButton = new JButton("Send");
        box.add(clientMsg);
        box.add(sendButton);

        ActionListener sendListener = new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String str = clientMsg.getText();
                if (str != null && str.trim().length() > 0) {
                    clientMsg.selectAll();
                }
                clientConnection.send(str);
                clientMsg.selectAll();
                clientMsg.requestFocus();
                clientMsg.setText("");
            }
        };
        clientMsg.addActionListener(sendListener);
        sendButton.addActionListener(sendListener);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                clientConnection.close();
            }
        });
    }
}
