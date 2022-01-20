package com.Tetris.Model.Tetriminos;

public class Pair {
    public int x;
    public int y;

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Pair add(Pair one, Pair two) {
        return new Pair(one.x + two.x, one.y + two.y);
    }
}