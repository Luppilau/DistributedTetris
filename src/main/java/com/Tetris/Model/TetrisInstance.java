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
                if (game.hasEnded) {
                    System.out.println("ending game");
                    frames = 100;
                    state = State.EndAnimation;
                    return;
                }
                canvas.render(game);
                updateFramesToTick();
                break;
            case HasEnded:
                break;
            case ClearAnimation:

                break;
            case EndAnimation:
                if (frames > 0) {
                    progressEndAnimation();
                } else {
                    state = State.HasEnded;
                }
                break;
        }

    }

    public void rotateRight() {
        if (!game.hasEnded) {
            game.rotateRight();
            canvas.render(game);
        }
    }

    public void rotateLeft() {
        if (!game.hasEnded) {
            game.rotateLeft();
            canvas.render(game);
        }
    }

    public void moveLeft() {
        if (!game.hasEnded) {
            game.moveLeft();
            canvas.render(game);
        }
    }

    public void moveRight() {
        if (!game.hasEnded) {
            game.moveRight();
            canvas.render(game);
        }
    }

    public void moveDown() {
        if (!game.hasEnded) {
            game.moveDown();
            canvas.render(game);
        }
    }

    public void dropDown() {
        if (!game.hasEnded) {
            game.dropDown();
            canvas.render(game);
        }
    }

    public void swap() {
        if (!game.hasEnded) {
            game.swap();
            canvas.render(game);
        }
    }

    private void progressEndAnimation() {
        if (frames % 5 == 0) {
            canvas.drawGrey(frames / 5 - 1);
        }
        frames--;
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