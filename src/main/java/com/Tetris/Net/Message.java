package com.Tetris.Net;

import com.Tetris.Model.Tetriminos.Tetrimino;
import org.jspace.*;

public class Message {
    public static Object[] pieceRequest(int ID, int amount) {
        return new Tuple("piece_request", ID, amount).getTuple();
    }

    public static Template pieceRequest() {
        return new Template(new ActualField("piece_request"), new FormalField(Integer.class),
                new FormalField(Integer.class));
    }

    public static Tuple tetriminoPackage(int ID, Tetrimino[] list) {
        return new Tuple("tetrimino_package", ID, list);
    }

    public static Template tetriminoPackage(int ID) {
        return new Template(new ActualField("tetrimino_package"),
                new ActualField(ID),
                new FormalField(Tetrimino[].class));
    }
}
