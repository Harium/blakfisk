package com.harium.etyl.networking.protocol.common;

import com.harium.etyl.networking.model.BaseServer;
import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.model.data.ConnectionData;
import com.harium.etyl.networking.model.data.MessageProtocol;
import com.harium.etyl.networking.protocol.ProtocolHandler;

import java.util.Collection;

public abstract class ServerProtocol extends ProtocolImpl {

    protected BaseServer server;

    public ServerProtocol(String prefix, BaseServer server) {
        super(prefix);
        this.server = server;
    }

    public void sendTCP(int peerId, String message) {
        server.sendToTCP(peerId, ProtocolHandler.packMessage(prefix, message));
    }

    public void sendTCP(Peer peer, String message) {
        this.sendTCP(peer.getId(), message);
    }

    public void sendTCPNoPrefix(Peer peer, String message) {
        server.sendToTCP(peer.getId(), ProtocolHandler.packRawMessage(message));
    }

    public void sendTCPtoAll(String message) {
        server.sendToAllTCP(ProtocolHandler.packMessage(prefix, message));
    }

    public void sendTCPtoAll(Collection<Integer> peerIds, String message) {
        for (Integer peer : peerIds) {
            this.sendTCP(peer, message);
        }
    }

    public void sendTCPtoAllExcept(Peer peer, String message) {
        server.sendToAllExceptTCP(peer.getId(), ProtocolHandler.packMessage(prefix, message));
    }

    public void sendUDP(int peerId, String message) {
        server.sendToUDP(peerId, ProtocolHandler.packMessage(prefix, message));
    }

    public void sendUDP(Peer peer, String message) {
        sendUDP(peer.getId(), message);
    }

    public void sendUDPNoPrefix(Peer peer, String message) {
        server.sendToUDP(peer.getId(), ProtocolHandler.packRawMessage(message));
    }

    public void sendUDPtoAll(String message) {
        ConnectionData data = ProtocolHandler.packMessage(prefix, message);
        data.connectionType = MessageProtocol.UDP;
        server.sendToAllUDP(data);
    }

    public void sendUDPtoAll(Collection<Integer> peers, String message) {
        for (Integer peer : peers) {
            this.sendUDP(peer, message);
        }
    }

    public void sendUDPtoAllExcept(Peer peer, String message) {
        server.sendToAllExceptUDP(peer.getId(), ProtocolHandler.packMessage(prefix, message));
    }

}
