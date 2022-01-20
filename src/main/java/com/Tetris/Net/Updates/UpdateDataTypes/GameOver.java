package com.Tetris.Net.Updates.UpdateDataTypes;

import com.Tetris.Net.Updates.UpdateData;

public class GameOver extends UpdateData {
    public boolean value;

    public GameOver(boolean value) {
        this.value = value;
    }
}
