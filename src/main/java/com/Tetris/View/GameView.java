package com.Tetris.View;

import java.io.IOException;

import com.Tetris.Model.TetrisInstance;
import com.Tetris.Model.TetrisModel;
import com.Tetris.Model.Opponent.OpponentInstance;
import com.Tetris.Model.Opponent.OpponentModel;

import com.Tetris.Net.Updates.UpdateHandler;

import org.jspace.RemoteSpace;
import org.jspace.SequentialSpace;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

public class GameView extends Scene {
    private Thread updateHandlerHandle;
    private TetrisModel game;
    private TetrisInstance instance;
    private TetrisScene gameScene;

    public GameView(Parent root, RemoteSpace space, int playerID, int opponentID) throws IOException {
        super(root);
        HBox me = (HBox) root;
        me.setSpacing(25);

        SequentialSpace junkQueue = new SequentialSpace();

        TetrisCanvas canvas = new TetrisCanvas();
        game = new TetrisModel(canvas, space, junkQueue, playerID);
        instance = new TetrisInstance(canvas, game);
        gameScene = new TetrisScene(canvas, game);

        TetrisCanvas oppCanvas = new TetrisCanvas();
        OpponentModel oppGame = new OpponentModel(oppCanvas);
        TetrisScene oppGameScene = new TetrisScene(oppCanvas, oppGame);
        OpponentInstance oppInstance = new OpponentInstance(oppCanvas, oppGame, oppGameScene);

        me.getChildren().add(gameScene);
        me.getChildren().add(oppGameScene);
        instance.start();

        updateHandlerHandle = new Thread(
                new UpdateHandler(space, opponentID, playerID, instance, oppInstance, junkQueue));
        updateHandlerHandle.start();

        this.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent key) -> handleKeyEvent(key));
        this.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent key) -> {
            if (instance.keyPressed == key.getCode()) {
                instance.keyPressed = null;
            }
        });
    }

    private void handleKeyEvent(KeyEvent key) {
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
        } else if (key.getCode().equals(KeyCode.ESCAPE)) {
            System.exit(0);
        }
    }
}
