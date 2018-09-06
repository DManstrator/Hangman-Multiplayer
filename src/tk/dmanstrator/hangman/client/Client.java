package tk.dmanstrator.hangman.client;

/**
 * Combines a Client Connection with a Protocol and the active time.
 * @author DManstrator
 *
 */
public class Client {
    
    /**
     * Client Connection.
     */
    private final ClientConnection clientConnection;
    
    /**
     * Time of Client in Milliseconds.
     */
    private long time = 0;
    
    /**
     * Protocol of the Client.
     */
    private String protocol;
    
    /**
     * Default Constructor.
     * @param clientConnection Client Connection to store.
     */
    public Client(ClientConnection clientConnection)  {
        this.clientConnection = clientConnection;
    }
    
    /**
     * Getter for the Client Connection.
     * @return the Client Connection
     */
    public ClientConnection getClientConnection() {
        return clientConnection;
    }
    
    /**
     * Getter for the time the Client needed.
     * @return needed time of the Client
     */
    public long getTime() {
        return time;
    }
    
    /**
     * Appends some Milliseconds to the existing time
     * @param millis time in Milliseconds to append
     */
    public long appendTime(long millis) {
        return this.time += millis;
    }
    
    /**
     * Getter for the Protocol.
     * @return the protocol of the Client
     */
    public String getProtocol() {
        return protocol;
    }
    
    /**
     * Appends a message to the Players Protocol.
     * @param msg Message to append
     * @return the new Protocol of the Client
     */
    public String appendToProtocol(String msg) {
        return this.protocol += protocol;
    }
    
    /**
     * Implementation of own toString-Method.
     */
    @Override
    public String toString() {
        return String.format("Client with CC '%s'", clientConnection);
    }


}
