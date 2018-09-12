package tk.dmanstrator.hangman.client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.json.JSONObject;

/**
 * Class represents a Client Configuration for the Hangman Game.
 * @author DManstrator
 *
 */
public class ClientConfiguration {
    
    /**
     * Name of Configuration File.
     */
    private final static String CONFIGNAME = "config.json";
    
    /**
     * (Nick)name of the Client.
     */
    private final String name;
    
    /**
     * IP Address of the Server it should connect to.
     */
    private final String ipAddr;
    
    /**
     * Port of the Server it should connect to.
     */
    private final int port;
    
    /**
     * Marks the Instance as valid or invalid.
     */
    private final boolean valid;
    
    /**
     * Default Constructor.
     * Checks if the configuration file is available and creates a valid {@link ClientConfiguration} in that case. Else, an invalid {@link ClientConfiguration} will be created.
     * If a {@link ClientConfiguration} is valid can be checked with the {@link ClientConfiguration#isValid()} method.
     */
    public ClientConfiguration() {
        String fileContent = "";
        try {
            fileContent = Files.readAllLines(Paths.get(CONFIGNAME)).stream().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
        }
        if (!fileContent.isEmpty())  {
            JSONObject jsonContent = new JSONObject(fileContent);
            name = jsonContent.getString("name");
            ipAddr = jsonContent.getString("server_ip");
            port = jsonContent.getInt("server_port");
            valid = true;
        }  else  {
            valid = false;
            name = null;
            ipAddr = null;
            port = -1;
        }
    }
    
    /**
     * Custom Constructor. Creates a valid {@link ClientConfiguration} with the given data and will write it into the configuration file.
     * @param name Name of Client
     * @param ipAddr IP Address of the Server it should connect to
     * @param port Port of the Server it should connect to
     */
    public ClientConfiguration(String name, String ipAddr, int port)  {
        this.name = name;
        this.ipAddr = ipAddr;
        this.port = port;
        valid = true;
        
        JSONObject config = new JSONObject();  
        config.put("name", name);  
        config.put("server_ip", ipAddr);
        config.put("server_port", port);
        
        File file = new File(CONFIGNAME);
        try  {
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(config.toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException ex)  {
            ex.printStackTrace();
        }
    }
    
    /**
     * Gets the Name of the Client.
     * @return the name of the Client
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the IP-Address the Client wants to connect to.
     * @return the IP-Address the Client wants to connect to
     */
    public String getIpAddr() {
        return ipAddr;
    }
    
    /**
     * Gets the Port the Client wants to connect to.
     * @return the Port the Client wants to connect to
     */
    public int getPort() {
        return port;
    }
    
    /**
     * Checks if an Instance of this Object is valid or invalid.
     * @return <code>true</code> if instance is valid, else <code>false</code>
     */
    public boolean isValid() {
        return valid;
    }
    
}
