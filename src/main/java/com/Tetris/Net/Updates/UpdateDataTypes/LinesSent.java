package com.Tetris.Net.Updates.UpdateDataTypes;

import com.Tetris.Net.Updates.UpdateData;

public class LinesSent extends UpdateData {
    public int amount;
    public int hole;

    public LinesSent(int amount, int hole) {
        this.amount = amount;
        this.hole = hole;
    }
}
