package com.Tetris.Model.Tetriminos;

import javafx.scene.paint.Color;

public enum Tetrimino {
    I, // Bar
    O, // Square
    T, // T-bar
    S, // Right Snake
    Z, // Left Snake
    J, // Left gun
    L, // Right gun
    TRASH;

    public static Color getColor(Tetrimino piece) {
        switch (piece) {
            case I:
                return Color.valueOf("#32C7EF");
            case O:
                return Color.valueOf("#F8D30A");
            case T:
                return Color.valueOf("#AE4D9B");
            case S:
                return Color.valueOf("#42B641");
            case Z:
                return Color.valueOf("#EF2129");
            case J:
                return Color.valueOf("#5A65AD");
            case L:
                return Color.valueOf("#EF7821");
            case TRASH:
                return Color.valueOf("#878b91");
            default:
                return Color.valueOf("#FFFFFF");
        }
    }

    public static Color getGreyColor(Tetrimino piece) {
        switch (piece) {
            case I:
                return Color.valueOf("#a2a2a2");
            case O:
                return Color.valueOf("#9c9c9c");
            case T:
                return Color.valueOf("#aeaeae");
            case S:
                return Color.valueOf("#424242");
            case Z:
                return Color.valueOf("#686868");
            case J:
                return Color.valueOf("#797979");
            case L:
                return Color.valueOf("#828282");
            case TRASH:
                return Color.valueOf("#878b91");
            default:
                return Color.valueOf("#FFFFFF");
        }
    }
}
