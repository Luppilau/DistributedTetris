package com.Server;

import com.Tetris.Model.RandomPieceGenerator;
import com.Tetris.Model.Tetrimino;
import com.Tetris.Net.GameEndMessage;
import com.Tetris.Net.Message;

import org.jspace.ActualField;
import org.jspace.Space;
import org.jspace.Template;
import org.jspace.Tuple;

public class GameThread implements Runnable {
    private int LobbyID;
    private int Player1;
    private int Player2;
    private Space channel;

    Thread generator;

    public GameThread(Space channel, int LobbyID) {
        this.channel = channel;
        this.LobbyID = LobbyID;
    }

    @Override
    public void run() {
        Template GameEndTemplate = GameEndMessage.GameEndTemplate;

        try {
            // Get the ok from protocol, ensuring that each player is connected
            channel.get(ServerMessages.okTemplate.getFields());
            channel.get(ServerMessages.okTemplate.getFields());

            generator = new Thread(new PieceGenerator(Player1, Player2, channel));
            generator.start();

            System.out.println("Waiting for scores");
            Object[] score1 = channel.get(GameEndTemplate.getFields());
            Object[] score2 = channel.get(GameEndTemplate.getFields());
            if ((int) score2[1] == Player1) {
                Object[] swap = score1;
                score1 = score2;
                score2 = swap;
            }

            if ((int) score1[2] > (int) score2[2]) {
                System.out.println("Player 1 wins!");
            } else if ((int) score1[2] < (int) score2[2]) {
                System.out.println("Player 2 wins!");
            } else {
                System.out.println("Tie!");
            }
            generator.interrupt();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public class PieceGenerator implements Runnable {
        private Space channel;

        private int p1;
        private int p2;

        private RandomPieceGenerator pieceGen1;
        private RandomPieceGenerator pieceGen2;

        public PieceGenerator(int p1, int p2, Space channel) {
            this.p1 = p1;
            this.p2 = p2;
            this.channel = channel;
            long seed = (long) p1 ^ ((long) p2 ^ 37) * 12319874; // TODO Implement random seed!
            pieceGen1 = new RandomPieceGenerator(seed);
            pieceGen2 = new RandomPieceGenerator(seed);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    System.out.println("Waiting for tetrimino package request");
                    Object[] request = channel.get(Message.tetriminoPackage().getFields());
                    int ID = (int) request[1];
                    int amount = (int) request[2];

                    Tetrimino[] minos;
                    if (ID == p1) {
                        minos = pieceGen1.nextPieces(amount);
                    } else {
                        minos = pieceGen2.nextPieces(amount);
                    }
                    Tuple message = Message.tetriminoPackage(ID, minos);

                    channel.put(message);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
