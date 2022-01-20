package com.Tetris.Net.Updates.UpdateDataTypes;

import com.Tetris.Model.Tetriminos.FallingPiece;
import com.Tetris.Model.Tetriminos.Pair;
import com.Tetris.Model.Tetriminos.Rotation;
import com.Tetris.Model.Tetriminos.Tetrimino;
import com.Tetris.Net.Updates.UpdateData;

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