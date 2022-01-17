package com.Server;

import org.jspace.RemoteSpace;

import java.io.IOException;
import java.util.Scanner;

public class ClientMock {
    public static void main(String[] args) throws IOException, InterruptedException {

        RemoteSpace server = new RemoteSpace(Server.URI + "lobby" + "?keep");

        server.put("game request");
        server.put("game request");
        System.out.println("put the requets");


    }
}
