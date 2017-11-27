package com.harium.etyl.networking.protocol.common;

import com.harium.etyl.networking.model.BaseServer;
import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.model.data.ConnectionData;
import com.harium.etyl.networking.model.data.ConnectionType;
import com.harium.etyl.networking.protocol.ProtocolHandler;

import java.util.Collection;

public abstract class ServerProtocol extends ProtocolImpl {

    protected BaseServer server;

    public ServerProtocol(byte[] prefix, BaseServer server) {
        super(prefix);
        this.server = server;
    }

    public ServerProtocol(String prefix, BaseServer server) {
        super(prefix);
        this.server = server;
    }

    public void sendTCP(int peerId, byte[] message) {
        ConnectionData data = ProtocolHandler.packMessage(prefix, message);
        data.connectionType = ConnectionType.TCP;
        server.sendToTCP(peerId, data);
    }

    public void sendTCP(Peer peer, byte[] message) {
        sendTCP(peer.getId(), message);
    }

    public void sendTCPtoAll(byte[] message) {
        ConnectionData data = ProtocolHandler.packMessage(prefix, message);
        data.connectionType = ConnectionType.TCP;
        server.sendToAllTCP(data);
    }

    public void sendTCPtoAll(Collection<Integer> peerIds, byte[] message) {
        for (Integer peer : peerIds) {
            this.sendTCP(peer, message);
        }
    }

    public void sendTCPtoAllExcept(Peer peer, byte[] message) {
        ConnectionData data = ProtocolHandler.packMessage(prefix, message);
        data.connectionType = ConnectionType.TCP;
        server.sendToAllExceptTCP(peer.getId(), data);
    }

    public void sendTCPNoPrefix(Peer peer, byte[] message) {
        server.sendToTCP(peer.getId(), ProtocolHandler.packRawMessage(message));
    }

    public void sendUDP(int peerId, byte[] message) {
        ConnectionData data = ProtocolHandler.packMessage(prefix, message);
        data.connectionType = ConnectionType.UDP;
        server.sendToUDP(peerId, data);
    }

    public void sendUDP(Peer peer, byte[] message) {
        sendUDP(peer.getId(), message);
    }

    public void sendUDPtoAll(byte[] message) {
        ConnectionData data = ProtocolHandler.packMessage(prefix, message);
        data.connectionType = ConnectionType.UDP;
        server.sendToAllUDP(data);
    }

    public void sendUDPtoAll(Collection<Integer> peers, byte[] message) {
        for (Integer peer : peers) {
            sendUDP(peer, message);
        }
    }

    public void sendUDPtoAllExcept(Peer peer, byte[] message) {
        ConnectionData data = ProtocolHandler.packMessage(prefix, message);
        data.connectionType = ConnectionType.UDP;
        server.sendToAllExceptUDP(peer.getId(), data);
    }

    public void sendUDPNoPrefix(Peer peer, byte[] message) {
        server.sendToUDP(peer.getId(), ProtocolHandler.packRawMessage(message));
    }

    public void sendWebSocket(int peerId, byte[] message) {
        ConnectionData data = ProtocolHandler.packMessage(prefix, message);
        data.connectionType = ConnectionType.WEBSOCKET;
        server.sendToUDP(peerId, data);
    }

    public void sendWebSocket(Peer peer, byte[] message) {
        sendWebSocket(peer.getId(), message);
    }

    public void sendWebSockettoAll(byte[] message) {
        ConnectionData data = ProtocolHandler.packMessage(prefix, message);
        data.connectionType = ConnectionType.WEBSOCKET;
        server.sendToAllTCP(data);
    }

    public void sendWebSockettoAll(Collection<Integer> peers, byte[] message) {
        for (Integer peer : peers) {
            sendWebSocket(peer, message);
        }
    }

    public void sendWebsockettoAllExcept(Peer peer, byte[] message) {
        ConnectionData data = ProtocolHandler.packMessage(prefix, message);
        data.connectionType = ConnectionType.WEBSOCKET;
        server.sendToAllExceptTCP(peer.getId(), data);
    }

    public void sendWebSocketNoPrefix(Peer peer, byte[] message) {
        server.sendToTCP(peer.getId(), ProtocolHandler.packRawMessage(message));
    }
}
