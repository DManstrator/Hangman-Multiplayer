package tk.dmanstrator.hangman.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.json.JSONObject;

/**
 * Class represents a Server Configuration for the Hangman Game.
 * @author DManstrator
 *
 */
public class ServerConfiguration {
    
    /**
     * Name of Configuration File.
     */
    private final static String CONFIGNAME = "serverconfig.json";
    
    /**
     * Port of the Server it should listen for Clients.
     */
    private final int port;
    
    /**
     * Marks the Instance as valid or invalid.
     */
    private final boolean valid;
    
    /**
     * Default Constructor.
     * Checks if the configuration file is available and creates a valid {@link ServerConfiguration} in that case. Else, an invalid {@link ServerConfiguration} will be created.
     * If a {@link ServerConfiguration} is valid can be checked with the {@link ServerConfiguration#isValid()} method.
     */
    public ServerConfiguration() {
        String fileContent = "";
        try {
            fileContent = Files.readAllLines(Paths.get(CONFIGNAME)).stream().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
        }
        if (!fileContent.isEmpty())  {
            JSONObject jsonContent = new JSONObject(fileContent);
            port = jsonContent.getInt("port");
            valid = true;
        }  else  {
            valid = false;
            port = -1;
        }
    }
    
    
    /**
     * Custom Constructor. Creates a valid {@link ServerConfiguration} with the given data and will write it into the configuration file.
     * @param port Port of the Server it should connect to
     */
    public ServerConfiguration(int port)  {
        this.port = port;
        valid = true;
        
        JSONObject config = new JSONObject();  
        config.put("port", port);
        
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
     * Gets the Port the Server should listen to.
     * @return the Port the Server should listen to
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
