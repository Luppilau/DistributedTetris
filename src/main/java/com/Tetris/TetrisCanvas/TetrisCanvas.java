package com.Tetris.TetrisCanvas;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import com.Tetris.Model.Pair;
import com.Tetris.Model.Tetrimino;
import com.Tetris.Model.TetrisModel;

public class TetrisCanvas {
    private final double squareUnit;
    private static final double defaultHeight = 500;
    private static final double defaultWidth = 250;

    private Canvas canvas;
    private GraphicsContext context;

    public TetrisCanvas() {
        canvas = new Canvas();
        canvas.setHeight(defaultHeight);
        canvas.setWidth(defaultWidth);
        squareUnit = defaultWidth / 10;
        context = canvas.getGraphicsContext2D();
    }

    public TetrisCanvas(double width, double height) {
        canvas = new Canvas(width, height);
        squareUnit = width / 10;
        context = canvas.getGraphicsContext2D();
    }

    public void render(TetrisModel game) {
        context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        context.setFill(Color.valueOf("#A9A9A9"));
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int x = 0; x < game.matrix.length; x++) {
            for (int y = 0; y < game.matrix[0].length; y++) {
                if (game.matrix[x][y] == null) {
                    continue;
                }
                context.setFill(Tetrimino.getColor(game.matrix[x][y]));

                paintSquare(x, y);

            }
        }

        context.setFill(Tetrimino.getColor(game.current.getType()));
        for (Pair sq : game.current.getSquares()) {
            paintSquare(sq.x, sq.y);
        }

    }

    private void paintSquare(int x, int y) {
        double corner2 = canvas.getHeight() - (squareUnit * (1 + y));
        double corner1 = squareUnit * x;

        context.fillRect(corner1,
                corner2,
                squareUnit,
                squareUnit);
    }

    public Canvas getCanvas() {
        return canvas;
    }

}
