package com.Tetris.Model;

import com.Tetris.Model.Tetriminoes.*;

import java.util.HashSet;
import java.util.Set;

public class FallingPiece {
    public static final int DEFAULTX = 5;
    public static final int DEFAULTY = 21;
    protected Rotation rotation = Rotation.O;
    public Pair pos;

    public FallingPiece() {
        pos = new Pair(DEFAULTX, DEFAULTY);
    }

    public static FallingPiece NewFallingPiece(Tetrimino T) {
        switch (T) {
            case I:
                return new I();
            case J:
                return new J();
            case L:
                return new L();
            case O:
                return new O();
            case S:
                return new S();
            case T:
                return new T();
            case Z:
                return new Z();
        }
        return null;
    }

    public void rotateRight(Tetrimino[][] matrix) {
        Pair initialPosition = Pair.add(pos, new Pair(0, 0));
        rotation = rotation.right();
        for (Pair poses : getKickPosistionsRight()) {
            pos = Pair.add(poses, initialPosition);
            if (!colliding(matrix)) {
                return;
            }
        }
        pos = initialPosition;
        rotation = rotation.left();
        return;
    }

    public void rotateLeft(Tetrimino[][] matrix) {
        rotation = rotation.left();
        Pair initialPosition = Pair.add(pos, new Pair(0, 0));
        for (Pair poses : getKickPositionsLeft()) {
            pos = Pair.add(poses, initialPosition);
            if (!colliding(matrix)) {
                return;
            }
        }
        pos = initialPosition;
        rotation = rotation.right();
        return;
    }

    public Pair[] getSquares() {
        Pair[] out = new Pair[4];
        int i = 0;
        for (Pair offset : getBlocks()) {
            out[i] = Pair.add(offset, pos);
            i++;
        }
        return out;
    }

    public Tetrimino getType() {
        return null;
    }

    public Pair[] getBlocks() {
        return new Pair[0];
    }

    public void move(int x, int y) {
        pos.x += x;
        pos.y += y;
    }

    public int[] relevantLines() {
        Set<Integer> positions = new HashSet<Integer>();

        for (Pair sq : getSquares()) {
            positions.add(sq.y);
        }
        int[] out = positions.stream().mapToInt(Number::intValue).toArray();

        for (int i = 0; i < out.length / 2; i++) {
            int temp = out[i];
            out[i] = out[out.length - i - 1];
            out[out.length - i - 1] = temp;
        }

        return out;
    }

    public boolean colliding(Tetrimino[][] matrix) {
        for (Pair sq : getSquares()) {
            if (sq.x < 0 || sq.x > 9 || sq.y < 0 || sq.y > 39) {
                return true;
            }
            if (matrix[sq.x][sq.y] != null) {
                return true;
            }
        }
        return false;
    }

    protected Pair[] getKickPosistionsRight() {
        return wallKickRight[rotation.getValue()];
    }

    protected Pair[] getKickPositionsLeft() {
        return wallKickLeft[rotation.getValue()]; // Not used except for O piece
    }

    protected static final Pair[][] wallKickRight = new Pair[][] {
            // O -> R
            { new Pair(0, 0), new Pair(-1, 0), new Pair(-1, +1), new Pair(0, -2), new Pair(-1, -2) },
            // R-> Z
            { new Pair(0, 0), new Pair(1, 0), new Pair(1, -1), new Pair(0, 2), new Pair(1, 2) },
            // Z -> L
            { new Pair(0, 0), new Pair(1, 0), new Pair(1, 1), new Pair(0, -2), new Pair(1, -2) },
            // L -> O
            { new Pair(0, 0), new Pair(-1, 0), new Pair(-1, -1), new Pair(0, 2), new Pair(-1, 2) },
    };

    protected static final Pair[][] wallKickLeft = new Pair[][] {
            // R -> O
            { new Pair(0, 0), new Pair(1, 0), new Pair(1, -1), new Pair(0, 2), new Pair(1, 2) },
            // Z -> R
            { new Pair(0, 0), new Pair(-1, 0), new Pair(-1, 1), new Pair(0, -2), new Pair(-1, -2) },
            // L -> Z
            { new Pair(0, 0), new Pair(-1, 0), new Pair(-1, -1), new Pair(0, 2), new Pair(-1, 2) },
            // O -> L
            { new Pair(0, 0), new Pair(1, 0), new Pair(1, 1), new Pair(0, -2), new Pair(1, 2) }
    };

}
