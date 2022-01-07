package com.Tetris.Model;

import javafx.event.Event;
import javafx.event.EventType;

public class CustomEvent extends Event {
    public static final EventType<CustomEvent> NextPieceEvent = new EventType<CustomEvent>(ANY);

    public CustomEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
