package com.Tetris.Net;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.Template;

public class GameEndMessage {
    // Game End Message:
    // ("game end", session_id, score)

    public static final Template GameEndTemplate = new Template(new ActualField("final score"),
            new FormalField(Integer.class),
            new FormalField(Integer.class));
}