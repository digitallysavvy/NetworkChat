
import java.io.IOException;
import javax.swing.JFrame;

public class ChatConnector {
    
    static ClientConnector clientConnection;
    
    public ChatConnector (){
        clientConnection = null;
    }

    public static void main(String[] args) {
        
        connectToServer(args[0], Integer.parseInt(args[1]));
        
    }
    
    static public void connectToServer(String server, int port){
        
        try {
            clientConnection = new ClientConnector(server, port);
            launchChatWindow();
        } catch (IOException ex) {
            System.out.println("Cannot connect to " + server + ":" + port);
            System.exit(0);
        }
        
    }
    
    static public void launchChatWindow(){
        //Create chat window
        JFrame chatWindow = new ChatWindow(clientConnection);
        chatWindow.setTitle("Group Chat");
        chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatWindow.pack();
        chatWindow.setResizable(false);
        chatWindow.setVisible(true);
    }
}
