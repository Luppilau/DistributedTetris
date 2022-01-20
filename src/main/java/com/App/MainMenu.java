package com.App;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

import com.Server.Server;
import com.Server.ServerMessages;
import com.Tetris.View.GameView;

import org.jspace.RemoteSpace;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class MainMenu extends Scene {
    Stage stage;

    Object[] sessionDetails;

    public MainMenu(Parent root, Stage stage) {
        super(root,450,250);
        this.stage = stage;


        TextField connectTextField = (TextField) root.lookup("#field");
        Button connectButton = (Button) root.lookup("#button");
        Text errorText = (Text) root.lookup("#error");

        connectButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            String ip = connectTextField.getText();
            String URI = "tcp://" + ip + "/lobby?keep";
            try {
                RemoteSpace lobby = new RemoteSpace(URI);
                navigateToGame(lobby, ip);
            } catch (IOException e) {
                // Try again!
                errorText.setText("ERROR! Try again");
            }
        });
    }

    public void navigateToMainMenu() {
        stage.setScene(this);
        stage.show();
    }

    public void navigateToGame(RemoteSpace space, String ip) throws IOException {
        Parent gameRoot = FXMLLoader.load(getClass().getClassLoader().getResource("GameLayout.fxml"));

        // Connect to the server and get private space for game.

        try {
            space.put(ServerMessages.gameRequest());

            Task<Void> gameSetUp = new Task<Void>() {

                @Override
                protected Void call() {
                    try {
                        sessionDetails = space.get(ServerMessages.sessionDetails.getFields());
                        succeeded();
                    } catch (InterruptedException e) {
                        failed();
                        e.printStackTrace();
                    }
                    return null;
                }

            };
            gameSetUp.run();
            gameSetUp.setOnSucceeded(event -> {
                int gameID = (int) sessionDetails[1];
                int playerID = (int) sessionDetails[2];
                int opponentID = (int) sessionDetails[3];
                System.out.println("Got playerID: " + playerID + " and gameID: " + gameID);

                try {
                    String URI = Server.getURI(ip, gameID);
                    RemoteSpace game = new RemoteSpace(URI);
                    game.put(ServerMessages.okMessage);
                    GameView gameView = new GameView(gameRoot, game, playerID, opponentID);
                    stage.setScene(gameView);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
