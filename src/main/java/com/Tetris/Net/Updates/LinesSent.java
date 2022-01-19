package com.Tetris.Net.Updates;

public class LinesSent extends UpdateData {
    int amount;
    int hole;

    public LinesSent(int amount, int hole) {
        this.amount = amount;
        this.hole = hole;
    }
}
