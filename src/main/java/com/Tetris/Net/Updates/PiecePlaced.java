package com.Tetris.Net.Updates;

import com.Tetris.Model.FallingPiece;
import com.Tetris.Model.Pair;
import com.Tetris.Model.Rotation;
import com.Tetris.Model.Tetrimino;

public class PiecePlaced extends UpdateData {
    public Tetrimino kind;
    public Rotation rot;
    public Pair location;

    public PiecePlaced(FallingPiece piece) {
        kind = piece.getType();
        rot = piece.getRotation();
        location = piece.pos;
    }
}