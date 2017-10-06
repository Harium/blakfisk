package com.harium.etyl.networking.protocol.common;

import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.model.data.MessageProtocol;
import com.harium.etyl.networking.protocol.Protocol;
import com.harium.etyl.networking.util.ByteMessageUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class ProtocolImpl implements Protocol {

    protected byte[] prefix = ByteMessageUtils.EMPTY_BYTES;
    protected Map<Integer, Peer> peers = new LinkedHashMap<>();

    public ProtocolImpl(String prefix) {
        super();
        this.prefix = prefix.getBytes();
    }

    public byte[] getPrefix() {
        return prefix;
    }

    @Override
    public void receive(byte messageProtocol, Peer peer, byte[] message) {
        if (MessageProtocol.TCP == messageProtocol) {
            receiveTCP(peer, message);
        } else if (MessageProtocol.UDP == messageProtocol) {
            receiveUDP(peer, message);
        } else if (MessageProtocol.WEBSOCKET == messageProtocol) {
            receiveWebSocket(peer, message);
        } else {
            System.err.println("Undefined Connection Protocol");
        }
    }

    @Override
    public void receiveWebSocket(Peer peer, byte[] message) {
        receiveTCP(peer, message);
    }

    @Override
    public void addPeer(Peer peer) {
        peers.put(peer.getId(), peer);
    }

    @Override
    public void removePeer(Peer peer) {
        peers.remove(peer.getId());
    }

}
