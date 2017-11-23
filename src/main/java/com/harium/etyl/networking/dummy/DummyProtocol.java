package com.harium.etyl.networking.dummy;

import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.protocol.Protocol;
import com.harium.etyl.networking.util.ByteMessageUtils;

public class DummyProtocol implements Protocol {

    @Override
    public void receiveTCP(Peer peer, byte[] message) {}

    @Override
    public void receiveUDP(Peer peer, byte[] message) {}

    @Override
    public void receiveWebSocket(Peer peer, byte[] message) {}

    @Override
    public byte[] getPrefix() {
        return ByteMessageUtils.EMPTY_BYTES;
    }

    @Override
    public void addPeer(Peer peer){}

    @Override
    public void removePeer(Peer peer){}

    @Override
    public void receive(byte messageProtocol, Peer peer, byte[] message) {}

}
