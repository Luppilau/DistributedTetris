package com.Tetris.View;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import com.Tetris.Model.Pair;
import com.Tetris.Model.Tetrimino;
import com.Tetris.Model.TetrisModel;

public class TetrisCanvas extends Canvas {
    public final double squareUnit;
    private static final double defaultHeight = 500;
    private static final double defaultWidth = 250;

    private GraphicsContext context;

    public TetrisCanvas() {
        super(defaultWidth, defaultHeight);
        squareUnit = defaultWidth / 10;
        context = getGraphicsContext2D();
    }

    public TetrisCanvas(double width, double height) {
        super(width, height);
        squareUnit = width / 10;
        context = getGraphicsContext2D();
    }

    public void render(TetrisModel game) {
        context.clearRect(0, 0, getWidth(), getHeight());
        context.setFill(Color.valueOf("#A9A9A9"));
        context.fillRect(0, 0, getWidth(), getHeight());
        context.setStroke(Color.valueOf("#000000"));
        for (int x = 0; x < game.matrix.length; x++) {
            for (int y = 0; y < game.matrix[0].length; y++) {
                if (game.matrix[x][y] == null) {
                    // context.setFill(Color.valueOf("#A9A9A9"));
                    continue;
                } else {
                    context.setFill(Tetrimino.getColor(game.matrix[x][y]));
                }
                paintSquareWithBorder(x, y);

            }
        }

        Pair ghostPos = game.getGhostPosition();

        for (Pair sq : game.current.getBlocks()) {
            context.setFill(Color.valueOf("D3D3D3"));
            paintSquareGhost(ghostPos.x + sq.x, ghostPos.y + sq.y);
        }

        for (Pair sq : game.current.getSquares()) {
            context.setFill(Tetrimino.getColor(game.current.getType()));
            paintSquareWithBorder(sq.x, sq.y);
        }

    }

    private void paintSquareWithBorder(int x, int y) {
        double corner2 = getHeight() - (squareUnit * (1 + y));
        double corner1 = squareUnit * x;
        context.fillRect(corner1,
                corner2,
                squareUnit,
                squareUnit);
        context.strokeRect(corner1,
                corner2,
                squareUnit,
                squareUnit);

    }

    private void paintSquareGhost(int x, int y) {
        double corner2 = getHeight() - (squareUnit * (1 + y));
        double corner1 = squareUnit * x;
        context.fillRect(corner1,
                corner2,
                squareUnit,
                squareUnit);
        context.strokeRect(corner1,
                corner2,
                squareUnit,
                squareUnit);

    }

    public void drawGrey(int y) {
        context.setStroke(Color.valueOf("#000000"));
        context.setFill(Tetrimino.getColor(Tetrimino.TRASH));
        for (int x = 0; x < 10; x++) {
            paintSquareWithBorder(x, y);
        }
    }
}
