package tk.dmanstrator.hangman.client;

import java.util.HashMap;
import java.util.Map;


import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.stage.Stage;
import tk.dmanstrator.hangman.utils.GuiUtils;

/**
 * One of the two Main-Programs of the Hangman Game, starts a new Client.
 * Class represents the Client for the Hangman. With initializing, it builds the GUI for the Client.
 * @author DManstrator
 *
 */
public class HangmanClient extends Application  {
    
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
        VBox mainLayout = new VBox(20);
        
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
            GuiUtils guiUtils = new GuiUtils();
            String name = nameField.getText();
            String ipAddr = ipField.getText();
            String port = portField.getText();
            String errorResponse = guiUtils.verify(name, ipAddr, port);
            if (errorResponse == null)  {
                config = new ClientConfiguration(name, ipAddr, Integer.parseInt(port));
                connectButton.setDisable(false);
                primaryStage.setScene(mainScene);
                primaryStage.setTitle("Hangman-Multiplayer");
            }  else  {
                guiUtils.alert(errorResponse);
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
        
        primaryStage.setOnCloseRequest(t ->  {
            Platform.exit();
            System.exit(0);
        });
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
