package com.Tetris.Net.Updates.UpdateDataTypes;

import com.Tetris.Model.Tetriminos.Tetrimino;
import com.Tetris.Net.Updates.UpdateData;

public class Swap extends UpdateData {
    public Tetrimino swap;

    public Swap(Tetrimino swap) {
        this.swap = swap;
    }
}
