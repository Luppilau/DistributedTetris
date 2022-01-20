package com.Tetris.Model;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.List;

import com.Server.ServerMessages;
import com.Tetris.Net.ClientTetraminoGenerator;
import com.Tetris.Net.UpdateKind;
import com.Tetris.Net.Updates.GameOver;
import com.Tetris.Net.Updates.LineClear;
import com.Tetris.Net.Updates.LinesSent;
import com.Tetris.Net.Updates.NextPiece;
import com.Tetris.Net.Updates.PiecePlaced;
import com.Tetris.Net.Updates.Swap;
import com.Tetris.Net.Updates.UpdateData;

import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import org.jspace.Space;

import javafx.scene.canvas.Canvas;

import static java.lang.Math.max;

public class TetrisModel {
    public Space junkQueue;
    private RemoteSpace netSpace;

    private int playerID;

    public Tetrimino[][] matrix = new Tetrimino[10][40];
    public FallingPiece current;

    public SimpleIntegerProperty level;
    public SimpleIntegerProperty lines;
    public SimpleIntegerProperty score;

    public boolean hasEnded = false;
    public ArrayList<Integer> linesCleared = new ArrayList<>(4);

    private FallingPiece swap;
    private FallingPiece nextPiece;
    private boolean hasSwapped = false;
    private Canvas canvas;

    private ClientTetraminoGenerator generator;
    private Thread generatorThread;

    public TetrisModel(Canvas canvas, RemoteSpace space, Space junkQueue, int playerID) {
        generator = new ClientTetraminoGenerator(space, playerID);
        netSpace = space;
        this.playerID = playerID;

        this.junkQueue = junkQueue;

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

    // Move the current falling piece one step down,
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
            Object s = junkQueue.getp(new FormalField(Integer.class), new FormalField(Integer.class));
            // Basic implementation of queryAllp as it isn't part of the jspace
            // specification
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

    private void lockPiece() {
        sendPieceUpdate(current);
        for (Pair sq : current.getSquares()) {
            assert (!(sq.x < 0 || sq.x > 9 || sq.y < 0 || sq.y > 39));
            matrix[sq.x][sq.y] = current.getType();
            // END GAME
            if (sq.y >= 21) {
                hasEnded = true;
                sendGameEndUpdate();
                return;
            }
        }
        current = nextPiece;
        nextPiece = generator.nextPiece();
        sendNextPieceUpdate(nextPiece.getType());
        canvas.fireEvent(new CustomEvent(CustomEvent.NextPieceEvent));

    }

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

    public void swap() {
        if (!hasSwapped) {
            FallingPiece temp;
            if (swap != null) {
                temp = swap;
                temp.pos = new Pair(FallingPiece.DEFAULTX, FallingPiece.DEFAULTY);
            } else {
                temp = nextPiece;
                nextPiece = generator.nextPiece();
            }
            current.rotation = Rotation.O;
            swap = current;
            current = temp;
            hasSwapped = true;
            sendNextPieceUpdate(nextPiece.getType());
            sendSwapUpdate(swap.getType());
        }
    }

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

    public FallingPiece getSwap() {
        return swap;
    }

    public FallingPiece getNextPiece() {
        return nextPiece;
    }

    private void sendGameEndUpdate() {
        // Notifies the server and the opponent that the game has ended
        try {
            netSpace.put(ServerMessages.update(playerID, UpdateKind.GameOver, new UpdateData()));
            netSpace.put(ServerMessages.gameEndMessage(playerID, score.get()));
            generatorThread.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendLinesClearedUpdate(int[] linesCleared, int score, int level) {
        try {
            netSpace.put(
                    ServerMessages.update(playerID, UpdateKind.LineClear, new LineClear(linesCleared, score, level)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendPieceUpdate(FallingPiece piece) {
        try {
            netSpace.put(ServerMessages.update(playerID, UpdateKind.PiecePlaced, new PiecePlaced(piece)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendSwapUpdate(Tetrimino type) {
        try {
            netSpace.put(ServerMessages.update(playerID, UpdateKind.Swap, new Swap(type)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendNextPieceUpdate(Tetrimino type) {
        try {
            netSpace.put(ServerMessages.update(playerID, UpdateKind.NextPiece, new NextPiece(type)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendJunkUpdate(int linesSent, int hole) {
        try {
            netSpace.put(ServerMessages.update(playerID, UpdateKind.LinesSent, new LinesSent(linesSent, hole)));
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
