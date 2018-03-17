package com.harium.etyl.networking.core.dummy;

import com.harium.etyl.networking.core.model.Peer;
import com.harium.etyl.networking.core.protocol.Protocol;
import com.harium.etyl.networking.core.helper.ByteMessageHelper;

import java.util.Map;

public class DummyProtocol implements Protocol {

    @Override
    public byte[] getPrefix() {
        return ByteMessageHelper.EMPTY_BYTES;
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
