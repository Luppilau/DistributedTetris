package com.Server;

import com.Tetris.Net.ClientTetriminoGenerator;
import org.jspace.RemoteSpace;
import com.Tetris.Net.Messages;

import java.io.IOException;

//Imitates two clinets connecting and playing the game.
//Outputs the amount of time taking 2x 10_000 tetraminos take.
//Only used for debugging the server.
public class ClientMock {
    // The default testing-ip address used throughout the project
    public static final String URI = "tcp://localhost:9090/";

    public static void main(String[] args) throws IOException, InterruptedException {

        RemoteSpace server = new RemoteSpace(URI + "lobby" + "?keep");

        server.put("game request");
        server.put("game request");
        System.out.println("put the requests");

        Object[] p1 = server.get(Messages.sessionDetails.getFields());
        Object[] p2 = server.get(Messages.sessionDetails.getFields());
        int p1ID = (int) p1[2];
        int p2ID = (int) p2[2];
        int gameID = (int) p1[1];

        System.out.println(p1ID + " " + p2ID + ": " + gameID);
        RemoteSpace game = new RemoteSpace(URI + "game" + gameID + "?keep");
        System.out.println(URI + "game" + gameID + "?keep");
        game.put(Messages.okMessage);
        game.put(Messages.okMessage);
        System.out.println("Sent the messages");

        ClientTetriminoGenerator player1Piece = new ClientTetriminoGenerator(game, p1ID);
        ClientTetriminoGenerator player2Piece = new ClientTetriminoGenerator(game, p2ID);

        Thread handle1 = new Thread(player1Piece);
        Thread handle2 = new Thread(player2Piece);

        handle1.start();
        handle2.start();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 10_000; i++) {
            player1Piece.nextPiece().getType();
            player2Piece.nextPiece().getType();
        }
        long end = System.currentTimeMillis() - start;

        System.out.println("Took 10 000 tetraminos in " + end + "ms");

        game.put(Messages.gameEndMessage(p1ID, 30));
        game.put(Messages.gameEndMessage(p2ID, 30));

        handle1.interrupt();
        handle2.interrupt();
        System.out.println("goodbye");
        System.exit(0);
    }

}