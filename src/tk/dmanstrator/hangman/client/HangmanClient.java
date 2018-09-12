package tk.dmanstrator.hangman.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * One of the two Main-Programs of the Hangman Game, starts a new Client.
 * Class represents the Client for the Hangman. With initializing, it builds the GUI for the Client.
 * @author DManstrator
 *
 */
public class HangmanClient extends Application  {
    
    /**
     * List of forbidden names.
     */
    private static final List<String> FORBIDDEN_NAMES = Arrays.asList("Hitler");
    
    /**
     * Actual Client which has a Connection to the Server.
     */
    private ClientStart client = null;
    
    /**
     * Client Configuration which contains name, IP and Port.
     */
    private ClientConfiguration config = new ClientConfiguration();
    
    /**
     * Scenes of the GUI.
     */
    private Map<String, Scene> scenes = new HashMap<>();
    
    /**
     * Gets the Client which is connected to the Server. Can be <code>null</code>.
     * @return the Client which is connected to the Server
     */
    public ClientStart getClient()  {
        return client;
    }
    
    /**
     * Gets the Client Configuration.
     * @return the client Configuration
     */
    public ClientConfiguration getClientConfiguration()  {
        return config;
    }
    
    /**
     * Gets the Scenes of the GUI.
     * @return the Scenes of the GUI
     */
    public Map<String, Scene> getScenes()  {
        return scenes;
    }
    
    /**
     * Gets the Scene by Name.
     * @param key Name of Scene
     * @return Scene if available or <code>null</code>
     */
    public Scene getScene(String key)  {
        return (scenes.containsKey(key) ? scenes.get(key) : null);
    }
    
    /**
     * Main-Method of the Hangman Client.
     * @param args Arguments
     */
    public static void main(String[] args)  {
        launch(args);
    }
    
