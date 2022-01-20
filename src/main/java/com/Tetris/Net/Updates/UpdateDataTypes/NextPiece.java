package com.Tetris.Net.Updates.UpdateDataTypes;

import com.Tetris.Model.Tetriminos.Tetrimino;
import com.Tetris.Net.Updates.UpdateData;

public class NextPiece extends UpdateData {
    public Tetrimino nextPiece;

    public NextPiece(Tetrimino nextPiece) {
        this.nextPiece = nextPiece;
    }
}
