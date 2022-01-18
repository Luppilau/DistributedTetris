package com.Tetris.View;

import java.io.IOException;

import com.Tetris.Model.TetrisInstance;
import com.Tetris.Model.TetrisModel;
import com.Tetris.Model.Opponent.OpponentInstance;
import com.Tetris.Model.Opponent.OpponentModel;

import org.jspace.RemoteSpace;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

public class GameView extends Scene {
    public GameView(Parent root, RemoteSpace space, int playerID, int opponentID) throws IOException {
        super(root);
        HBox me = (HBox) root;
        me.setSpacing(25);

        TetrisCanvas canvas = new TetrisCanvas();
        TetrisModel game = new TetrisModel(canvas, space, playerID);
        TetrisInstance instance = new TetrisInstance(canvas, game);
        TetrisScene gameScene = new TetrisScene(canvas, game);

        TetrisCanvas oppCanvas = new TetrisCanvas();
        OpponentModel oppGame = new OpponentModel(oppCanvas);
        OpponentInstance oppInstance = new OpponentInstance(oppCanvas, oppGame);
        TetrisScene oppGameScene = new TetrisScene(oppCanvas, oppGame);

        me.getChildren().add(gameScene);
        me.getChildren().add(oppGameScene);
        instance.start();

        this.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent key) -> {
            if (key.getCode().equals(KeyCode.DOWN)) {
                instance.keyPressed = KeyCode.DOWN;
                instance.keyPressedFrames = 0;
            } else if (key.getCode().equals(KeyCode.LEFT)) {
                instance.keyPressed = KeyCode.LEFT;
                instance.keyPressedFrames = 0;
            } else if (key.getCode().equals(KeyCode.RIGHT)) {
                instance.keyPressed = KeyCode.RIGHT;
                instance.keyPressedFrames = 0;
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
                gameScene.swapPiece.setPiece(game.getSwap());
            }
        });
        this.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent key) -> {
            if (instance.keyPressed == key.getCode()) {
                instance.keyPressed = null;
            }
        });
    }
}
