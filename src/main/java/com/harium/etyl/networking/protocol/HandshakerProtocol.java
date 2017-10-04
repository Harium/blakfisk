package com.harium.etyl.networking.protocol;

import com.harium.etyl.networking.model.BaseServer;
import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.protocol.common.ProtocolImpl;

public abstract class HandshakerProtocol extends ProtocolImpl implements Protocol {

    protected BaseServer server;

    public HandshakerProtocol(String prefix, BaseServer server) {
        super(prefix);
        this.server = server;
    }

    public void addPeer(Peer peer) {
        String message = buildHandshake(peer);

        server.sendToTCP(peer.getId(), ProtocolHandler.packMessage(prefix, message));
    }

    public abstract String buildHandshake(Peer peer);

    @Override
    public void receiveTCP(Peer peer, byte[] message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void receiveUDP(Peer peer, byte[] message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void receiveWebSocket(Peer peer, byte[] message) {
        receiveTCP(peer, message);
    }

}
