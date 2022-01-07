package com.Tetris.Model;

import com.Tetris.View.TetrisCanvas;

import javafx.animation.AnimationTimer;

public class TetrisInstance extends AnimationTimer {
    private TetrisCanvas canvas;
    private TetrisModel game;
    private State state;
    private int frames = 0;

    public TetrisInstance(TetrisCanvas canvas, TetrisModel model) {
        this.game = model;
        this.canvas = canvas;
        this.state = State.Tick;
    }

    @Override
    public void handle(long now) {
        switch (state) {
            case Tick:
                if (frames > 0) {
                    frames--;
                    return;
                }
                game.tick();
                canvas.render(game);
                updateFramesToTick();
            case HasEnded:

            case ClearAnimation:

            case EndAnimation:
        }

    }

    public void rotateRight() {
        game.rotateRight();
        canvas.render(game);
    }

    public void rotateLeft() {
        game.rotateLeft();
        canvas.render(game);
    }

    public void moveLeft() {
        game.moveLeft();
        canvas.render(game);
    }

    public void moveRight() {
        game.moveRight();
        canvas.render(game);
    }

    public void moveDown() {
        game.moveDown();
        canvas.render(game);
    }

    public void dropDown() {
        game.dropDown();
        canvas.render(game);
    }

    public void swap() {
        game.swap();
        canvas.render(game);
    }

    private void updateFramesToTick() {
        final int[] times = { 48, 43, 38, 33, 28, 23, 18, 13, 8, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2 };
        if (game.level >= 29) {
            frames = 1;
        } else {
            frames = times[game.level];
        }
    }

}

enum State {
    Tick,
    ClearAnimation,
    EndAnimation,
    HasEnded
}