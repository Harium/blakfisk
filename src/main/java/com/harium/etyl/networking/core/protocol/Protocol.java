package com.harium.etyl.networking.core.protocol;

import com.harium.etyl.networking.core.model.Peer;

import java.util.Map;

public interface Protocol {
    byte[] getPrefix();

    void addPeer(Peer peer);

    void removePeer(Peer peer);

    void receiveTCP(Peer peer, byte[] message);

    void receiveUDP(Peer peer, byte[] message);

    void receiveWebSocket(Peer peer, byte[] message);

    void sendTCP(Peer peer, byte[] message);

    void sendUDP(Peer peer, byte[] message);

    void sendWebSocket(Peer peer, byte[] message);

    void receive(byte messageProtocol, Peer peer, byte[] message);

    void tick();

    Map<Integer, Peer> getPeers();
}
