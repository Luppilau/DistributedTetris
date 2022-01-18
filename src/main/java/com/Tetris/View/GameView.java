package com.Tetris.View;

import java.io.IOException;

import com.Server.ServerMessages;
import com.Tetris.Model.TetrisInstance;
import com.Tetris.Model.TetrisModel;

import org.jspace.RemoteSpace;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

public class GameView extends Scene {
    public GameView(Parent root, RemoteSpace space) throws IOException {
        super(root);
        HBox me = (HBox) root;
        me.setSpacing(25);

        // Connect to the server!
        try {
            space.put(ServerMessages.gameRequest());
            space.get(ServerMessages.sessionDetails.getFields());
        } catch (InterruptedException e) {
        }

        TetrisCanvas canvas = new TetrisCanvas();
        TetrisModel game = new TetrisModel(canvas);
        TetrisInstance instance = new TetrisInstance(canvas, game);
        TetrisScene gameScene = new TetrisScene(canvas, game, instance);

        TetrisCanvas oppCanvas = new TetrisCanvas();
        TetrisModel oppGame = new TetrisModel(oppCanvas);
        TetrisInstance oppInstance = new TetrisInstance(oppCanvas, oppGame);
        TetrisScene oppGameScene = new TetrisScene(oppCanvas, oppGame, oppInstance);

        me.getChildren().add(gameScene);
        me.getChildren().add(oppGameScene);
        instance.start();
        oppInstance.start();

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
