package com.Server;

import com.Tetris.Model.Tetrimino;
import com.Tetris.Net.ClientPieceGenerator;
import com.Tetris.Net.Message;
import org.jspace.RemoteSpace;

import java.io.IOException;
import java.util.Scanner;

public class ClientMock {
    public static void main(String[] args) throws IOException, InterruptedException {

        RemoteSpace server = new RemoteSpace(Server.URI + "lobby" + "?keep");

        server.put("game request");
        server.put("game request");
        System.out.println("put the requests");

        Object[] p1 = server.get(ServerMessages.sessionDetails.getFields());
        Object[] p2 = server.get(ServerMessages.sessionDetails.getFields());
        int p1ID = (int) p1[2];
        int p2ID = (int) p2[2];
        int gameID = (int) p1[1];

        System.out.println(p1ID + " " + p2ID + ": " + gameID);
        RemoteSpace game = new RemoteSpace(Server.URI + "game" + gameID + "?keep");
        System.out.println(Server.URI + "game" + gameID + "?keep");
        game.put(ServerMessages.okMessage);
        game.put(ServerMessages.okMessage);
        System.out.println("Sent the messages");

        ClientPieceGenerator player1Piece = new ClientPieceGenerator(game,p1ID);
        ClientPieceGenerator player2Piece = new ClientPieceGenerator(game,p2ID);

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

        System.out.println("Took 10 000 tetraminos in " + end +"ms");

        game.put(ServerMessages.gameEndMessage(p1ID,30));
        game.put(ServerMessages.gameEndMessage(p2ID,30));

        handle1.interrupt();
        handle2.interrupt();
        System.out.println("goodbye");
        System.exit(0);
    }


}