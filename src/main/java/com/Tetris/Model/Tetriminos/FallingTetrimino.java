package com.Tetris.Model.Tetriminos;

import java.util.HashSet;
import java.util.Set;

import com.Tetris.Model.Tetriminos.FallingTetriminos.*;

/*
    Representation of a Tetrimino on a tetris board matrix. Handles rotation, position and type of current falling piece
*/

public class FallingTetrimino {
    public static final int DEFAULTX = 5;
    public static final int DEFAULTY = 21;
    public Rotation rotation = Rotation.O;
    public Pair pos;

    public FallingTetrimino() {
        pos = new Pair(DEFAULTX, DEFAULTY);
    }

    public static FallingTetrimino newFallingPiece(Tetrimino T) {
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
            default:
                return null;
        }
    }

    // Checks through all possible rotations according to the SRS-system, and
    // rotates/repositions to first available position
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
    }

    // Checks through all possible rotations according to the SRS-system, and
    // rotates/repositions to first available position
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
    }

    // Returns positions of blocks in context of a game board matrix
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

    // Returns all blocks in relation to (0,0)
    // Is overridden by all types of FallingTetriminos
    public Pair[] getBlocks() {
        return new Pair[0];
    }

    public void move(int x, int y) {
        pos.x += x;
        pos.y += y;
    }

    // Returns array of all lines this piece currently resides in, in sorted by
    // largest to smallest
    public int[] relevantLines() {
        Set<Integer> positions = new HashSet<>();

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

    // Checks whether this Falling Tetrimino collides with any blocks in a game
    // board matrix
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

    public void setLocation(Pair pos) {
        this.pos = pos;
    }

    public void setRotation(Rotation rot) {
        this.rotation = rot;
    }

    public Rotation getRotation() {
        return rotation;
    }

}
