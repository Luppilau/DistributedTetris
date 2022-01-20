package com.App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent mainMenuRoot = FXMLLoader.load(getClass().getClassLoader().getResource("MainMenuLayout.fxml"));
        MainMenu home = new MainMenu(mainMenuRoot, stage);

        stage.setResizable(false);
        stage.setScene(home);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}