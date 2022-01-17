package com.Server;

import com.Tetris.Model.Tetrimino;
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
        RemoteSpace game = new RemoteSpace(Server.URI + "game" + 0 + "?keep");
        System.out.println(Server.URI + "game" + 0 + "?keep");
        game.put(ServerMessages.okMessage);
        game.put(ServerMessages.okMessage);
        System.out.println("Sent the messages");

        game.put(Message.pieceRequest(p1ID,5));
        game.put("wow");
        game.put("wowow");
        System.out.println("Sent piece request");
        Tetrimino[] minos = (Tetrimino[]) game.get(Message.tetriminoPackage().getFields())[2];
        for (Tetrimino T : minos) {
            System.out.println(T);
        }
        System.out.println();
        game.put(Message.pieceRequest(p2ID,5));
        minos = (Tetrimino[]) game.get(Message.tetriminoPackage().getFields())[2];
        for (Tetrimino T : minos) {
            System.out.println(T);
        }
    }


}