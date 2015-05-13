
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ServerConnector extends Thread {

    private Socket client;
    private String clientName;
    public PrintStream outputStream;
    private BufferedReader inputStream;
    private boolean getName, chatting;
    static final String NEWLINE = System.getProperty("line.separator");
    static final String TAB = "\t";

    public ServerConnector(Socket socket) {
        client = socket;
    }

    public String getClientName(){
        return clientName;
    }

    public void run() {
        try {
            inputStream = new BufferedReader(new InputStreamReader(client.getInputStream()));
            outputStream = new PrintStream(client.getOutputStream());
            getName = true;

            //Get client name
            while (true) {
                clientName = inputStream.readLine().trim();

                if (clientName.contains("#exit")) {
                    outputStream.println("I'm sorry but your name cannot not contain '#exit'" + NEWLINE + "Please enter your name again.");
                } else {
                    getName = false;
                    chatting = true;
                    outputStream.println("Welcome to the group chat " + clientName + NEWLINE + " ** NOTE: use #exit to leave the chat **"+ NEWLINE);
                    break;
                }
            }

            synchronized (this) {
                IMServer.clientEntersChat(this, clientName);
            }


            //While in the chat room
            while (chatting) {
                String msg = inputStream.readLine();

                //Check if client is trying to quit
                if (msg.startsWith("#exit")) {
                    chatting = false;
                }

                IMServer.msgAll(this, clientName, msg);
            }

            //Inform other clients that someone has left the chat
            IMServer.clientExitsChat(this, clientName);
            IMServer.removeClient(this);

            inputStream.close();
            outputStream.close();
            client.close();
        } catch (IOException e) {
        }

    }
}
