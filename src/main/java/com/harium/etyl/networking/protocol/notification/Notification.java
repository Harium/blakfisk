package com.harium.etyl.networking.protocol.notification;

import com.harium.etyl.networking.model.Peer;

public class Notification {
    Peer peer;
    byte[] message;

    public Notification(Peer peer, byte[] message) {
        this.peer = peer;
        this.message = message;
    }
}
