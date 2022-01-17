package com.Server;

import org.jspace.*;

public class Server {
    private static SpaceRepository games;

    private static int LobbyIDC;
    private static int playerCount;

    public static final String URI = "tcp://localhost:9090/";

    public static void main(String[] args) throws InterruptedException {
        games = new SpaceRepository();
        LobbyIDC = 0;
        playerCount = 10;

        Space lobby = new SequentialSpace();
        games.add("lobby", lobby);
        games.addGate(URI + "?keep");
        System.out.println(URI + "?keep");

        while (true) {
            System.out.println("Waiting for game requests");
            lobby.get(ServerMessages.gameRequest.getFields());
            lobby.get(ServerMessages.gameRequest.getFields());
            int player1 = playerCount;
            int player2 = playerCount + 1;
            playerCount += 2;

            int gameID = LobbyIDC; LobbyIDC++;
            Space game = new SequentialSpace();
            games.add("game"+gameID,game);

            new Thread(new GameThread(game, gameID,player1,player2)).start();
            lobby.put(ServerMessages.sessionDetails(gameID,player1));
            lobby.put(ServerMessages.sessionDetails(gameID,player2));
            System.out.println("");
        }

    }
}