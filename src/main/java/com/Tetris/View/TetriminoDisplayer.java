package com.Tetris.View;

import com.Tetris.Model.FallingPiece;
import com.Tetris.Model.Pair;
import com.Tetris.Model.Tetrimino;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TetriminoDisplayer extends Canvas {
    private static final int WINDOWSIZE = 4;
    private final double squareUnit;
    private FallingPiece piece;
    private GraphicsContext context;

    public TetriminoDisplayer(double squareUnit) {
        super(squareUnit * WINDOWSIZE, squareUnit * WINDOWSIZE);
        this.squareUnit = squareUnit;
        context = getGraphicsContext2D();
        context.setStroke(Color.valueOf("#000000"));
    }

    public void render() {
        context.clearRect(0, 0, getWidth(), getHeight());
        context.setFill(Color.valueOf("#A9A9A9"));
        context.fillRect(0, 0, getWidth(), getHeight());
        if (piece != null) {
            renderPiece();
        }
    }

    private void renderPiece() {
        context.setFill(Tetrimino.getColor(piece.getType()));

        for (Pair sq : piece.getBlocks()) {
            double corner2 = getHeight() - (squareUnit * (1 + sq.y)) - squareUnit;
            double corner1 = squareUnit * sq.x + squareUnit;
            context.fillRect(corner1,
                    corner2,
                    squareUnit,
                    squareUnit);
            context.strokeRect(
                    corner1 + 1,
                    corner2 + 1,
                    squareUnit - 1,
                    squareUnit - 1);
        }
    }

    public void setPiece(FallingPiece piece) {
        this.piece = piece;
        render();
    }

}