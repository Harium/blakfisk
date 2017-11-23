package com.prodec.dronepark.simulator.networking.server.protocol;

import com.harium.etyl.networking.model.Peer;

public class Notification {
    Peer peer;
    String message;
    int id;

    public Notification(Peer peer, String message) {
        this.peer = peer;
        this.message = message;
    }
}
