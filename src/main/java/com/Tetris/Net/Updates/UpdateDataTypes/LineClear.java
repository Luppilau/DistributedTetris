package com.Tetris.Net.Updates.UpdateDataTypes;

import com.Tetris.Net.Updates.UpdateData;

public class LineClear extends UpdateData {
    public int newScore;
    public int newLevel;
    public int[] linesCleared;

    public LineClear(int[] linesCleared, int newScore, int newLevel) {
        this.linesCleared = linesCleared;
        this.newScore = newScore;
        this.newLevel = newLevel;
    }
}
