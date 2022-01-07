package com.Tetris.View;

import com.Tetris.Model.TetrisInstance;
import com.Tetris.Model.TetrisModel;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

public class TetrisScene extends Scene {
    public TetrisScene(Parent root) {
        super(root);
        TetrisCanvas canvas = new TetrisCanvas();
        TetrisModel game = new TetrisModel();
        TetrisInstance instance = new TetrisInstance(canvas, game);
        TetriminoDisplayer swap = new TetriminoDisplayer(canvas.squareUnit);
        instance.start();
        swap.render();

        BorderPane layout = (BorderPane) root.lookup("#layout");
        layout.setCenter(canvas);
        layout.setLeft(swap);

        this.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent key) -> {
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
            } else if (key.getCode().equals(KeyCode.SPACE)) {
                instance.dropDown();
            } else if (key.getCode().equals(KeyCode.SHIFT)) {
                instance.swap();
                swap.setPiece(game.getSwap());
            }
        });
    }

}
