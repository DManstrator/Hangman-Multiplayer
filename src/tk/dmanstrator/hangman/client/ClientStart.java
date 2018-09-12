package tk.dmanstrator.hangman.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * One of the two Main-Programs of the Hangman Game, starts a new Client.
 * Class represents the Client for the Hangman. With initializing, it builds a Connection to the Server.
 * @author DManstrator
 *
 */
public class ClientStart implements Runnable  {

    /**
     * Name of the player.
     */
    private final String name;
    
    /**
     * Socket of Player.
     */
    private final Socket socket;
    
    /**
     * GUI of Client.
     */
    private HangmanClient hangmanClient;
    
    /**
     * Custom-Constructor of Client.
     * @param hangmanClient GUI of Client
     * @param config Configuration to apply
     */
    public ClientStart(HangmanClient hangmanClient, ClientConfiguration config) {
        String name = config.getName();
        String ipAddr = config.getIpAddr();
        int port = config.getPort();
        Socket socket = null;
        System.out.println(String.format("%s wants to connect to %s:%d", name, ipAddr, port));
        
        boolean worked = false;  // lazy workaround
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(ipAddr, port), 2500);
            worked = true;
        } catch (SocketTimeoutException ex)  {
            hangmanClient.cancelGui();
            
        } catch (IOException ex) {
            hangmanClient.cancelGui();
        }

        this.socket = socket;
        this.name = name;
        this.hangmanClient = hangmanClient;
        if (worked)  {
            new Thread(this).start();
            hangmanClient.acceptGui();
        }
        // TODO send the name to the Server
    }
    
    /**
     * Getter for the name of the Client.
     * @return Name of Client.
     */
    public String getPlayerName()  {
        return name;
    }

    /**
     * Returns this client.
     * @return this client
     */
    public Socket getSocketOfClient()  {
        return socket;
    }
    
    /**
     * Returns the GUI of the Client.
     * @return the GUI of the Client
     */
    public HangmanClient getGui()  {
        return hangmanClient;
    }
    
    /**
     * run-Method.
     */
    @Override
    public void run() {
        while (true)  {
            try {
                receive();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * Receives a message from the Client Connection.
     * @throws IOException in case something breaks.
     */
    private void receive() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String answer = reader.readLine(); // gets the response of the client
        System.out.println(answer);
        // TODO do something with it
    }
    
    /**
     * Writes a message to the Client Connection.
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
