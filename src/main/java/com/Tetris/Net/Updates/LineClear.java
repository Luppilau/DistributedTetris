package com.Tetris.Net.Updates;

public class LineClear extends UpdateData {
    public int[] linesCleared;

    public LineClear(int[] linesCleared) {
        this.linesCleared = linesCleared;
    }
}
