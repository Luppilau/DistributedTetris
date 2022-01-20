package com.Server;

import com.Tetris.Model.Generators.RandomTetriminoGenerator;
import com.Tetris.Model.Tetriminos.Tetrimino;
import com.Tetris.Net.Messages;

import org.jspace.*;

import java.util.Random;

//This runnable class oversees a game
public class GameThread implements Runnable {
    private int gameID;
    private int player1;
    private int player2;
    private Space channel; // The space which is shared between the thread and the two clients.

    // A thread that can take piece-requests from clients and return
    // tetraminoPackages
    // the PieceGenerator class is in this file
    Thread pieceGenerator;

    public GameThread(Space channel, int gameID, int p1, int p2) {
        this.channel = channel;
        player1 = p1;
        player2 = p2;
        TetriminoGenerator lol = new TetriminoGenerator(player1, player2, channel);
        pieceGenerator = new Thread(lol);
    }

    @Override
    public void run() {
        try {
            // Get the ok from protocol, ensuring that each player is connected to the
            // private space
            channel.get(Messages.okTemplate.getFields());
            channel.get(Messages.okTemplate.getFields());
            System.out.println("Got oks! Starting the game!");
            // Start giving out pieces
            pieceGenerator.start();

            // Wait for both players to submit their scores and find out who won
            Object[] score1 = channel.get(Messages.GameEndTemplate.getFields());
            Object[] score2 = channel.get(Messages.GameEndTemplate.getFields());
            String g = "Game " + gameID + " is over, ";
            if ((int) score1[2] > (int) score2[2]) {
                System.out.println(g + "player " + (int) score1[1] + " wins!");
            } else if ((int) score1[2] < (int) score2[2]) {
                System.out.println(g + "player " + (int) score2[1] + " wins!");
            } else {
                System.out.println(g + "both players win! it's a tie!");
            }
            // Stop the piece generator and end the thread.
            pieceGenerator.interrupt();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}

// A runnable class that generates tetramino for both players when they submit
// requests
// An important property is that each players given tetraminoes must be the same
// which is why two RandomTetraminoGenerators exist
class TetriminoGenerator implements Runnable {
    private Space channel;

    private int p1;
    private int p2;

    private RandomTetriminoGenerator pieceGen1;
    private RandomTetriminoGenerator pieceGen2;

    public TetriminoGenerator(int p1, int p2, Space channel) {
        this.p1 = p1;
        this.p2 = p2;
        this.channel = channel;
        long seed = (new Random()).nextLong();
        pieceGen1 = new RandomTetriminoGenerator(seed);
        pieceGen2 = new RandomTetriminoGenerator(seed);
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Get the next pieceRequest and generate some tetraminos, then put them in a
                // tuple and send them.
                Object[] request = channel.get(Messages.pieceRequest().getFields());
                int ID = (int) request[1];
                int amount = (int) request[2];

                Tetrimino[] minos;
                if (ID == p1) {
                    minos = pieceGen1.nextPieces(amount);
                } else if (ID == p2) {
                    minos = pieceGen2.nextPieces(amount);
                } else {
                    minos = null;
                }

                Tuple message = Messages.tetriminoPackage(ID, minos);
                channel.put(message.getTuple());

            } catch (InterruptedException e) {

            }
        }
    }
}