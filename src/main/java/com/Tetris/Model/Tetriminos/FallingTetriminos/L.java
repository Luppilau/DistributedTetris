package com.Tetris.Model.Tetriminos.FallingTetriminos;

import com.Tetris.Model.Tetriminos.FallingTetrimino;
import com.Tetris.Model.Tetriminos.Pair;
import com.Tetris.Model.Tetriminos.Tetrimino;

public class L extends FallingTetrimino {
    private static final Pair[][] blocks = new Pair[][] {
            { new Pair(0, 0), new Pair(-1, 0), new Pair(1, 0), new Pair(1, 1) },
            { new Pair(0, 0), new Pair(0, 1), new Pair(0, -1), new Pair(1, -1) },
            { new Pair(0, 0), new Pair(-1, 0), new Pair(1, 0), new Pair(-1, -1) },
            { new Pair(0, 0), new Pair(0, 1), new Pair(0, -1), new Pair(-1, 1) },
    };

    public L() {
        super();
    }

    @Override
    public Pair[] getBlocks() {
        return blocks[rotation.getValue()];
    }

    @Override
    public Tetrimino getType() {
        return Tetrimino.L;
    }
}
