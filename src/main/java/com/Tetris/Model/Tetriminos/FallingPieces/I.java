package com.Tetris.Model.Tetriminos.FallingPieces;

import com.Tetris.Model.Tetriminos.FallingPiece;
import com.Tetris.Model.Tetriminos.Pair;
import com.Tetris.Model.Tetriminos.Tetrimino;

public class I extends FallingPiece {
    private static final Pair[][] blocks = new Pair[][] {
            { new Pair(-1, 0), new Pair(0, 0), new Pair(1, 0), new Pair(2, 0) },
            { new Pair(1, 1), new Pair(1, 0), new Pair(1, -1), new Pair(1, -2) },
            { new Pair(-1, -1), new Pair(0, -1), new Pair(1, -1), new Pair(2, -1) },
            { new Pair(0, -2), new Pair(0, -1), new Pair(0, 0), new Pair(0, 1) },
    };

    @Override
    public Pair[] getBlocks() {
        return blocks[rotation.getValue()];
    }

    @Override
    public Tetrimino getType() {
        return Tetrimino.I;
    }

    @Override
    protected Pair[] getKickPosistionsRight() {
        return wallKickRight[rotation.getValue()];
    }

    @Override
    protected Pair[] getKickPositionsLeft() {
        return wallKickLeft[rotation.getValue()]; // Not used except for O piece
    }

    protected static final Pair[][] wallKickRight = new Pair[][] {
            // O->R
            { new Pair(0, 0), new Pair(-2, 0), new Pair(+1, 0), new Pair(-2, -1), new Pair(+1, +2) },
            // R->Z
            { new Pair(0, 0), new Pair(-1, 0), new Pair(+2, 0), new Pair(-1, +2), new Pair(+2, -1) },
            // Z->L
            { new Pair(0, 0), new Pair(+2, 0), new Pair(-1, 0), new Pair(+2, +1), new Pair(-1, -2) },
            // L->O
            { new Pair(0, 0), new Pair(+1, 0), new Pair(-2, 0), new Pair(+1, -2), new Pair(-2, +1) }
    };

    protected static final Pair[][] wallKickLeft = new Pair[][] {
            // R->O
            { new Pair(0, 0), new Pair(+2, 0), new Pair(-1, 0), new Pair(+2, +1), new Pair(-1, -2) },
            // Z->R
            { new Pair(0, 0), new Pair(+1, 0), new Pair(-2, 0), new Pair(+1, -2), new Pair(-2, +1) },
            // L->Z
            { new Pair(0, 0), new Pair(-2, 0), new Pair(+1, 0), new Pair(-2, -1), new Pair(+1, +2) },
            // O->L
            { new Pair(0, 0), new Pair(-1, 0), new Pair(+2, 0), new Pair(-1, +2), new Pair(+2, -1) }
    };

}
