package com.harium.etyl.networking.model;

public class Packet {
    Peer peer;
    byte[] message;

    public Packet(Peer peer, byte[] message) {
        this.peer = peer;
        this.message = message;
    }

    public Peer getPeer() {
        return peer;
    }

    public void setPeer(Peer peer) {
        this.peer = peer;
    }

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }
}
