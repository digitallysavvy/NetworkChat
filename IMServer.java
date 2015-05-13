
import java.io.IOException;
import java.net.Socket;
import java.io.PrintStream;
import java.util.ArrayList;

public class IMServer {

    static private ConnectionListener connectionListener;
    static private ArrayList<ServerConnector> clientsList = new ArrayList<ServerConnector>();
    static final String NEWLINE = System.getProperty("line.separator");
    static final String TAB = "  ";

    public static void main(String[] args) throws IOException {

        int port = Integer.parseInt(args[0]);
        //Start Listening for connections to the IM server    
        connectionListener = new ConnectionListener(port);

    }

    static public void addClient(Socket socket) {

        //Create a new client connection
        ServerConnector client = new ServerConnector(socket);
        client.start();
        clientsList.add(client);

    }

    static public void removeClient(ServerConnector connection) {
        synchronized (connection) {
            //Remove client from list
            clientsList.remove(connection);
        }
    }

    static public void msgAll(ServerConnector connection, String speaker, String msg) {

        synchronized (connection) {

            //send msg to all clients connected to IMServer
            for (ServerConnector client : clientsList) {
                client.outputStream.println("<" + speaker + "> " + msg);
            }

        }
    }

    static public void clientEntersChat(ServerConnector connection, String newClient) {

        synchronized (connection) {
            for (ServerConnector client : clientsList) {  
                    client.outputStream.println(TAB + TAB + "{ " + newClient + " has entered the group chat. }");
                if(clientsList.size() == 1){
                        connection.outputStream.println(TAB + " - Please wait for other people to join this group chat");
                }
            }
            if(clientsList.size() > 1){
                connection.outputStream.println("The people in this chat are: ");
            }
            for (ServerConnector client : clientsList) { 
                if(client != connection){
                    connection.outputStream.println(TAB + " - " + client.getClientName());  
                }
            }
        }

    }

    static public void clientExitsChat(ServerConnector connection, String clientLeaving) {

        synchronized (connection) {

            for (ServerConnector client : clientsList) {
                //Notify the group that a user has left
                client.outputStream.println(TAB + TAB + "{ " + clientLeaving + " has left the group chat. }");
            }

        }

    }

}
