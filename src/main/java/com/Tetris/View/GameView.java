package com.Tetris.View;

import java.io.IOException;

import com.Tetris.Model.TetrisInstance;
import com.Tetris.Model.TetrisModel;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;

public class GameView extends Scene {
    public GameView(Parent root) throws IOException {
        super(root);
        HBox me = (HBox) root;

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
    }
}
