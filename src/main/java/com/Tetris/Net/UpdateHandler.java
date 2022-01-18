package com.Tetris.Net;

import com.Server.ServerMessages;
import com.Tetris.Model.Pair;
import com.Tetris.Model.Rotation;
import com.Tetris.Model.Tetrimino;
import com.Tetris.Model.TetrisInstance;
import com.Tetris.Model.Opponent.OpponentInstance;

import org.jspace.RemoteSpace;

public class UpdateHandler implements Runnable {
    private RemoteSpace net;

    private int opponentID;

    private TetrisInstance clientInstance;
    private OpponentInstance opponentInstance;

    @Override
    public void run() {
        while (true) {
            try {
                Object[] update = net.get(ServerMessages.updateTemplate(opponentID));
                UpdateKind kind = (UpdateKind) update[2];
                UpdateData data = (UpdateData) update[3];
                // Get the update
                switch (kind) {
                    case PiecePlaced:
                        Tetrimino tet = data.kind;
                        Pair place = data.location;
                        opponentInstance.placePiece(tet, place, Rotation.L);
                    case LineClear:
                        int[] linesToClear = data.lineList;
                        opponentInstance.clearLines(linesToClear);

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}