package com.App;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.Tetris.Model.TetrisInstance;
import com.Tetris.Model.TetrisModel;
import com.Tetris.TetrisCanvas.TetrisCanvas;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        TetrisCanvas canvas = new TetrisCanvas();
        TetrisModel game = new TetrisModel();
        TetrisInstance instance = new TetrisInstance(canvas, game);

        instance.start();
        VBox vbox = new VBox(canvas.getCanvas());
        Scene scene = new Scene(vbox);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent key) -> {
            if (key.getCode().equals(KeyCode.DOWN)) {
                instance.moveDown();
            } else if (key.getCode().equals(KeyCode.LEFT)) {
                instance.moveLeft();
            } else if (key.getCode().equals(KeyCode.RIGHT)) {
                instance.moveRight();
            } else if (key.getCode().equals(KeyCode.UP) ||
                    key.getCode().equals(KeyCode.X)) {
                instance.rotateRight();
            } else if (key.getCode().equals(KeyCode.Z) ||
                    key.getCode().equals(KeyCode.CONTROL)) {
                instance.rotateLeft();
            } else if (key.getCode().equals(KeyCode.SPACE) ||
                    key.getCode().equals(KeyCode.Z)) {
                instance.dropDown();
            }
        });
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

}