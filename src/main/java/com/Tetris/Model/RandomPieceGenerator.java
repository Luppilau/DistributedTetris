package com.Tetris.Model;

import java.util.Random;
import com.Tetris.Model.Tetriminoes.*;

public class RandomPieceGenerator implements PieceGenerator {
    Random r = new Random();

    public RandomPieceGenerator() {

    }

    public FallingPiece nextPiece() {
        return new I();
    }
}