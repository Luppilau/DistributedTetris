package com.Tetris.Model.Generators;

import java.util.Random;
import com.Tetris.Model.Tetriminos.*;
import com.Tetris.Model.Tetriminos.FallingPieces.*;

public class RandomTetriminoGenerator implements TetriminoGenerator {
    Random r;

    public RandomTetriminoGenerator() {
        r = new Random();
    }

    public RandomTetriminoGenerator(long seed) {
        r = new Random(seed);
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

    public Tetrimino[] nextPieces(int amount) {
        Tetrimino[] out = new Tetrimino[amount];
        for (int i = 0; i < amount; i++) {
            int next = r.nextInt(7);
            Tetrimino nextTetrimino;
            switch (next) {
                case 0:
                    nextTetrimino = Tetrimino.I;
                    break;
                case 1:
                    nextTetrimino = Tetrimino.J;
                    break;
                case 2:
                    nextTetrimino = Tetrimino.L;
                    break;
                case 3:
                    nextTetrimino = Tetrimino.O;
                    break;
                case 4:
                    nextTetrimino = Tetrimino.S;
                    break;
                case 5:
                    nextTetrimino = Tetrimino.T;
                    break;
                case 6:
                    nextTetrimino = Tetrimino.Z;
                    break;
                default:
                    nextTetrimino = Tetrimino.TRASH;
                    break;
            }
            out[i] = nextTetrimino;
        }
        return out;
    }
}