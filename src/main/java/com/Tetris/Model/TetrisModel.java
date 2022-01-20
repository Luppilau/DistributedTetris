package com.Tetris.Model;

import com.Tetris.Controller.CustomEvent;
import com.Tetris.Model.Tetriminos.FallingTetrimino;
import com.Tetris.Model.Tetriminos.Pair;
import com.Tetris.Model.Tetriminos.Rotation;
import com.Tetris.Model.Tetriminos.Tetrimino;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.List;

import com.Tetris.Net.Messages;
import com.Tetris.Net.ClientTetriminoGenerator;
import com.Tetris.Net.Updates.UpdateKind;
import com.Tetris.Net.Updates.UpdateDataTypes.LineClear;
import com.Tetris.Net.Updates.UpdateDataTypes.LinesSent;
import com.Tetris.Net.Updates.UpdateDataTypes.NextPiece;
import com.Tetris.Net.Updates.UpdateDataTypes.PiecePlaced;
import com.Tetris.Net.Updates.UpdateDataTypes.Swap;
import com.Tetris.Net.Updates.UpdateData;

import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import org.jspace.Space;

import javafx.scene.canvas.Canvas;

import static java.lang.Math.max;

/*

This class contains all information about the current game of tetris.

Over time it has become a bit bloated and multi-functional (Super-class)
It both handles running the game and sending out updates to the opponent
it also sends events to the surrounding UI, which is why it imports the javafx Canvas. 

It needs a refactor, but we don't have the time.

*/
public class TetrisModel {
    public Space junkQueue; // Internal communication channel between this and UpdateHandler used to give
                            // junk at the right time
    private RemoteSpace netSpace; // The space residing in the Server process, used to send updates to the
                                  // opponent

    private int playerID; // The ID of the player that plays on this model

    public Tetrimino[][] matrix = new Tetrimino[10][40]; // The game board
    public FallingTetrimino current; // The current piece to fall

    // SimpleIntegerProperty updates the UI automatically
    public SimpleIntegerProperty level;
    public SimpleIntegerProperty lines;
    public SimpleIntegerProperty score;

    public boolean hasEnded = false;
    // A list of the last lines cleared
    public ArrayList<Integer> linesCleared = new ArrayList<>(4);

    private FallingTetrimino swap;
    private FallingTetrimino nextPiece;
    private boolean hasSwapped = false;

    // The canvas on which the game is rendered, used to send Events to TetrisCanvas
    private Canvas canvas;

    // TetriminoGenerator and the thread that runs it.
    private ClientTetriminoGenerator generator;
    private Thread generatorThread;

    public TetrisModel(Canvas canvas, RemoteSpace space, Space junkQueue, int playerID) {
        generator = new ClientTetriminoGenerator(space, playerID);
        netSpace = space;
        this.playerID = playerID;

        this.junkQueue = junkQueue;

        // Create a thread and start it, save the handle for stopping the thread later.
        generatorThread = new Thread(generator);
        generatorThread.start();

        current = generator.nextPiece();
        nextPiece = generator.nextPiece();
        sendNextPieceUpdate(nextPiece.getType());
        level = new SimpleIntegerProperty(0);
        score = new SimpleIntegerProperty(0);
        lines = new SimpleIntegerProperty(0);
        this.canvas = canvas;
    }

    public TetrisModel(Canvas canvas) {
        level = new SimpleIntegerProperty(0);
        score = new SimpleIntegerProperty(0);
        lines = new SimpleIntegerProperty(0);
        this.canvas = canvas;
    }

