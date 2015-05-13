
import java.io.IOException;
import java.net.ServerSocket;

public class ConnectionListener {
    
    boolean listening;
    IMServer server;

    public ConnectionListener(int port){
        listening = false;
        try {
            startListening(port);
        } catch (IOException e) {}
    }
    
    public void startListening(int portNumber) throws IOException {
        
        listening = true;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {

            System.out.println("Waiting for connections...");

            while (listening) {
                IMServer.addClient(serverSocket.accept());
                System.out.println("Connected on port " + serverSocket);
            }

        } catch (IOException e) {
            System.err.println("Error, unable to listen on port " + portNumber);
            System.exit(-1);
        }
    }
}
