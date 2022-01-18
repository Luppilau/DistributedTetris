package com.Tetris.Model.Opponent;

import com.Tetris.Model.Pair;
import com.Tetris.Model.Rotation;
import com.Tetris.Model.Tetrimino;
import com.Tetris.View.TetrisCanvas;

public class OpponentInstance {
    private TetrisCanvas canvas;
    private OpponentModel model;

    public OpponentInstance(TetrisCanvas canvas, OpponentModel model) {
        this.canvas = canvas;
        this.model = model;
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

    public void sendLines(int amount) {
        model.sendLines(amount);
        canvas.renderOpponent(model);
    }
}