package com.Tetris.Model.Tetriminos.FallingPieces;

import com.Tetris.Model.Tetriminos.FallingPiece;
import com.Tetris.Model.Tetriminos.Pair;
import com.Tetris.Model.Tetriminos.Tetrimino;

public class J extends FallingPiece {
    protected static final Pair[][] blocks = new Pair[][] {
            { new Pair(0, 0), new Pair(-1, 0), new Pair(1, 0), new Pair(-1, 1) },
            { new Pair(0, 0), new Pair(0, 1), new Pair(0, -1), new Pair(1, 1) },
            { new Pair(0, 0), new Pair(-1, 0), new Pair(1, 0), new Pair(1, -1) },
            { new Pair(0, 0), new Pair(0, 1), new Pair(0, -1), new Pair(-1, -1) },
    };

    @Override
    public Pair[] getBlocks() {
        return blocks[rotation.getValue()];
    }

    @Override
    public Tetrimino getType() {
        return Tetrimino.J;
    }
}
