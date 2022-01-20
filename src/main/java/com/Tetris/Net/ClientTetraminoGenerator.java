package com.Tetris.Net;

import com.Tetris.Model.FallingPiece;
import com.Tetris.Model.TetraminoGenerator;
import com.Tetris.Model.Tetrimino;
import org.jspace.ActualField;
import org.jspace.RemoteSpace;
import org.jspace.SequentialSpace;
import org.jspace.Space;

public class ClientTetraminoGenerator implements Runnable, TetraminoGenerator {
    private static final int BUFFER_SIZE = 7;

    private RemoteSpace netSpace;
    private Space internalSpace;

    private int playerID;

    private Tetrimino[] currentPack;
    private Tetrimino[] nextPack;
    private int currentTetrimino;

    public ClientTetraminoGenerator(RemoteSpace netSpace, int playerID) {
        this.netSpace = netSpace;
        this.playerID = playerID;
        internalSpace = new SequentialSpace();

        try {
            netSpace.put(Message.pieceRequest(playerID, BUFFER_SIZE));
            netSpace.put(Message.pieceRequest(playerID, BUFFER_SIZE));
            Tetrimino[] minos1 = (Tetrimino[]) netSpace.get(Message.tetriminoPackage(playerID).getFields())[2];
            Tetrimino[] minos2 = (Tetrimino[]) netSpace.get(Message.tetriminoPackage(playerID).getFields())[2];
            currentPack = minos1;
            nextPack = minos2;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        while (true) {
            try {
                internalSpace.get(new ActualField("piece request"));
                internalSpace.getp(new ActualField("piece updated"));
                netSpace.put(Message.pieceRequest(playerID, BUFFER_SIZE));
                Tetrimino[] minos = (Tetrimino[]) netSpace.get(Message.tetriminoPackage(playerID).getFields())[2];
                nextPack = minos;
                internalSpace.put("piece updated");
            } catch (InterruptedException e) {

            }

        }
    }

    @Override
    public FallingPiece nextPiece() {
        if (currentTetrimino == BUFFER_SIZE) {
            try {
                if (nextPack == null) {
                    internalSpace.get(new ActualField("piece updated"));
                }
                currentPack = nextPack;
                nextPack = null; // Crash if too slow
                currentTetrimino = 0;
                internalSpace.put("piece request");
            } catch (InterruptedException e) {

            }
        }
        Tetrimino out = currentPack[currentTetrimino];
        currentTetrimino++;
        return FallingPiece.newFallingPiece(out);
    }
}
