package com.Server;

import com.Tetris.Net.UpdateKind;
import com.Tetris.Net.Updates.UpdateData;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.Template;
import org.jspace.TemplateField;
import org.jspace.Tuple;

public class ServerMessages {
    public static final Template okTemplate = new Template(new ActualField("ok"));
    public static final Object[] okMessage = new Tuple("ok").getTuple();
    public static final Template gameRequest = new Template(new ActualField("game request"));
    public static final Template sessionDetails = new Template(
            new ActualField("session details"),
            new FormalField(Integer.class), // GameID
            new FormalField(Integer.class), // PlayerID
            new FormalField(Integer.class));// OpponentID

    public static final Template GameEndTemplate = new Template(
            new ActualField("final score"),
            new FormalField(Integer.class),
            new FormalField(Integer.class));

    public static final TemplateField[] updateTemplate(int ID) {
        return new Template(
                new ActualField("update"),
                new ActualField(ID),
                new FormalField(UpdateKind.class),
                new FormalField(UpdateData.class)).getFields();

    }

    public static Object[] update(int playerID, UpdateKind kind, UpdateData data) {
        return new Tuple("update", playerID, kind, data).getTuple();
    }

    public static Object[] gameEndMessage(int playerID, int score) {
        return new Tuple("final score", playerID, score).getTuple();
    }

    public static Object[] sessionDetails(int gameID, int playerID, int opponentID) {
        return new Tuple("session details", gameID, playerID, opponentID).getTuple();
    }

    public static final Object[] gameRequest() {
        return new Tuple("game request").getTuple();
    };
}
