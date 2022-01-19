package com.Tetris.Net.Updates;

import com.Server.ServerMessages;
import com.Tetris.Model.Pair;
import com.Tetris.Model.Rotation;
import com.Tetris.Model.Tetrimino;
import com.Tetris.Model.TetrisInstance;
import com.Tetris.Model.Opponent.OpponentInstance;
import com.Tetris.Net.UpdateKind;

import org.jspace.RemoteSpace;
import org.jspace.Space;

public class UpdateHandler implements Runnable {
    private RemoteSpace net;
    private Space junkQueue;

    private int opponentID;

    private TetrisInstance clientInstance;
    private OpponentInstance opponentInstance;

    public UpdateHandler(RemoteSpace net, int opponentID, TetrisInstance clientInstance,
            OpponentInstance opponentInstance, Space junkQueue) {
        this.net = net;
        this.opponentID = opponentID;
        this.clientInstance = clientInstance;
        this.opponentInstance = opponentInstance;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object[] update = net.get(ServerMessages.updateTemplate(opponentID));
                UpdateKind kind = (UpdateKind) update[2];
                // Get the update
                switch (kind) {
                    case PiecePlaced:
                        PiecePlaced data = (PiecePlaced) update[3];
                        opponentInstance.placePiece(data.kind, data.location, data.rot);
                        break;
                    case LineClear:
                        LineClear data1 = (LineClear) update[3];
                        // TODO: Send some junk!
                        opponentInstance.clearLines(data1.linesCleared);
                        break;
                    case LinesSent:
                        LinesSent data2 = (LinesSent) update[3];
                        opponentInstance.sendLines(data2.amount);
                        break;
                    default:

                        break;

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}