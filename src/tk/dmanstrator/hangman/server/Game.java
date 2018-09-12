package tk.dmanstrator.hangman.server;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import tk.dmanstrator.hangman.client.Client;
import tk.dmanstrator.hangman.client.ClientConnection;

/**
 * Class represents a Hangman Game.
 * @author DManstrator
 *
 */
public class Game extends Thread {

    /**
     * List of Client Connections.
     */
    private final List<Client> clients;

    /**
     * Current Turn.
     */
    private int turn = 0;
    
    /**
     * Time in seconds to wait after finally starts.
     */
    private static final int WAIT = 5;

    /**
     * Constructor for the Game. 
     * @param connections The two Client Connections
     */
    Game(List<ClientConnection> connections)  {
        clients = connections.stream().map(Client::new).collect(Collectors.toList());

        System.out.println("Game has started with the following Clients:");
        System.out.println(clients);
    }
    
    /**
     * Getter for the Turn.
     * @return turn of the Game.
     */
    public int getTurn() {
        return turn;
    }
    
    /**
     * Increments the amount of turns by one.
     */
    public void incrementTurn() {
        this.turn++;
    }
    
    /**
     * Prints a message to a Client with the connection.
     * @param connection Connection of Client
     * @param message Message to send
     * @throws IOException in case something breaks
     */
    public void print(ClientConnection connection, String message)  {
        connection.send(message);
    }

    /**
     * Updates the clients of this game.
     */
    public void broadcast() {
        synchronized (clients) {
            for (int c = 0; c < clients.size(); c++) {
                long beginn = System.currentTimeMillis();  // to see how long it took
                clients.get(c).getClientConnection();  // Update Clients
            }     
        }
    }


    /**
     * Initializes the Game. Reads a Question from the List and sends it to the Client.
     * After receiving the answers, it increments the score if allowed and appends the time each.
     */
    @Override
    public void run() {
        Client firstClient = clients.get(0);
        Client secondClient = clients.get(1);
        // initializing
        String msgToPlayer2 = ", the game starts in " + WAIT + " seconds!";
        String msgToPlayer1 = "Player 2 has joined" + msgToPlayer2;
        msgToPlayer2 = "Welcome to Hangman" + msgToPlayer2;
        
        firstClient.getClientConnection().send(msgToPlayer1);
        secondClient.getClientConnection().send(msgToPlayer2);
        
        try {
            TimeUnit.SECONDS.sleep(WAIT);
        } 
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // some rounds
        // TODO
        
        // ending

        //firstClient.getClientConnection().send(firstClient.getProtocol());
        //secondClient.getClientConnection().send(secondClient.getProtocol());

        String fin = "Game Over!";
        // determine if was solved or not
    }

    /**
     * Appends time.
     * @param id ID of player
     * @param time time to append
     */
    public void appendTime(int id, long time) {
        clients.get(id).appendTime(time);
    }
}
