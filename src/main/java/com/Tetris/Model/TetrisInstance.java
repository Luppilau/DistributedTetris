package com.Tetris.Model;

import com.Tetris.TetrisCanvas.TetrisCanvas;

import javafx.animation.AnimationTimer;

public class TetrisInstance extends AnimationTimer {
    private TetrisCanvas canvas;
    private TetrisModel game;
    private int frames = 0;

    public TetrisInstance(TetrisCanvas canvas, TetrisModel model) {
        this.game = model;
        this.canvas = canvas;
    }

    @Override
    public void handle(long now) {
        if (frames < 30) {
            frames++;
            return;
        }
        game.tick();
        canvas.render(game);
        frames = 0;
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

}