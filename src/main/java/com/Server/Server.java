package com.Server;

import org.jspace.*;

import java.util.Scanner;

import com.Tetris.Net.Messages;

/*
This file contains the Server console application which is responsible for generating pieces for the clients and making matches between them so they can play
The folder which this file is a part of contains three other files:
    - ClientMock: This file is a testing class that immitates two clients wanting to play
    - GameThread: A runnable class that can maintain a game given a private space to do it in.
 */
public class Server {
    private static SpaceRepository games;

    // We need unique ids for players and games we do this by incrementing
    // Should be something else in a real application.
    private static int gameID;
    private static int playerCount;

    public static final String URI_PREFIX = "tcp://";
    public static final String URI_SUFFIX = "/?keep";

    public static void main(String[] args) throws InterruptedException {
        Scanner input = new Scanner(System.in);
        games = new SpaceRepository();
        gameID = 0;
        playerCount = 0;

        // Set up the lobby space and open a gate through which clients can access it.

        Space lobby = new SequentialSpace();
        games.add("lobby", lobby);

        System.out.println("Enter the ip-address and port below (ip:port)");
        String userURI = input.nextLine();
        String URI = URI_PREFIX + userURI + URI_SUFFIX;
        games.addGate(URI);
        System.out.println("Opening the gate: " + URI);
        input.close();

        while (true) {
            // Get two game requests and create two playerIDs
            lobby.get(Messages.gameRequest.getFields());
            lobby.get(Messages.gameRequest.getFields());
            int player1 = playerCount;
            int player2 = playerCount + 1;
            playerCount += 2;

            // Set up a private space for them and give it a new id
            Space game = new SequentialSpace();
            games.add("game" + gameID, game);

            // Start a GameThread to oversee the game and put the sessionDetails in the
            // space
            new Thread(new GameThread(game, gameID, player1, player2)).start();
            lobby.put(Messages.sessionDetails(gameID, player1, player2));
            lobby.put(Messages.sessionDetails(gameID, player2, player1));
            gameID++;
        }

    }

    // Construct a URI from an IP and a gameID. Used in the client application
    public static String getURI(String ip, int gameID) {
        return URI_PREFIX + ip + "/game" + gameID + "?keep";
    }
}