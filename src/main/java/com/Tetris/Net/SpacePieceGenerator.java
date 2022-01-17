package com.Tetris.Net;

import com.Tetris.Model.FallingPiece;
import com.Tetris.Model.PieceGenerator;
import com.Tetris.Model.Tetrimino;
import com.Tetris.Model.Tetriminoes.I;
import org.jspace.*;

import java.io.IOException;

public class SpacePieceGenerator implements PieceGenerator {
    private Space channel;

    public SpacePieceGenerator() throws IOException, InterruptedException {
        Space channel = new QueueSpace();
        new Thread(new ConnectionHandler("", channel, 1)).start();
    }

    public FallingPiece nextPiece() {
        try {
            Tetrimino T = (Tetrimino) channel.get(new FormalField(Tetrimino.class))[0];
            channel.put(Message.pieceRequest(0, 1));
            return FallingPiece.newFallingPiece(T);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Failed to get next piece...");
        return new I();
    }
}

class ConnectionHandler implements Runnable {
    private static final int BUFFER_SIZE = 5;

    private Space channel;
    private RemoteSpace server_channel;
    private int sessionID;

    public ConnectionHandler(String uri, Space channel, int sessionID) throws IOException, InterruptedException {
        server_channel = new RemoteSpace(uri);
        this.channel = channel;

        server_channel.put(Message.pieceRequest(sessionID, BUFFER_SIZE));
        Tetrimino[] minoes = (Tetrimino[]) server_channel.get(Message.tetriminoPackage(sessionID).getFields())[3];
        for (Tetrimino t : minoes) {
            channel.put(t);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                channel.get(Message.pieceRequest().getFields());
                server_channel.put(Message.pieceRequest(sessionID, 1));
                // Cursed line
                Tetrimino t = (Tetrimino) ((Tetrimino[]) server_channel
                        .get(Message.tetriminoPackage(sessionID).getFields())[3])[0];
                channel.put(t);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
