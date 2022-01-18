package com.Tetris.Net;

import com.Tetris.Model.FallingPiece;
import com.Tetris.Model.PieceGenerator;
import com.Tetris.Model.Tetrimino;
import org.jspace.ActualField;
import org.jspace.RemoteSpace;
import org.jspace.SequentialSpace;
import org.jspace.Space;

public class ClientPieceGenerator implements Runnable, PieceGenerator {
    private static final int BUFFER_SIZE = 7;

    private RemoteSpace netSpace;
    private Space internalSpace;

    private int playerID;

    private Tetrimino[] currentPack;
    private Tetrimino[] nextPack;
    private int currentTetrimino;

    public ClientPieceGenerator(RemoteSpace netSpace, int playerID) {
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
                netSpace.put(Message.pieceRequest(playerID, BUFFER_SIZE));
                Tetrimino[] minos = (Tetrimino[]) netSpace.get(Message.tetriminoPackage(playerID).getFields())[2];
                currentPack = nextPack;
                nextPack = minos;
                currentTetrimino = 0;
                // System.out.println(Arrays.toString(currentPack));
                // System.out.println(Arrays.toString(nextPack)+"\n");
                internalSpace.put("piece updated");
            } catch (InterruptedException e) {

            }

        }
    }

    @Override
    public FallingPiece nextPiece() {
        if (currentTetrimino == BUFFER_SIZE) {
            try {
                System.out.println("Taking piece");
                internalSpace.put("piece request");
                internalSpace.get(new ActualField("piece updated"));
                System.out.println("Got piece");
            } catch (InterruptedException e) {

            }
        }
        Tetrimino out = currentPack[currentTetrimino];
        currentTetrimino++;
        return FallingPiece.newFallingPiece(out);
    }
}
