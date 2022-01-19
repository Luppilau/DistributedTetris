package com.Tetris.Model;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;

import com.Server.ServerMessages;
import com.Tetris.Net.ClientPieceGenerator;
import com.Tetris.Net.UpdateKind;
import com.Tetris.Net.Updates.LineClear;
import com.Tetris.Net.Updates.PiecePlaced;

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
    public SimpleIntegerProperty points;

    public boolean hasEnded = false;
    public ArrayList<Integer> linesCleared = new ArrayList<>(4);

    private FallingPiece swap;
    private FallingPiece nextPiece;
    private boolean hasSwapped = false;
    private Canvas canvas;

    private ClientPieceGenerator generator;
    private Thread generatorThread;

    public TetrisModel(Canvas canvas, RemoteSpace space, Space junkQueue, int playerID) {
        generator = new ClientPieceGenerator(space, playerID);
        netSpace = space;
        this.playerID = playerID;

        this.junkQueue = junkQueue;

        generatorThread = new Thread(generator);
        generatorThread.start();

        current = generator.nextPiece();
        nextPiece = generator.nextPiece();
        level = new SimpleIntegerProperty(0);
        points = new SimpleIntegerProperty(0);
        lines = new SimpleIntegerProperty(0);
        this.canvas = canvas;
    }

    public TetrisModel(Canvas canvas) {
        level = new SimpleIntegerProperty(0);
        points = new SimpleIntegerProperty(0);
        lines = new SimpleIntegerProperty(0);
        this.canvas = canvas;
    }

    public void tick() {
        // Fall piece

        current.move(0, -1);
        if (!current.colliding(matrix)) {
            return;
        }
        current.move(0, 1);
        int[] relevantLines = current.relevantLines();
        lockPiece();
        hasSwapped = false;

        // Detect line clear
        int nLines = 0;
        lines: for (int y : relevantLines) {
            for (int x = 0; x < 10; x++) {
                if (matrix[x][y] == null) {

                    continue lines;
                }
            }
            linesCleared.add(y);
            moveMatrixDown(y, 1);
            nLines += 1;
        }

        if (nLines > 0) {
            sendLinesClearedUpdate(linesCleared.stream().mapToInt(i -> i).toArray());
        }

        // Receive all junk-updates

        try {
            Object s = junkQueue.getp(new FormalField(Integer.class));
            while (s != null) {
                Integer nextJunkAmt = (Integer) ((Object[]) junkQueue.getp(new FormalField(Integer.class)))[0];
                for (int i = 0; i < linesCleared.size(); i++) {
                    linesCleared.set(i, linesCleared.get(i) + nextJunkAmt);
                }

                moveMatrixUp(nextJunkAmt);
                s = junkQueue.getp(new FormalField(Integer.class))[0];
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        lines.set(lines.get() + nLines);
        points.set(points.get() + calculateScore(nLines));
        if (level.getValue() >= 15 && lines.getValue() % 10 == 0) {
            level.set(level.get() + 1);
        } else if (lines.getValue() >= level.getValue() * 10 + 10
                || lines.getValue() >= max(100, 10 * level.getValue() - 50)) {
            level.set(level.get() + 1);
        }

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

            }
        }
        current = nextPiece;
        nextPiece = generator.nextPiece();
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

    public void moveMatrixUp(int amount) {
        for (int i = 9; i >= 0; i--) {
            for (int j = 39; j >= 0; j--) {
                if (j - amount < 0) {
                    matrix[i][j] = Tetrimino.TRASH;
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
        try {
            netSpace.put(ServerMessages.gameEndMessage(playerID, points.get()));
            generatorThread.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendLinesClearedUpdate(int[] linesCleared) {
        try {
            netSpace.put(ServerMessages.update(playerID, UpdateKind.LineClear, new LineClear(linesCleared)));
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

}
