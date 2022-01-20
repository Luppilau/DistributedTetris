package com.Tetris.Model.Tetriminos;

//Rotatino representation taken from standard notation from tetris wiki
public enum Rotation {
    O, // 0
    R,
    Z, // 2
    L;

    public Rotation right() {
        switch (this) {
            case O:
                return R;
            case R:
                return Z;
            case Z:
                return L;
            case L:
                return O;
            default:
                return this; /// Doesnt happen!
        }
    }

    public Rotation left() {
        switch (this) {
            case O:
                return L;
            case R:
                return O;
            case Z:
                return R;
            case L:
                return Z;
            default:
                return this; /// Doesnt happen!
        }
    }

    public int getValue() {
        switch (this) {
            case O:
                return 0;
            case R:
                return 1;
            case Z:
                return 2;
            case L:
                return 3;
            default:
                return -1;
        }
    }
}