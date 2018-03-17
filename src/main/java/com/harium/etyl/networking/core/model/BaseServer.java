package com.harium.etyl.networking.core.model;

public interface BaseServer extends BaseSender {
    void onConnect(Peer peer);

    void joinPeer(Peer peer);

    void leftPeer(Peer peer);

    boolean hasPeer(int id);

    Peer getPeer(int id);

    void removePeer(int id);
}
