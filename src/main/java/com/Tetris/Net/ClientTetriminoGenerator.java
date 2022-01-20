package com.Tetris.Net;

import com.Tetris.Model.Tetriminos.FallingTetrimino;
import com.Tetris.Model.Generators.TetriminoGenerator;
import com.Tetris.Model.Tetriminos.Tetrimino;
import org.jspace.ActualField;
import org.jspace.RemoteSpace;
import org.jspace.SequentialSpace;
import org.jspace.Space;

/*
This class functions as a buffer betwwen the net-space and the internal game model
Here is maintained a buffer of two BUFFER_SIZE sized arrays of tetraminos.

When a tetramino is needed, we first see if there are any in the currentPack, if not 
then we send a request for more and begin taking form nextPack. 
when a request is sent in the internal space, the run() method catches the request and 
sends a request to the server generator for a new pack. When this pack arrives nextPack is replaced with this new package.


*/
public class ClientTetriminoGenerator implements Runnable, TetriminoGenerator {
    private static final int BUFFER_SIZE = 7;

    private RemoteSpace netSpace;
    private Space internalSpace;

    private int playerID;

    private Tetrimino[] currentPack;
    private Tetrimino[] nextPack;
    private int currentTetrimino;

    public ClientTetriminoGenerator(RemoteSpace netSpace, int playerID) {
        this.netSpace = netSpace;
        this.playerID = playerID;
        internalSpace = new SequentialSpace();

        try {
            // Fill up the buffer before the game starts
            netSpace.put(Messages.pieceRequest(playerID, BUFFER_SIZE));
            netSpace.put(Messages.pieceRequest(playerID, BUFFER_SIZE));
            Tetrimino[] minos1 = (Tetrimino[]) netSpace.get(Messages.tetriminoPackage(playerID).getFields())[2];
            Tetrimino[] minos2 = (Tetrimino[]) netSpace.get(Messages.tetriminoPackage(playerID).getFields())[2];
            currentPack = minos1;
            nextPack = minos2;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    // Runs in parallel with the actual game
    @Override
    public void run() {
        while (true) {
            try {
                // Get an internal request
                internalSpace.get(new ActualField("piece request"));
                // pull out any old "piece updated" message
                internalSpace.getp(new ActualField("piece updated"));
                // put a request
                netSpace.put(Messages.pieceRequest(playerID, BUFFER_SIZE));
                // Update with new tetraminos
                Tetrimino[] minos = (Tetrimino[]) netSpace.get(Messages.tetriminoPackage(playerID).getFields())[2];
                nextPack = minos;
                internalSpace.put("piece updated");
            } catch (InterruptedException e) {

            }

        }
    }

    // Called by the game
    @Override
    public FallingTetrimino nextPiece() {
        if (currentTetrimino == BUFFER_SIZE) {
            // If none remain in currentPack try to take the next
            try {
                if (nextPack == null) {
                    // If none are in the nextPack wait for the internalSpace to update nextPack
                    internalSpace.get(new ActualField("piece updated"));
                }
                // Replace currentpack and send an internal piece request
                currentPack = nextPack;
                nextPack = null; // Crash if too slow
                currentTetrimino = 0;
                internalSpace.put("piece request");
            } catch (InterruptedException e) {

            }
        }
        Tetrimino out = currentPack[currentTetrimino];
        currentTetrimino++;
        return FallingTetrimino.newFallingPiece(out);
    }
}
