package com.Tetris.Net.Updates;

import com.Tetris.Model.Tetrimino;

public class NextPiece extends UpdateData {
    public Tetrimino nextPiece;

    public NextPiece(Tetrimino nextPiece) {
        this.nextPiece = nextPiece;
    }
}
