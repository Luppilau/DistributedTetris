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

        MainMenu home = new MainMenu();
        // Parent gameRoot =
        // FXMLLoader.load(getClass().getClassLoader().getResource("GameLayout.fxml"));
        // GameView gameView = new GameView(gameRoot);
        Scene homeView = new Scene(home);
        stage.setResizable(false);
        stage.setScene(homeView);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}