package com.harium.etyl.networking.protocol.reliable;

import com.harium.etyl.networking.model.Peer;

public class Packet {
    Peer peer;
    byte[] message;

    public Packet(Peer peer, byte[] message) {
        this.peer = peer;
        this.message = message;
    }
}
