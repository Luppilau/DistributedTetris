package com.App;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

import com.Tetris.View.GameView;

import org.jspace.RemoteSpace;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class MainMenu extends Scene {
    Stage stage;

    public MainMenu(Parent root, Stage stage) {
        super(root);
        this.stage = stage;

        TextField connectTextField = (TextField) root.lookup("#field");
        Button connectButton = (Button) root.lookup("#button");
        Text errorText = (Text) root.lookup("#error");

        connectButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            String ip = connectTextField.getText();
            String URI = "tcp://" + ip + ":9090/lobby?keep";
            try {
                RemoteSpace lobby = new RemoteSpace(URI);
                navigateToGame(lobby);
            } catch (IOException e) {
                // Try again!
                errorText.setText("ERROR! Try again");
                System.out.println("ERROR");
            }

            System.out.println(connectTextField.getText());
        });
    }

    public void navigateToMainMenu() {
        stage.setScene(this);
        stage.show();
    }

    public void navigateToGame(RemoteSpace space) throws IOException {
        Parent gameRoot = FXMLLoader.load(getClass().getClassLoader().getResource("GameLayout.fxml"));
        GameView gameView = new GameView(gameRoot, space);

        stage.setScene(gameView);
        stage.show();
    }

}
