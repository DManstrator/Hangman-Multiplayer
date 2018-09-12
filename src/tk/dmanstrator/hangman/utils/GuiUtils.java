package tk.dmanstrator.hangman.utils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Utility Methods for the GUI.
 * @author DManstrator
 *
 */
public class GuiUtils {
    
    /**
     * List of forbidden names.
     */
    private static final List<String> FORBIDDEN_NAMES = Arrays.asList("Hitler");
    
    /**
     * Opens an AlertBox to show the User that his input was not correct.
     * @param errorResponse Additional Error Response giving the User more information
     */
    public void alert(String errorResponse) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Incorrect Input!");
        window.setMinWidth(250);
        
        Label label = new Label("Please re-check your input, it was wrong!");
        Label info = new Label(errorResponse);
        info.setWrapText(true);
        info.setStyle("-fx-font-weight: bold; -fx-font-size: 15px; -fx-text-align: center;");
        info.setTextAlignment(TextAlignment.CENTER);
        
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> window.close());
        
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, info, closeButton);
        layout.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(layout, 500, 150);
        window.setScene(scene);
        window.showAndWait();
    }
    

    /**
     * Verifies the User Input
     * @param name Name to check
     * @param ip IP to check
     * @param port Port to check
     * @return <code>null</code> if all the data is formally correct, else returns an Error Message
     */
    public String verify(String name, String ip, String port) {
        if (name.trim().isEmpty() || ip.trim().isEmpty() || port.trim().isEmpty())  {
            return "You have to fill in every value!";
        }
        
        // check if name is not allowed
        if (FORBIDDEN_NAMES.contains(name))  {
            return "That name is not allowed!";
        }
        
        // check if IP is formally correct
        if (!verifyIp(ip))  {
            return "IP-Address is not valid!";
        }
        
        // check if port is legitimate
        String portMsg = verifyPort(port);
        if (portMsg != null)  {  // kind of unnecessary, could return portMsg since it's the last check
            return portMsg;
        }
        
        return null;
    }
    
    /**
     * Validates an IPv4 address. Returns true if valid.
     * 
     * @param ip The IPv4 address to validate
     * @return true if the argument contains a valid IPv4 address
     */
    public boolean verifyIp(String ip) {
        Pattern pattern = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
        Matcher matcher = pattern.matcher(ip);

        if (matcher.matches()) {
            for (int c = 1; c <= matcher.groupCount(); c++) {
                String ipSegment = matcher.group(c);
                if (ipSegment == null || ipSegment.length() == 0) {
                    return false;
                }
    
                int iIpSegment = 0;
    
                try {
                    iIpSegment = Integer.parseInt(ipSegment);
                } catch (NumberFormatException e) {
                    return false;
                }
    
                if (iIpSegment > 255) {
                    return false;
                }
    
                if (ipSegment.length() > 1 && ipSegment.startsWith("0")) {
                    return false;
                }
            }
        } else {
            return false;
        }

        return true;
    }
    
    /**
     * Verifies that a given Port is valid.
     * @param port Port to check
     * @return <code>null</code> if the port is valid, else an Error Response
     */
    public String verifyPort(String port) {
        try {
            int portAsInt = Integer.parseInt(port);
            if (portAsInt > 65535)  {
                return "The Port Number is not in range!";
            } else if (portAsInt > 49151)  {
                return "The Port Number is in a reserved area (from 49152 up to 65535) and cannot be used!";
            } else if (portAsInt < 1024 || portAsInt == 1080)  {
                return "You shouldn't use this number as a port for a good reason!"
                        + System.lineSeparator() + "Use something from 1024 up to 49151 instead.";
            }
        } catch (NumberFormatException ex)  {
            return "The port has to be a number!";
        }
        return null;
    }

}
