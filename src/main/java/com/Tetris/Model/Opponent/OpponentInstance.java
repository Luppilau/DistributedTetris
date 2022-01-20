package com.Tetris.Model.Opponent;

import com.Tetris.Model.Tetriminos.FallingTetrimino;
import com.Tetris.Model.Tetriminos.Pair;
import com.Tetris.Model.Tetriminos.Rotation;
import com.Tetris.Model.Tetriminos.Tetrimino;
import com.Tetris.View.TetrisCanvas;
import com.Tetris.View.TetrisScene;

/*
    Handler for view of opponent board on client screen. 
*/
public class OpponentInstance {
    private TetrisCanvas canvas;
    private OpponentModel model;
    private TetrisScene scene;

    public OpponentInstance(TetrisCanvas canvas, OpponentModel model, TetrisScene scene) {
        this.canvas = canvas;
        this.model = model;
        this.scene = scene;
        canvas.renderOpponent(model);
    }

    public void placePiece(Tetrimino kind, Pair location, Rotation rot) {
        model.placePiece(kind, location, rot);
        canvas.renderOpponent(model);
    }

    public void clearLines(int[] lines) {
        model.clearLines(lines);
        canvas.renderOpponent(model);
    }

    public void sendLines(int amount, int hole) {
        model.sendLines(amount, hole);
        canvas.renderOpponent(model);
    }

    public void swap(Tetrimino type) {
        scene.swapPiece.setPiece(FallingTetrimino.newFallingPiece(type));
    }

    public void nextPiece(Tetrimino type) {
        scene.nextPiece.setPiece(FallingTetrimino.newFallingPiece(type));
    }

    public void setScore(int score) {
        model.setScore(score);
    }

    public void setLevel(int level) {
        model.setLevel(level);
    }

    public void incrementLines(int length) {
        model.incrementLines(length);
    }

    public void endGame() {
        model.hasEnded = true;
        canvas.renderOpponent(model);
    }
}