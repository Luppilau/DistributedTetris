package com.Tetris.Model;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;

import javafx.scene.canvas.Canvas;

import static java.lang.Math.max;

public class TetrisModel {
    public Tetrimino[][] matrix = new Tetrimino[10][40];
    public FallingPiece current;
    public SimpleIntegerProperty level;
    public SimpleIntegerProperty lines;
    public SimpleIntegerProperty points;
    public boolean hasEnded = false;
    public ArrayList<Integer> linesCleared = new ArrayList<Integer>(4);

    private FallingPiece swap;
    private FallingPiece nextPiece;
    private boolean hasSwapped = false;
    private Canvas canvas;

    private RandomPieceGenerator generator = new RandomPieceGenerator();

    public TetrisModel(Canvas canvas) {
        current = generator.nextPiece();
        nextPiece = generator.nextPiece();
        level = new SimpleIntegerProperty(0);
        points = new SimpleIntegerProperty(0);
        lines = new SimpleIntegerProperty(0);
        this.canvas = canvas;
    }

    public void tick() {
        // Fall piece
        current.move(0, -1);
        if (!current.colliding(matrix)) {
            return;
        }
        current.move(0, 1);
        int[] relevantLines = current.relevantLines();

        lockPiece();

        hasSwapped = false;

        // Detect line clear
        int nLines = 0;
        lines: for (int y : relevantLines) {
            for (int x = 0; x < 10; x++) {
                if (matrix[x][y] == null) {
                    continue lines;
                }
            }
            linesCleared.add(y);
            moveMatrixDown(y, 1);
            nLines += 1;
        }
        lines.set(lines.get() + nLines);
        points.set(points.get() + calculateScore(nLines));
        if (level.getValue() >= 15 && lines.getValue() % 10 == 0) {
            level.set(level.get() + 1);
        } else if (lines.getValue() >= level.getValue() * 10 + 10
                || lines.getValue() >= max(100, 10 * level.getValue() - 50)) {
            level.set(level.get() + 1);
        }

    }

    private void lockPiece() {
        for (Pair sq : current.getSquares()) {
            assert (!(sq.x < 0 || sq.x > 9 || sq.y < 0 || sq.y > 39));
            matrix[sq.x][sq.y] = current.getType();
            if (sq.y >= 21) {
                hasEnded = true;
            }
        }

        current = nextPiece;
        nextPiece = generator.nextPiece();
        canvas.fireEvent(new CustomEvent(CustomEvent.NextPieceEvent));

    }

    public void rotateRight() {
        current.rotateRight(matrix);
    }

    public void rotateLeft() {
        current.rotateLeft(matrix);
    }

    public void moveLeft() {
        current.move(-1, 0);
        if (current.colliding(matrix)) {
            current.move(1, 0);
        }
    }

    public void moveRight() {
        current.move(1, 0);
        if (current.colliding(matrix)) {
            current.move(-1, 0);
        }
    }

    public void moveDown() {
        current.move(0, -1);
        if (current.colliding(matrix)) {
            current.move(0, 1);
        }
    }

    public void dropDown() {
        while (!current.colliding(matrix)) {
            current.move(0, -1);
        }
        current.move(0, 1);
    }

    public Pair getGhostPosition() {
        // Copy of old pos
        Pair ghost = new Pair(current.pos.x, current.pos.y);
        while (!current.colliding(matrix)) {
            current.move(0, -1);
        }
        current.move(0, 1);
        // Copy of ghost pos
        Pair ghostPos = new Pair(current.pos.x, current.pos.y);
        current.pos = ghost;
        return ghostPos;
    }

    public void swap() {
        if (!hasSwapped) {
            FallingPiece temp;
            if (swap != null) {
                temp = swap;
                temp.pos = new Pair(FallingPiece.DEFAULTX, FallingPiece.DEFAULTY);
            } else {
                temp = nextPiece;
                nextPiece = generator.nextPiece();
            }
            current.rotation = Rotation.O;
            swap = current;
            current = temp;
            hasSwapped = true;
        }
    }

    private void moveMatrixDown(int y, int amount) {
        for (int i = y; i < 39; i++) {
            for (int x = 0; x < 10; x++) {
                if ((i + amount) > 39) {
                    matrix[x][i] = null;
                } else {
                    matrix[x][i] = matrix[x][i + amount];
                }
            }
        }
    }

    public void moveMatrixUp(int amount) {
        for (int i = 9; i >= 0; i--) {
            for (int j = 39; j >= 0; j--) {
                if (j - amount < 0) {
                    matrix[i][j] = Tetrimino.TRASH;
                } else {
                    matrix[i][j] = matrix[i][j - amount];
                }
            }
        }
    }

    private int calculateScore(int n) {
        int p = 0;
        switch (n) {
            case 1:
                p = 40;
                break;
            case 2:
                p = 100;
                break;
            case 3:
                p = 300;
                break;
            case 4:
                p = 1200;
                break;
        }
        return p * (level.getValue() + 1);
    }

    public FallingPiece getSwap() {
        return swap;
    }

    public FallingPiece getNextPiece() {
        return nextPiece;
    }

}
