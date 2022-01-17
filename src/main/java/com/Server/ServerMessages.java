package com.Server;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.Template;
import org.jspace.Tuple;

public class ServerMessages {
    public static final Template okTemplate = new Template(new ActualField("ok"));
    public static final Object[] okMessage = new Tuple("ok").getTuple();
    public static final Template gameRequest = new Template(new ActualField("game request"));
    public static final Template sessionDetails = new Template(new ActualField("session details"), new FormalField(Integer.class), new FormalField(Integer.class));

    public static Object[] sessionDetails(int gameID, int playerID) {
        return new Tuple("session details", gameID, playerID).getTuple();
    }
}
