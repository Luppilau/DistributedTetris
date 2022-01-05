package com.App;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.jspace.SequentialSpace;
import org.jspace.ActualField;
import org.jspace.Space;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        String javaVersion = "Eyo";
        String javafxVersion = "Yello";

        Label label = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        Scene scene = new Scene(new StackPane(label), 640, 480);
        stage.setScene(scene);
        stage.show();

        Space board = new SequentialSpace();
        try {
            board.put("fork", 1);
        } catch (Exception e) {
            // TODO: handle exception
        }
        try {
            Object[] test = board.get(new ActualField("fork"), new ActualField(1));
            System.out.println("Waiter put fork " + " on the table.");
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public static void main(String[] args) {
        launch();
    }

}