package com.Tetris.View;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import com.Tetris.Model.Tetriminos.Pair;
import com.Tetris.Model.Tetriminos.Tetrimino;
import com.Tetris.Model.TetrisModel;
import com.Tetris.Model.Opponent.OpponentModel;

/*
    Main rendering of a tetrisboard in JavaFx
*/
public class TetrisCanvas extends Canvas {
    public final double squareUnit;
    private static final double defaultHeight = 500;
    private static final double defaultWidth = 250;

    private static final Color backgroundColor = Color.valueOf("#000000");
    private static final Color outlineColor = Color.valueOf("#000000");
    private static final Color junkColor = Color.valueOf("#FF0000");

    private GraphicsContext context;

    public TetrisCanvas() {
        super(defaultWidth, defaultHeight);
        squareUnit = defaultWidth / 10;
        context = getGraphicsContext2D();
    }

    public void render(TetrisModel game) {
        context.clearRect(0, 0, getWidth(), getHeight());
        context.setFill(backgroundColor);
        context.fillRect(0, 0, getWidth(), getHeight());
        context.setStroke(outlineColor);
        for (int x = 0; x < game.matrix.length; x++) {
            for (int y = 0; y < game.matrix[0].length; y++) {
                if (game.matrix[x][y] == null) {
                    continue;
                }
                context.setFill(Tetrimino.getColor(game.matrix[x][y]));
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

        drawIncomingJunk(game.getCountIncomingJunk());
    }

    public void renderOpponent(OpponentModel game) {
        context.clearRect(0, 0, getWidth(), getHeight());
        context.setFill(backgroundColor);
        context.fillRect(0, 0, getWidth(), getHeight());
        context.setStroke(outlineColor);
        for (int x = 0; x < game.matrix.length; x++) {
            for (int y = 0; y < game.matrix[0].length; y++) {
                if (game.matrix[x][y] == null) {
                    continue;
                }
                if (game.hasEnded) {
                    context.setFill(Tetrimino.getGreyColor(game.matrix[x][y]));
                } else {
                    context.setFill(Tetrimino.getColor(game.matrix[x][y]));
                }
                paintSquareWithBorder(x, y);
            }
        }
    }

    private void paintSquareWithBorder(int x, int y) {
        double corner2 = getHeight() - (squareUnit * (1 + y));
        double corner1 = squareUnit * x;
        context.fillRect(
                corner1,
                corner2,
                squareUnit,
                squareUnit);
        context.strokeRect(
                corner1 + 1,
                corner2 + 1,
                squareUnit - 1,
                squareUnit - 1);

    }

    private void paintSquareGhost(int x, int y) {
        double corner2 = getHeight() - (squareUnit * (1 + y));
        double corner1 = squareUnit * x;
        context.fillRect(
                corner1,
                corner2,
                squareUnit,
                squareUnit);
        context.strokeRect(
                corner1 + 1,
                corner2 + 1,
                squareUnit - 1,
                squareUnit - 1);

    }

    public void drawGrey(int y) {
        context.setStroke(outlineColor);
        context.setFill(Tetrimino.getColor(Tetrimino.TRASH));
        for (int x = 0; x < 10; x++) {
            paintSquareWithBorder(x, y);
        }
    }

    public void clearSquare(int clearedLine, int i) {
        context.setFill(backgroundColor);
        context.setStroke(backgroundColor);
        paintSquareWithBorder(i - 1, clearedLine);
        paintSquareWithBorder(10 - i, clearedLine);
    }

    public void drawIncomingJunk(int amount) {
        context.setFill(junkColor);
        context.fillRect(
                squareUnit * 10 - 5,
                20 * squareUnit - amount * squareUnit,
                5,
                amount * squareUnit);
    }
}
