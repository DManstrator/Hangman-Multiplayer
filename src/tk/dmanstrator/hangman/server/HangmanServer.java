package tk.dmanstrator.hangman.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import tk.dmanstrator.hangman.client.ClientConnection;

/**  
 * One of the two Main-Programs of the Hangman Game, executes the Server.
 * Waits for the Clients and connects them to the Game.
 * 
 * @author DManstrator
 */
public class HangmanServer extends Thread {
    
    /**
     * Port of the Server. Equal to the Port of the Client.
     */
    private static final int PORT = 1337;
    
    /**
     * ID of the player.
     */
    private static long playerId = 0;
    
    
    /**
     * List of Games.
     */
    private final List<Game> games = new ArrayList<>();
    
    /**
     * Initializes the Game Server and waits permanent for new Clients.
     */
    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(PORT)) {
            List<ClientConnection> clientlist = new ArrayList<>();
            System.out.println("Game Server waiting on Port " + PORT + "!");
            while (true)  {
                Socket client = server.accept();
                synchronized (clientlist) {
                    ClientConnection cc = new ClientConnection(client, playerId);
                    int playerNr = (int) (playerId % 2) + 1;
                    cc.start();

                    clientlist.add(cc);
                    System.out.println("Client " + playerNr + " (playerID: " + playerId + ") accepted!");
                    
                    playerId++;  // increase playerId;
                    if (clientlist.size() == 2) {
                        synchronized (games) {  // TODO no sync needed?
                            Game game = new Game(clientlist);
                            game.start();
                            games.add(game);
                            clientlist.clear();  // wait for two new clients
                        }
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Main-Method of the Hangman Server.
     * @param args Arguments
     */
    public static void main(String[] args) {
        HangmanServer gs = new HangmanServer();
        gs.start();
    }
}