package com.harium.etyl.networking.test;

import com.harium.etyl.networking.model.Peer;

public class DummyPeer extends Peer {
    int id;

    public DummyPeer() {
        this.id = Integer.MIN_VALUE;
    }

    public DummyPeer(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }
}
