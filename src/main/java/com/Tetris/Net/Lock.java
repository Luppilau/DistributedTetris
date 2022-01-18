package com.Tetris.Net;

import org.jspace.ActualField;
import org.jspace.SequentialSpace;
import org.jspace.Space;

public class Lock {
    private Space lock;

    public Lock() {
        lock = new SequentialSpace();
        try {
            lock.put("lock");
        } catch (InterruptedException e) {
        }
    }

    public void getLock() {
        try {
            lock.get(new ActualField("lock"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void putLock() {
        try {
            lock.put("lock");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}