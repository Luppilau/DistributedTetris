package com.Tetris.Model.Opponent;

import com.Tetris.Model.FallingPiece;
import com.Tetris.Model.Pair;
import com.Tetris.Model.Rotation;
import com.Tetris.Model.Tetrimino;
import com.Tetris.Model.TetrisModel;

import javafx.scene.canvas.Canvas;

public class OpponentModel extends TetrisModel {

    public OpponentModel(Canvas canvas) {
        super(canvas);
    }

    public void placePiece(Tetrimino kind, Pair pos, Rotation rot) {
        FallingPiece piece = FallingPiece.newFallingPiece(kind);
        piece.setRotation(rot);
        piece.setLocation(pos);
        for (Pair sq : piece.getSquares()) {
            this.matrix[sq.x][sq.y] = kind;
        }
    }

    public void clearLines(int[] lines) {
        for (int y : lines) {
            super.moveMatrixDown(y, 1);
        }
    }

    public void sendLines(int amount, int hole) {
        super.moveMatrixUp(amount, hole);
    }
}
