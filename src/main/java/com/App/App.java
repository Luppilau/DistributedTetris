package com.App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

import com.Tetris.View.GameView;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // HomeScreen home = new HomeScreen();
        Parent gameRoot = FXMLLoader.load(getClass().getClassLoader().getResource("GameLayout.fxml"));
        GameView gameView = new GameView(gameRoot);
        stage.setScene(gameView);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}