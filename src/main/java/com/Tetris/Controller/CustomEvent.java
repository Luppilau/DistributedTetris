package com.Tetris.Controller;

import javafx.event.Event;
import javafx.event.EventType;

/*
    Custom event type, to enable firing of and listening to homemade JavaFX events
*/
public class CustomEvent extends Event {
    public static final EventType<CustomEvent> NextPieceEvent = new EventType<CustomEvent>(ANY);

    public CustomEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
