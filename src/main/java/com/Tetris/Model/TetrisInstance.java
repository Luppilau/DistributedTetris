package com.Tetris.Model;

import com.Tetris.View.TetrisCanvas;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;

/*
In this file the main controller of the Tetris game, by extending AnimationTimer from javaFX
we ensure that the game is updated 60 times a second.

This class also handles events given by javaFX and projects them into the gameModel and triggers re-renders when necessary
The ending animation and line-clear animation also happens here.
 */
public class TetrisInstance extends AnimationTimer {
    private TetrisCanvas canvas;
    private TetrisModel game;
    private State state; //Current state, are we in an animation or should the game progress?
    private int frames = 0; //Counter that indicates how many frames should pass until the next update should occur

    // Delayed Auto Shift variables
    // When a button is held, we want the pieces to move quickly
    public KeyCode keyPressed = null;
    public int keyPressedFrames = 0;

    private static final int clearAnimationFrames = 45;

    public TetrisInstance(TetrisCanvas canvas, TetrisModel model) {
        this.game = model;
        this.canvas = canvas;
        this.state = State.Tick;
    }

    //This function is called 60 times a second and is responsible for doing what needs to be done in order to render the next frame.
    @Override
    public void handle(long now) {

        processDAS();

        //Simple state machine
        //Tick: Progress the game as normal
        if (state == State.Tick) {
            //Early return when nothing should happen
            if (frames > 0) {
                frames--;
                return;
            }
            //If it is time, let the game model know that is is time to "tick"
            game.tick();
            if (game.hasEnded) {
                //If the game ended on this tick, play the ending animation
                frames = 100;
                state = State.EndAnimation;
            } else if (!game.linesCleared.isEmpty()) {
                //If any lines were cleared, play the clearAnimation
                frames = clearAnimationFrames;
                state = State.ClearAnimation;
            } else {
                //Otherwise render the updated game state
                canvas.render(game);
                updateFramesToTick();
            }
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
            //Do nothing, the game is over.
        }
    }

    private void processDAS() {
        final int dasDelay = 12; //How many frames until DAS starts
        final int dasTime = 3; //Frames between moves

        //If no key is pressed, stop
        if (keyPressed == null) {
            return;
        }
        //Realize DAS
        if (keyPressedFrames == 0 || (keyPressedFrames >= dasDelay && (keyPressedFrames - dasDelay) % dasTime == 0)) {
            switch (keyPressed) {
                case RIGHT:
                    moveRight();
                    break;
                case LEFT:
                    moveLeft();
                    break;
                case DOWN:
                    moveDown();
                    break;
                default:
                    break;
            }
        }
        //Update the amount of frames with button press
        keyPressedFrames += (keyPressed != null) ? 1 : 0;
    }

    //Following 7 functions are self documenting.
    //As we only render in handle() when the game "ticks", we need to render when the player changes something.
    //Functions follow format: update model then render update
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

    //Progress the clear animation
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

    //Progress the ending animation,
    private void progressEndAnimation() {
        if (frames % 5 == 0) {
            canvas.drawGrey(frames / 5 - 1);
        }
        frames--;
    }

    //As the game gets faster as the levels progress, we need different frame timings, these are here
    private void updateFramesToTick() {
        //Until level 29 the timings are above 1 frame
        final int[] times = { 48, 43, 38, 33, 28, 23, 18, 13, 8, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2 };
        //after, there is a single frame between each "drop"
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