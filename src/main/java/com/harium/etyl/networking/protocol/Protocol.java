package com.harium.etyl.networking.protocol;

import com.harium.etyl.networking.model.Peer;

public interface Protocol {
    byte[] getPrefix();

    void addPeer(Peer peer);

    void removePeer(Peer peer);

    void receiveTCP(Peer peer, byte[] message);

    void receiveUDP(Peer peer, byte[] message);

    void receiveWebSocket(Peer peer, byte[] message);

    void receive(byte messageProtocol, Peer peer, byte[] message);
}
