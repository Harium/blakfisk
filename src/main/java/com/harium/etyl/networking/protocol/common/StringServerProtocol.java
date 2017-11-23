package com.harium.etyl.networking.protocol.common;

import com.harium.etyl.networking.model.BaseServer;
import com.harium.etyl.networking.model.Peer;

import java.util.Collection;

public abstract class StringServerProtocol extends ServerProtocol {

    public StringServerProtocol(String prefix, BaseServer server) {
        super(prefix, server);
    }

    public void sendTCP(int peerId, String message) {
        sendTCP(peerId, message.getBytes());
    }

    public void sendTCP(Peer peer, String message) {
        super.sendTCP(peer, message.getBytes());
    }

    public void sendTCPtoAll(String message) {
        super.sendTCPtoAll(message.getBytes());
    }

    public void sendTCPtoAll(Collection<Integer> peerIds, String message) {
        super.sendTCPtoAll(peerIds, message.getBytes());
    }

    public void sendTCPtoAllExcept(Peer peer, String message) {
        super.sendTCPtoAllExcept(peer, message.getBytes());
    }

    public void sendTCPNoPrefix(Peer peer, String message) {
        super.sendTCPNoPrefix(peer, message.getBytes());
    }

    public void sendUDP(int peerId, String message) {
        sendUDP(peerId, message.getBytes());
    }

    public void sendUDP(Peer peer, String message) {
        super.sendUDP(peer, message.getBytes());
    }

    public void sendUDPtoAll(String message) {
        super.sendUDPtoAll(message.getBytes());
    }

    public void sendUDPtoAll(Collection<Integer> peerIds, String message) {
        super.sendUDPtoAll(peerIds, message.getBytes());
    }

    public void sendUDPtoAllExcept(Peer peer, String message) {
        super.sendUDPtoAllExcept(peer, message.getBytes());
    }

    public void sendUDPNoPrefix(Peer peer, String message) {
        super.sendUDPNoPrefix(peer, message.getBytes());
    }

    @Override
    public void receiveTCP(Peer peer, byte[] message) {
        receiveTCP(peer, new String(message));
    }

    protected abstract void receiveTCP(Peer peer, String message);

    @Override
    public void receiveUDP(Peer peer, byte[] message) {
        receiveUDP(peer, new String(message));
    }

    protected abstract void receiveUDP(Peer peer, String message);

    @Override
    public void receiveWebSocket(Peer peer, byte[] message) {
        receiveWebSocket(peer, new String(message));
    }

    protected void receiveWebSocket(Peer peer, String message) {
        receiveTCP(peer, message);
    }
}
