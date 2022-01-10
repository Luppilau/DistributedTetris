package com.Tetris.Model;

import com.Tetris.View.TetrisCanvas;

import javafx.animation.AnimationTimer;

public class TetrisInstance extends AnimationTimer {
    private TetrisCanvas canvas;
    private TetrisModel game;
    private State state;
    private int frames = 0;

    private static final int clearAnimationFrames = 45;

    public TetrisInstance(TetrisCanvas canvas, TetrisModel model) {
        this.game = model;
        this.canvas = canvas;
        this.state = State.Tick;
    }

    @Override
    public void handle(long now) {
        if (state == State.Tick) {
            if (frames > 0) {
                frames--;
                return;
            }
            game.tick();
            if (game.hasEnded) {
                frames = 100;
                state = State.EndAnimation;
                return;
            } else if (!game.linesCleared.isEmpty()) {
                frames = clearAnimationFrames;
                state = State.ClearAnimation;
                return;
            }
            canvas.render(game);
            updateFramesToTick();
        } else if (state == State.EndAnimation) {
            if (frames > 0) {
                progressEndAnimation();
            } else {
                state = State.HasEnded;
            }
        } else if (state == State.ClearAnimation) {
            if (frames > 0) {
                progressClearAnimation();
            } else {
                game.linesCleared.clear();
                state = State.Tick;
            }

        } else if (state == State.HasEnded) {

        }
    }

    public void rotateRight() {
        if (state == State.Tick) {
            game.rotateRight();
            canvas.render(game);
        }
    }

    public void rotateLeft() {
        if (state == State.Tick) {
            game.rotateLeft();
            canvas.render(game);
        }
    }

    public void moveLeft() {
        if (state == State.Tick) {
            game.moveLeft();
            canvas.render(game);
        }
    }

    public void moveRight() {
        if (state == State.Tick) {
            game.moveRight();
            canvas.render(game);
        }
    }

    public void moveDown() {
        if (state == State.Tick) {
            game.moveDown();
            canvas.render(game);
        }
    }

    public void dropDown() {
        if (state == State.Tick) {
            game.dropDown();
            canvas.render(game);
            frames = 0;
        }
    }

    public void swap() {
        if (state == State.Tick) {
            game.swap();
            canvas.render(game);
        }
    }

    private void progressClearAnimation() {
        final int delayBefore = 10;
        final int delayAfter = 10;

        if (frames > clearAnimationFrames - delayBefore) {
            frames--;
        } else if (frames < delayAfter) {
            frames--;
        } else if ((frames - delayAfter) % 5 == 0) {
            int currentFrame = (frames - delayAfter);
            for (int clearedLine : game.linesCleared) {
                canvas.clearSquare(clearedLine, currentFrame / 5);
            }
            frames--;
        } else {
            frames--;
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
        if (game.level.getValue() >= 29) {
            frames = 1;
        } else {
            frames = times[game.level.getValue()];
        }
    }

}

enum State {
    Tick,
    ClearAnimation,
    EndAnimation,
    HasEnded
}