    // Progress the game by moving the current falling piece one step down,
    // If it collides with the current stack, lock it in and run line detection and
    // if applicable
    // score updates.
    public void tick() {

        current.move(0, -1);
        // If not colliding return early
        if (!current.colliding(matrix)) {
            return;
        }

        // Else move up to previous position where the piece didn't collide
        current.move(0, 1);

        // Then get the lines that need to be checked for line-clear
        // relevantLines() maintains a Big -> Small sorting (this is important!)
        int[] relevantLines = current.relevantLines();
        lockPiece();
        // Let the player swap in again
        hasSwapped = false;

        // Run line detection
        int nLines = 0;
        lines: for (int y : relevantLines) {
            // Detect line clear
            for (int x = 0; x < 10; x++) {
                if (matrix[x][y] == null) {
                    continue lines;
                }
            }
            // Realize line clear
            linesCleared.add(y);
            moveMatrixDown(y, 1);
            nLines += 1;
        }

        // Get junk updates and update model with it
        emptyJunkQueue();

        // update the number
        lines.set(lines.get() + nLines);
        // update the score
        score.set(score.get() + calculateScore(nLines));

        // Update the current level
        // The formula is taken from https://tetris.wiki/Tetris_(NES,_Nintendo)
        if (level.getValue() >= 15 && lines.getValue() % 10 == 0) {
            level.set(level.get() + 1);
        } else if (lines.getValue() >= level.getValue() * 10 + 10
                || lines.getValue() >= max(100, 10 * level.getValue() - 50)) {
            level.set(level.get() + 1);
        }

        // If lines were cleared, send an update to the opponent
        if (nLines > 0) {
            // ArrayList<Integer> to int[]
            sendLinesClearedUpdate(linesCleared.stream().mapToInt(i -> i).toArray(), score.get(),
                    level.get()); // C
        }
    }

