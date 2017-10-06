package com.harium.etyl.networking.model;

public interface BaseServer extends BaseSender {
    void onConnect(Peer peer);

    void joinPeer(Peer peer);

    void leftPeer(Peer peer);

    Peer getPeer(int id);

    void removePeer(int id);
}
