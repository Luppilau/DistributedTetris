package com.Tetris.View;

import com.Tetris.Model.CustomEvent;
import com.Tetris.Model.TetrisInstance;
import com.Tetris.Model.TetrisModel;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class TetrisScene extends Scene {
    public TetrisScene(Parent root) {
        super(root);
        TetrisCanvas canvas = new TetrisCanvas();
        TetrisModel game = new TetrisModel();
        TetrisInstance instance = new TetrisInstance(canvas, game);
        TetriminoDisplayer swapPiece = new TetriminoDisplayer(canvas.squareUnit);
        TetriminoDisplayer nextPiece = new TetriminoDisplayer(canvas.squareUnit);
        instance.start();

        BorderPane layout = (BorderPane) root.lookup("#layout");
        layout.setCenter(canvas);

        VBox left = (VBox) root.lookup("#layoutLeft");
        left.getChildren().add(swapPiece);
        swapPiece.render();

        VBox right = (VBox) root.lookup("#layoutRight");
        right.getChildren().add(nextPiece);
        nextPiece.setPiece(game.getNextPiece());
        nextPiece.render();

        canvas.addEventHandler(CustomEvent.NextPieceEvent, (x) -> {
            System.out.println("test");
        });

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
                swapPiece.setPiece(game.getSwap());
            }
        });
    }
}
