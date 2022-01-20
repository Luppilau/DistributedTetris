package com.Server;

import org.jspace.*;

import java.util.Scanner;

public class Server {
    private static SpaceRepository games;

    private static int LobbyIDC;
    private static int playerCount;

    public static final String URI_PREFIX = "tcp://";
    public static final String URI_SUFFIX = "/?keep";

    public static void main(String[] args) throws InterruptedException {
        Scanner input = new Scanner(System.in);
        games = new SpaceRepository();
        LobbyIDC = 0;
        playerCount = 0;

        Space lobby = new SequentialSpace();
        games.add("lobby", lobby);

        System.out.println("Enter the ip-address and port below (ip:port)");
        String userURI = input.nextLine();
        String URI = URI_PREFIX + userURI + URI_SUFFIX;
        games.addGate(URI);
        System.out.println("Opening the gate: " + URI);

        while (true) {
            lobby.get(ServerMessages.gameRequest.getFields());
            lobby.get(ServerMessages.gameRequest.getFields());
            int player1 = playerCount;
            int player2 = playerCount + 1;
            playerCount += 2;

            int gameID = LobbyIDC;
            LobbyIDC++;
            Space game = new SequentialSpace();
            games.add("game" + gameID, game);

            new Thread(new GameThread(game, gameID, player1, player2)).start();
            lobby.put(ServerMessages.sessionDetails(gameID, player1, player2));
            lobby.put(ServerMessages.sessionDetails(gameID, player2, player1));
        }

    }

    public static String getURI(String ip, int gameID) {
        return URI_PREFIX + ip + "/game" + gameID + "?keep";
    }
}