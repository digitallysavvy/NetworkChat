
import java.io.IOException;
import java.net.ServerSocket;

public class ServerListener {

    public ServerListener(int port) throws IOException {

        boolean listening = true;

        
        try {
            ServerSocket socket = new ServerSocket(port); 
            while (listening) {
                System.out.println("Waiting for clients to connect...");
                IMServer.addClient(socket.accept());
                System.out.println("Connected");
            }
          
        } catch (IOException e) {
            System.err.println("Unable not listen on given port: " + port);
            System.exit(-1);
        }
    }
}