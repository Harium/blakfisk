package com.harium.etyl.networking.model;

public interface BaseServer {
    void joinPeer(Peer peer);

    void leftPeer(Peer peer);

    Peer getPeer(String id);

    void removePeer(String id);
}
