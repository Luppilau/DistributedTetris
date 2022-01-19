package com.Tetris.Net.Updates;

import com.Tetris.Model.Tetrimino;

public class Swap extends UpdateData {
    public Tetrimino swap;

    public Swap(Tetrimino swap) {
        this.swap = swap;
    }
}
