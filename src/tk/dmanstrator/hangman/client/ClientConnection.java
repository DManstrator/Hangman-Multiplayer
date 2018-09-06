package tk.dmanstrator.hangman.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Interface to the Client.
 * Each Client-Connection communicates with one Client process.
 * @author DManstrator
 *
 */
public class ClientConnection extends Thread {
    
    /**
     * Socket of the Client.
     */
    private Socket socket;
    
    /**
     * ID of the Client.
     */
    private final long clientId;

    /**
     * Constructor for a Client-Connection.
     * @param client Client
     */
    public ClientConnection(Socket client, long id)  {
        this.socket = client;
        this.clientId = id;
    }

    /**
     * Returns the id of a player depending on the Socket.
     * @return ID of player
     */
    public long getClientId()  {
        return clientId;
    }
    
    /**
     * Getter for the Socket.
     * @return the socket of a ClientConnection
     */
    public Socket getSocket()  {
        return socket;
    }
    
    /**
     * Execution of a Client Connection.
     */
    @Override
    public void run() {
        try {
            while (true) {
                receive();
            }
        } 
        catch (IOException ex) {
            try {
                socket.close();
            }
            catch (IOException io) {
                io.printStackTrace();
            }
        }
    }
    
    /**
     * Receives a message from the Client.
     * @throws IOException in case something breaks.
     */
    private void receive() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String answer = reader.readLine(); // gets the response of the client
        // TODO do something with it
    }

    /**
     * Writes a message to the Client.
     * @param message Message to write.
     */
    public void send(String message) {
        try {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.println(message);
            writer.flush();
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }
    }
}
