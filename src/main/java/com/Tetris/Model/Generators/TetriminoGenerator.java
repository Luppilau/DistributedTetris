package com.Tetris.Model.Generators;

import com.Tetris.Model.Tetriminos.FallingTetrimino;

/*
Generalized Tetramino generator
Exists for ease of use if we want to implement 7-bag or other generators
*/
public interface TetriminoGenerator {
    public FallingTetrimino nextPiece();
}