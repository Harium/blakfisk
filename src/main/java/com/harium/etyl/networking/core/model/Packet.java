package com.harium.etyl.networking.core.model;

public class Packet {
    short id;
    Peer peer;
    byte[] message;

    public Packet(Peer peer, byte[] message) {
        this.peer = peer;
        this.message = message;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
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
