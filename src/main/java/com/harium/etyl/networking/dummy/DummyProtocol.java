package com.harium.etyl.networking.dummy;

import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.protocol.Protocol;
import com.harium.etyl.networking.util.ByteMessageUtils;

import java.util.Map;

public class DummyProtocol implements Protocol {

    @Override
    public byte[] getPrefix() {
        return ByteMessageUtils.EMPTY_BYTES;
    }

    @Override
    public void addPeer(Peer peer) {

    }

    @Override
    public void removePeer(Peer peer) {

    }

    @Override
    public void receiveTCP(Peer peer, byte[] message) {

    }

    @Override
    public void receiveUDP(Peer peer, byte[] message) {

    }

    @Override
    public void receiveWebSocket(Peer peer, byte[] message) {

    }

    @Override
    public void sendTCP(Peer peer, byte[] message) {

    }

    @Override
    public void sendUDP(Peer peer, byte[] message) {

    }

    @Override
    public void sendWebSocket(Peer peer, byte[] message) {

    }

    @Override
    public void receive(byte messageProtocol, Peer peer, byte[] message) {

    }

    @Override
    public void tick() {

    }

    @Override
    public Map<Integer, Peer> getPeers() {
        return null;
    }
}
