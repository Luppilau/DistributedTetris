package com.Tetris.Model;

import java.util.Random;
import com.Tetris.Model.Tetriminoes.*;

public class RandomPieceGenerator implements PieceGenerator {
    Random r = new Random();

    public RandomPieceGenerator() {

    }

    public FallingPiece nextPiece() {
        int next = r.nextInt(7);
        switch (next) {
            case 0:
                return new I();
            case 1:
                return new J();
            case 2:
                return new L();
            case 3:
                return new O();
            case 4:
                return new S();
            case 5:
                return new T();
            case 6:
                return new Z();
        }
        return new I();
    }
}