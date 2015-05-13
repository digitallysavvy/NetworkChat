
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Observable;

public class ClientConnector extends Observable {

    private Socket socket;
    private OutputStream outputStream;

    public ClientConnector(String chatServer, int port) throws IOException {

        //Establish a connection with the chat server
        System.out.println("Connecting to chat server...");
        socket = new Socket(chatServer, port);

        System.out.println("Connection established");
        outputStream = socket.getOutputStream();

        //Create a Thread for recieving messages from chat
        Thread listenerThread = new Thread() {
            @Override
            public void run() {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String msg;
                    while ((msg = reader.readLine()) != null) {
                        notifyObservers(msg);
                    }
                } catch (IOException ex) {
                    notifyObservers(ex);
                }
            }
        };

        //Start listening to chat messages
        listenerThread.start();

    }

    public void notifyObservers(Object arg) {
        super.setChanged();
        super.notifyObservers(arg);
    }

    public void send(String text) {

        final String NEWLINE = System.getProperty("line.separator");

        try {
            outputStream.write((text + NEWLINE).getBytes());
            outputStream.flush();
        } catch (IOException ex) {
            notifyObservers(ex);
        }
    }

    public void close() {
        try {

            socket.close();

        } catch (IOException ex) {
            notifyObservers(ex);
        }
    }

}
