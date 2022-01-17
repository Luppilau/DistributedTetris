package com.Tetris.View;

import java.io.IOException;

import com.Tetris.Model.CustomEvent;
import com.Tetris.Model.TetrisInstance;
import com.Tetris.Model.TetrisModel;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class TetrisScene extends BorderPane {
    public TetriminoDisplayer swapPiece;
    public TetriminoDisplayer nextPiece;

    public TetrisScene(TetrisCanvas canvas, TetrisModel game, TetrisInstance instance) throws IOException {
        swapPiece = new TetriminoDisplayer(canvas.squareUnit);
        nextPiece = new TetriminoDisplayer(canvas.squareUnit);

        setPrefHeight(500);
        setStyle("-fx-border-color: blue ;-fx-border-width: 5;");

        VBox left = new VBox();
        left.getChildren().add(swapPiece);
        swapPiece.render();

        VBox right = new VBox();
        Parent scoreBoard = FXMLLoader.load(getClass().getClassLoader().getResource("ScoreBoard.fxml"));
        Label score = (Label) scoreBoard.lookup("#score");
        score.textProperty().bind(game.points.asString());
        Label lines = (Label) scoreBoard.lookup("#lines");
        lines.textProperty().bind(game.lines.asString());
        Label level = (Label) scoreBoard.lookup("#level");
        level.textProperty().bind(game.level.asString());
        right.getChildren().add(nextPiece);
        right.getChildren().add(scoreBoard);
        nextPiece.setPiece(game.getNextPiece());
        nextPiece.render();

        setCenter(canvas);
        setLeft(left);
        setRight(right);

        canvas.addEventHandler(CustomEvent.NextPieceEvent, (x) -> {
            nextPiece.setPiece(game.getNextPiece());
        });
    }
}
