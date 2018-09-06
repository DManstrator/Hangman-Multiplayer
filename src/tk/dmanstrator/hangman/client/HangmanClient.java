package tk.dmanstrator.hangman.client;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JFrame;

/**
 * One of the two Main-Programs of the Hangman Game, starts a new Client.
 * Class represents the Client for the Hangman. With initializing, it builds a Connection to the Server.
 * @author DManstrator
 *
 */
public class HangmanClient extends JFrame implements Runnable {
    
    /**
     * Serial ID.
     */
    private static final long serialVersionUID = 6552130141737583784L;

    /**
     * Port of the Client. Equal to the Port of the Server.
     */
    private static final int PORT = 1337;
    
    /**
     * Name of the player.
     */
    private final String name;
    
    /**
     * Socket of Player.
     */
    private final Socket socket;
    
    /**
     * Default-Constructor of Client.
     */
    public HangmanClient() {
        String name = "";
        Socket socket = null;

        try {
            socket = new Socket(InetAddress.getLoopbackAddress(), PORT);  // TODO let this value be set, help to get it, maybe even the port number
            String defaultName = "John Doe";
            name = initName();
            name = name.trim();
            if (name.isEmpty())  {
                name = defaultName;
            }
        }
        catch (IOException ex) {
            // TODO
            ex.printStackTrace();
        }   
        finally  {
            //initGui();
            new Thread(this).start();
        }
        this.socket = socket;
        this.name = name;
        // TODO maybe send the name to the Server
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
    Socket getSocketOfClient()  {
        return socket;
    }
    
    /**
     * Initializes the Player-Name.
     * @return name of the player
     */
    public String initName()  {
        return "John Doe";  // TODO to be implemented
    }
    
    /**
     * Main Method of Hangman Client.
     * @param args Program Arguments
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                HangmanClient client = new HangmanClient();
                //client.setVisible(true);
            }
        });
        // TODO start / display GUI
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
