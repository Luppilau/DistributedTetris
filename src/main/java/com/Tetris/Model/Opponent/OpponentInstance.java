package com.Tetris.Model.Opponent;

import com.Tetris.Model.FallingPiece;
import com.Tetris.Model.Pair;
import com.Tetris.Model.Rotation;
import com.Tetris.Model.Tetrimino;
import com.Tetris.View.TetrisCanvas;
import com.Tetris.View.TetrisScene;

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

    // lines must be sorted greatest to smallest
    public void clearLines(int[] lines) {
        model.clearLines(lines);
        canvas.renderOpponent(model);
    }

    public void sendLines(int amount, int hole) {
        model.sendLines(amount, hole);
        canvas.renderOpponent(model);
    }

    public void swap(Tetrimino type) {
        scene.swapPiece.setPiece(FallingPiece.newFallingPiece(type));
    }

    public void nextPiece(Tetrimino type) {
        scene.nextPiece.setPiece(FallingPiece.newFallingPiece(type));
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