package tk.dmanstrator.hangman.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import tk.dmanstrator.hangman.client.ClientConnection;
import tk.dmanstrator.hangman.utils.GuiUtils;

/**  
 * One of the two Main-Programs of the Hangman Game, executes the Server.
 * Waits for the Clients and connects them to the Game.
 * 
 * @author DManstrator
 */
public class HangmanServer extends Application implements Runnable {
    
    /**
     * Server Configuration which contains the Port.
     */
    private ServerConfiguration config = new ServerConfiguration();
    
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
        new Thread(() -> {
            int port = config.getPort();
            try (ServerSocket server = new ServerSocket(port)) {
                List<ClientConnection> clientlist = new ArrayList<>();
                System.out.println("Game Server waiting on Port " + port + "!");
                while (true) {
                    Socket client = server.accept();

                    synchronized (clientlist) {
                        ClientConnection cc = new ClientConnection(client, playerId);
                        int playerNr = (int) (playerId % 2) + 1;
                        cc.start();

                        clientlist.add(cc);
                        System.out.println("Client " + playerNr + " (playerID: " + playerId + ") accepted!");

                        playerId++; // increase playerId;
                        if (clientlist.size() == 2) {
                            synchronized (games) { // TODO no sync needed?
                                Game game = new Game(clientlist);
                                game.start();
                                games.add(game);
                                clientlist.clear(); // wait for two new clients
                            }
                        }
                    }
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    /**
     * Main-Method of the Hangman Server.
     * @param args Arguments
     */
    public static void main(String[] args) {
        boolean cmdStart = false;
        if (args.length != 0)  {
            GuiUtils guiUtils = new GuiUtils();
            String errorResponse = guiUtils.verifyPort(args[0]);
            if (errorResponse != null)  {
                System.err.println("Could not start the Server: " + errorResponse);
                System.exit(-1);
            }  else  {
                int port = Integer.parseInt(args[0]);
                HangmanServer hangmanServer = new HangmanServer();
                hangmanServer.config = new ServerConfiguration(port);
                hangmanServer.run();
                cmdStart = true;
            }
        }
        
        if (!cmdStart) {
            launch(args);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Main Layout
        VBox mainLayout = new VBox(20);
        
        Label mainWelcomeLabel = new Label("Welcome!");
        mainWelcomeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        
        Label mainInfoLabel = new Label("Please choose a Port to start the Server!");
        mainInfoLabel.setWrapText(true);
        mainInfoLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        mainInfoLabel.setTextAlignment(TextAlignment.CENTER);
        
        //Creating a GridPane container
        GridPane grid = new GridPane();
        grid.getColumnConstraints().addAll(new ColumnConstraints(25));
        
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(5);
        grid.setHgap(5);
        
        // Port Select
        final TextField portField = new TextField();
        if (config.isValid())  {
            portField.setText(String.valueOf(config.getPort()));
        }
        final Button startButton = new Button("Start");
        portField.setPromptText("Enter a Port Number");
        Label portInputLabel = new Label("Port:");
        GridPane.setColumnIndex(portInputLabel, 0);
        GridPane.setRowIndex(portInputLabel, 0);
        GridPane.setHalignment(portInputLabel, HPos.RIGHT);
        GridPane.setConstraints(portField, 1, 0);
        GridPane.setConstraints(startButton, 2, 0);
        grid.getChildren().addAll(portInputLabel, portField, startButton);
        
        mainLayout.getChildren().addAll(mainWelcomeLabel, mainInfoLabel, grid);
        mainLayout.setAlignment(Pos.CENTER);
        Scene mainScene = new Scene(mainLayout, 300, 400);
        
        // Running Scene
        VBox runningLayout = new VBox();
        Label upperRunningLabel = new Label("Sever is running, close it");
        Label lowerRunningLabel = new Label("whenever you want.");
        upperRunningLabel.setWrapText(true);
        upperRunningLabel.setTextAlignment(TextAlignment.CENTER);
        runningLayout.setAlignment(Pos.CENTER);
        runningLayout.getChildren().addAll(upperRunningLabel, lowerRunningLabel);
        
        Scene gameScene = new Scene(runningLayout, 300, 300);
        
        // Listeners
        startButton.setOnAction(e -> {
            GuiUtils guiUtils = new GuiUtils();
            String port = portField.getText();
            String errorResponse = guiUtils.verifyPort(port);
            if (errorResponse == null)  {
                upperRunningLabel.setText("Sever is running on Port " + port + ", close it");
                int iPort = Integer.parseInt(port);
                if (config.getPort() != iPort)  {
                    config = new ServerConfiguration(iPort);
                }
                primaryStage.setScene(gameScene);
                
                this.run();
            }  else  {
                guiUtils.alert(errorResponse);
            }
        });
        
        // Start
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Hangman-Multiplayer Server");
        primaryStage.show();
        
        primaryStage.setOnCloseRequest(t ->  {
            Platform.exit();
            System.exit(0);
        });
    }
}