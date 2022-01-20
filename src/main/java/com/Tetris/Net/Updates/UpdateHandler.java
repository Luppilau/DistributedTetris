package com.Tetris.Net.Updates;

import java.util.Arrays;
import java.util.Random;

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
    private int playerID;

    private Random holeGenerator;

    private TetrisInstance clientInstance;
    private OpponentInstance opponentInstance;

    public UpdateHandler(RemoteSpace net, int opponentID, int playerID, TetrisInstance clientInstance,
            OpponentInstance opponentInstance, Space junkQueue) {

        this.net = net;
        this.opponentID = opponentID;
        this.clientInstance = clientInstance;
        this.opponentInstance = opponentInstance;
        this.junkQueue = junkQueue;
        holeGenerator = new Random();
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
                        int nLines = data1.linesCleared.length;
                        if (nLines > 1 && nLines < 5) {
                            try {
                                int linesSent = 0;
                                switch (nLines) {
                                    case 2:
                                        linesSent = 1;
                                        break;
                                    case 3:
                                        linesSent = 2;
                                        break;
                                    case 4:
                                        linesSent = 4;
                                        break;
                                }
                                int hole = holeGenerator.nextInt(9);
                                junkQueue.put(linesSent, hole);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                        opponentInstance.clearLines(data1.linesCleared);
                        break;
                    case LinesSent:
                        LinesSent data2 = (LinesSent) update[3];
                        opponentInstance.sendLines(data2.amount, data2.hole);
                        break;

                    case Swap:
                        Swap data3 = (Swap) update[3];
                        opponentInstance.swap(data3.swap);
                        break;
                    case NextPiece:
                        NextPiece data4 = (NextPiece) update[3];
                        opponentInstance.nextPiece(data4.nextPiece);
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