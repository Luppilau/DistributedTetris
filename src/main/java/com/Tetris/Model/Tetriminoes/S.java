package com.Tetris.Model.Tetriminoes;

import com.Tetris.Model.FallingPiece;
import com.Tetris.Model.Pair;
import com.Tetris.Model.Tetrimino;

public class S extends FallingPiece {
    private static final Pair[][] blocks = new Pair[][] {
            { new Pair(-1, 0), new Pair(0, 0), new Pair(0, 1), new Pair(1, 1) },
            { new Pair(0, 1), new Pair(0, 0), new Pair(1, 0), new Pair(1, -1) },
            { new Pair(-1, -1), new Pair(0, 0), new Pair(0, -1), new Pair(1, 0) },
            { new Pair(-1, 1), new Pair(0, 0), new Pair(-1, 0), new Pair(0, -1) },
    };

    @Override
    public Pair[] getBlocks() {
        return blocks[rotation.getValue()];
    }

    @Override
    public Tetrimino getType() {
        return Tetrimino.S;
    }
}