    private void emptyJunkQueue() {
        // Receive all junk-updates
        try {
            // Basic implementation of queryAllp as it isn't part of the jspace
            // specification
            Object s = junkQueue.getp(new FormalField(Integer.class), new FormalField(Integer.class));
            while (s != null) {
                Integer nextJunkAmt = (Integer) ((Object[]) s)[0];
                Integer hole = (Integer) ((Object[]) s)[1];

                // To not break the lines-clear animation, we need to update the "linesCleared"
                // arraylist to account for the extra lines at the bottom
                for (int i = 0; i < linesCleared.size(); i++) {
                    linesCleared.set(i, linesCleared.get(i) + nextJunkAmt);
                }
                // Move the matrix up and send an update to the opponent to tell them that some
                // junk lines were added to the screen
                moveMatrixUp(nextJunkAmt, hole);
                sendJunkUpdate(nextJunkAmt, hole);
                s = junkQueue.getp(new FormalField(Integer.class), new FormalField(Integer.class));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Hacky solution to get the amount of incoming junk-lines for use in the UI
    public int getCountIncomingJunk() {
        int out = 0;
        try {
            List<Object[]> s = junkQueue.queryAll(new FormalField(Integer.class), new FormalField(Integer.class));
            // Basic implementation of queryAllp as it isn't
            for (Object[] junkUpdate : s) {
                out += (Integer) junkUpdate[0];
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return out;
    }

    // Write the current falling piece into the matrix, locking it in place.
    // If the piece locks in above the visible matrix (y >= 21), then end the game
    // and send updats to whom it may concern
    private void lockPiece() {
        sendPieceUpdate(current);
        for (Pair sq : current.getSquares()) {
            assert (!(sq.x < 0 || sq.x > 9 || sq.y < 0 || sq.y > 39));
            matrix[sq.x][sq.y] = current.getType();
            // END GAME
            if (sq.y >= 21) {
                hasEnded = true;
                sendGameEndUpdate();
                generatorThread.interrupt(); // Stop listening for new tetraminorequest
                return;
            }
        }
        current = nextPiece;
        nextPiece = generator.nextPiece();
        sendNextPieceUpdate(nextPiece.getType());
        canvas.fireEvent(new CustomEvent(CustomEvent.NextPieceEvent)); // Update UI

    }

    // Next 6 methods self documenting
    public void rotateRight() {
        current.rotateRight(matrix);
    }

    public void rotateLeft() {
        current.rotateLeft(matrix);
    }

    public void moveLeft() {
        current.move(-1, 0);
        if (current.colliding(matrix)) {
            current.move(1, 0);
        }
    }

    public void moveRight() {
        current.move(1, 0);
        if (current.colliding(matrix)) {
            current.move(-1, 0);
        }
    }

    public void moveDown() {
        current.move(0, -1);
        if (current.colliding(matrix)) {
            current.move(0, 1);
        }
    }

    public void dropDown() {
        while (!current.colliding(matrix)) {
            current.move(0, -1);
        }
        current.move(0, 1);
    }

    // Gets the position of the "ghost" tetramino
    public Pair getGhostPosition() {
        // Copy of old pos
        Pair ghost = new Pair(current.pos.x, current.pos.y);
        while (!current.colliding(matrix)) {
            current.move(0, -1);
        }
        current.move(0, 1);
        // Copy of ghost pos
        Pair ghostPos = new Pair(current.pos.x, current.pos.y);
        current.pos = ghost;
        return ghostPos;
    }

    // Swap swap-piece with current piece
    public void swap() {
        if (!hasSwapped) {
            FallingTetrimino temp;
            if (swap != null) {
                temp = swap;
                temp.pos = new Pair(FallingTetrimino.DEFAULTX, FallingTetrimino.DEFAULTY);
            } else {
                temp = nextPiece;
                nextPiece = generator.nextPiece();
            }
            current.setRotation(Rotation.O);
            swap = current;
            current = temp;
            hasSwapped = true;
            sendNextPieceUpdate(nextPiece.getType());
            sendSwapUpdate(swap.getType());
        }
    }

    // Move everything in the matrix down starting at y and moving amount squares
    protected void moveMatrixDown(int y, int amount) {

        for (int i = y; i < 39; i++) {
            for (int x = 0; x < 10; x++) {
                if ((i + amount) > 39) {
                    matrix[x][i] = null;
                } else {
                    matrix[x][i] = matrix[x][i + amount];
                }
            }
        }
    }

    // Move everything in the matrix up, putting junk where nothing was before.
    // Except in hole, there should be null so the client can clear the lines
    public void moveMatrixUp(int amount, int hole) {
        for (int i = 0; i < 10; i++) {
            for (int j = 39; j >= 0; j--) {
                if (j < amount) {
                    if (i == hole) {
                        matrix[i][j] = null;
                    } else {
                        matrix[i][j] = Tetrimino.TRASH;
                    }
                } else {
                    matrix[i][j] = matrix[i][j - amount];
                }
            }
        }
    }

    // Formula taken from tetris wiki see tick()
    private int calculateScore(int n) {
        assert (n >= 0 && n <= 4);
        int p = 0;
        switch (n) {
            case 1:
                p = 40;
                break;
            case 2:
                p = 100;
                break;
            case 3:
                p = 300;
                break;
            case 4:
                p = 1200;
                break;
        }
        return p * (level.getValue() + 1);
    }

    public FallingTetrimino getSwap() {
        return swap;
    }

    public FallingTetrimino getNextPiece() {
        return nextPiece;
    }

    // Remaining methods send updates to the opponent with the right formatting as
    // dictated by the Message class
    private void sendGameEndUpdate() {
        // Notifies the server and the opponent that the game has ended
        try {
            netSpace.put(Messages.update(playerID, UpdateKind.GameOver, new UpdateData()));
            netSpace.put(Messages.gameEndMessage(playerID, score.get()));
            ;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendLinesClearedUpdate(int[] linesCleared, int score, int level) {
        try {
            netSpace.put(
                    Messages.update(playerID, UpdateKind.LineClear, new LineClear(linesCleared, score, level)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendPieceUpdate(FallingTetrimino piece) {
        try {
            netSpace.put(Messages.update(playerID, UpdateKind.PiecePlaced, new PiecePlaced(piece)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendSwapUpdate(Tetrimino type) {
        try {
            netSpace.put(Messages.update(playerID, UpdateKind.Swap, new Swap(type)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendNextPieceUpdate(Tetrimino type) {
        try {
            netSpace.put(Messages.update(playerID, UpdateKind.NextPiece, new NextPiece(type)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendJunkUpdate(int linesSent, int hole) {
        try {
            netSpace.put(Messages.update(playerID, UpdateKind.LinesSent, new LinesSent(linesSent, hole)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void incrementLines(int lines) {
        this.lines.set(this.lines.get() + lines);
    }

    public void setScore(int score) {
        this.score.set(score);
    }

    public void setLevel(int level) {
        this.level.set(level);
    }
}
