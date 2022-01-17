package com.Server;

import org.jspace.ActualField;
import org.jspace.QueueSpace;
import org.jspace.SequentialSpace;
import org.jspace.Space;
import org.jspace.SpaceRepository;

public class Server {
    private static SpaceRepository games;

    private static int LobbyIDC;
    private static int playerCount;

    private static final String URI = "tcp://localhost:31415/";

    public static void main(String[] args) throws InterruptedException {
        games = new SpaceRepository();
        LobbyIDC = 0;
        playerCount = 10;

        Space lobby = new SequentialSpace();
        games.add("lobby", lobby);

        while (true) {
            System.out.println("Waiting for game requests");
            lobby.get(new ActualField("game request"));
            lobby.get(new ActualField("game request"));
            int player1 = playerCount;
            int player2 = playerCount + 1;
            playerCount += 2;
;
            int gameID = LobbyIDC; LobbyIDC++;
            Space game = new QueueSpace();
            games.put(""+gameID,game);
            games.addGate(URI + gameID + "?keep");

            System.out.println("starting game thread");
            new Thread(new GameThread(game, gameID)).start();

            lobby.put()

        }

    }
}