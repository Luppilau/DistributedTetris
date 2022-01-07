package com.Tetris.Model;

public class TetrisModel {
    public Tetrimino[][] matrix = new Tetrimino[10][40];
    public FallingPiece current;
    public int level;
    public int points;
    private FallingPiece swap;
    private boolean hasSwapped = false;

    private RandomPieceGenerator generator = new RandomPieceGenerator();

    public TetrisModel() {
        current = generator.nextPiece();
    }

    private void lockPiece() {
        for (Pair sq : current.getSquares()) {
            assert (!(sq.x < 0 || sq.x > 9 || sq.y < 0 || sq.y > 39));
            matrix[sq.x][sq.y] = current.getType();
        }
        current = generator.nextPiece();
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
        lines: for (int y : relevantLines) {
            for (int x = 0; x < 10; x++) {
                if (matrix[x][y] == null) {
                    continue lines;
                }
            }
            moveMatrixDown(y, 1);
        }

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
        tick();
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
                temp = generator.nextPiece();
            }
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

}
