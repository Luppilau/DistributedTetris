package com.App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

import com.Tetris.View.TetrisScene;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // HomeScreen home = new HomeScreen();
        Parent tetrisRoot = FXMLLoader.load(getClass().getClassLoader().getResource("TetrisLayout.fxml"));
        TetrisScene tetrisScene = new TetrisScene(tetrisRoot);
        stage.setScene(tetrisScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}