    /**
     * Creates the Users GUI.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Main Layout
        Label mainWelcomeLabel = new Label("Welcome!");
        mainWelcomeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        
        Label mainInfoLabel = new Label("Please set your profile first before connecting to a Server!");
        mainInfoLabel.setWrapText(true);
        mainInfoLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        mainInfoLabel.setTextAlignment(TextAlignment.CENTER);
        
        Button connectButton = new Button("Connect to Server");
        Button profileButton = new Button("Set Profile");
        
        if (!config.isValid())  {
            connectButton.setDisable(true);
        }
        
        VBox mainLayout = new VBox(20);
        
        HBox mainButtons = new HBox(5);
        mainButtons.getChildren().addAll(connectButton, profileButton);
        mainButtons.setAlignment(Pos.CENTER);

        mainLayout.getChildren().addAll(mainWelcomeLabel, mainInfoLabel, mainButtons);
        mainLayout.setAlignment(Pos.CENTER);
        Scene mainScene = new Scene(mainLayout, 300, 400);
        
        // Profile Layout
        Label profileLabel = new Label("Please set the following fields!");
        profileLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        
        //Creating a GridPane container
        GridPane grid = new GridPane();
        grid.getColumnConstraints().addAll(new ColumnConstraints(65));
        
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(5);
        grid.setHgap(5);
        
        // Nickname
        final TextField nameField = new TextField();
        if (config.isValid())  {
            nameField.setText(config.getName());
        }
        nameField.setPromptText("Enter your Nickname");
        Label nameInputLabel = new Label("Name:");
        GridPane.setColumnIndex(nameInputLabel, 0);
        GridPane.setRowIndex(nameInputLabel, 0);
        GridPane.setHalignment(nameInputLabel, HPos.RIGHT);
        GridPane.setConstraints(nameField, 1, 0);
        grid.getChildren().addAll(nameInputLabel, nameField);
        
        // IP
        final TextField ipField = new TextField();
        if (config.isValid())  {
            ipField.setText(config.getIpAddr());
        }
        ipField.setPromptText("Enter the Server IP");
        Label ipInputLabel = new Label("Server IP:");
        GridPane.setColumnIndex(ipInputLabel, 0);
        GridPane.setRowIndex(ipInputLabel, 1);
        GridPane.setHalignment(ipInputLabel, HPos.RIGHT);
        GridPane.setConstraints(ipField, 1, 1);
        grid.getChildren().addAll(ipInputLabel, ipField);
        
        // Port
        final TextField portField = new TextField();
        if (config.isValid())  {
            portField.setText(String.valueOf(config.getPort()));
        }
        portField.setPromptText("Enter the Server Port");
        Label portInputLabel = new Label("Server Port:");
        GridPane.setColumnIndex(portInputLabel, 0);
        GridPane.setRowIndex(portInputLabel, 2);
        GridPane.setHalignment(portInputLabel, HPos.RIGHT);
        GridPane.setConstraints(portField, 1, 2);
        grid.getChildren().addAll(portInputLabel, portField);
        
        // Return Buttons
        Button saveButton = new Button("Save & Return");
        Button returnButton = new Button("Return");
        returnButton.setOnAction(e -> {
            primaryStage.setScene(mainScene);
            primaryStage.setTitle("Hangman-Multiplayer");
        });
        saveButton.setOnAction(e ->  {
            String name = nameField.getText();
            String ipAddr = ipField.getText();
            String port = portField.getText();
            String errorResponse = verify(name, ipAddr, port);
            if (errorResponse == null)  {
                config = new ClientConfiguration(name, ipAddr, Integer.parseInt(port));
                connectButton.setDisable(false);
                primaryStage.setScene(mainScene);
                primaryStage.setTitle("Hangman-Multiplayer");
            }  else  {
                alert(errorResponse);
            }
        });
        
        HBox profileButtons = new HBox(5);
        profileButtons.getChildren().addAll(saveButton, returnButton);
        profileButtons.setAlignment(Pos.CENTER);
        
        VBox profileLayout = new VBox(20);
        profileLayout.getChildren().addAll(profileLabel, grid, profileButtons);
        profileLayout.setAlignment(Pos.CENTER);
        Scene profileScene = new Scene(profileLayout, 300, 500);
        
        // Play Layout
        // TODO more
        StackPane gameLayout = new StackPane();
        gameLayout.setAlignment(Pos.CENTER);
        gameLayout.getChildren().add(new Label("Connecting to Server.."));
        Scene gameScene = new Scene(gameLayout, 500, 500);
        
        scenes.put("gameScene", gameScene);
        
        // Listeners
        connectButton.setOnAction(e -> {
            primaryStage.setScene(gameScene);
            client = new ClientStart(this, config);
        });
        profileButton.setOnAction(e -> {
            primaryStage.setScene(profileScene);
            primaryStage.setTitle("User Profile");
        });
        
        // Start
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Hangman-Multiplayer");
        primaryStage.show();
        
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    /**
     * Verifies the User Input
     * @param name Name to check
     * @param ip IP to check
     * @param port Port to check
     * @return <code>null</code> if all the data is formally correct, else returns an Error Message
     */
    private String verify(String name, String ip, String port) {
        if (name.trim().isEmpty() || ip.trim().isEmpty() || port.trim().isEmpty())  {
            return "You have to fill in every value!";
        }
        
        // check if name is not allowed
        if (FORBIDDEN_NAMES.contains(name))  {
            return "That name is not allowed!";
        }
        
        // check if IP is formally correct
        if (!validIp(ip))  {
            return "IP-Address is not valid!";
        }
        
        // check if port is legitimate
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
    
    /**
     * Validates an IPv4 address. Returns true if valid.
     * 
     * @param ip The IPv4 address to validate
     * @return true if the argument contains a valid IPv4 address
     */
    private boolean validIp(String ip) {
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
     * Opens an AlertBox to show the User that his input was not correct.
     * @param errorResponse Additional Error Response giving the User more information
     */
    private void alert(String errorResponse) {
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
    
    void cancelGui()  {
        Scene scene = getScene("gameScene");
        StackPane pane = (StackPane) scene.getRoot();
        pane.getChildren().clear();
        
        VBox layout = new VBox(10);
        Label moreInfo = new Label("Check the IP and the Port and make sure that the Server has opened that Port!");
        moreInfo.setWrapText(true);
        moreInfo.setTextAlignment(TextAlignment.CENTER);
        layout.getChildren().addAll(new Label("Connection to Server failed!"), moreInfo);
        layout.setAlignment(Pos.CENTER);
        
        pane.getChildren().addAll(layout);
    }
    
    void acceptGui()  {
        Label label = (Label) getScene("gameScene").getRoot().getChildrenUnmodifiable().get(0);
        label.setText("Connection established, waiting for another player.");
    }
}
