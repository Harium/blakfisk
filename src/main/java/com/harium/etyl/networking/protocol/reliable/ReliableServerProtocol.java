package com.harium.etyl.networking.protocol.reliable;

import com.harium.etyl.networking.model.BaseServer;
import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.protocol.Protocol;
import com.harium.etyl.networking.protocol.common.ServerProtocol;
import com.harium.etyl.networking.util.ByteMessageUtils;

import java.util.*;

public class ReliableServerProtocol extends ServerProtocol {

    ReliableHandler handler;

    public ReliableServerProtocol(String prefix, BaseServer server, Protocol listener) {
        super(prefix, server);
        handler = new ReliableHandler(listener);
    }

    @Override
    public void receiveTCP(Peer peer, byte[] message) {
        handler.receiveTCP(peer, message);
    }

    @Override
    public void receiveUDP(Peer peer, byte[] message) {
        handler.receiveUDP(peer, message);
    }

    public void notify(Peer peer, String message) {
        handler.notify(peer, message.getBytes());
    }

    public void notify(Peer peer, byte[] message) {
        handler.notify(peer, message);
    }

    public void notifyAll(byte[] message) {
        handler.notifyAll(message);
    }

    public void notifyAll(String message) {
        notifyAll(message.getBytes());
    }

    public void notifyAllExcept(Peer peer, byte[] message) {
        handler.notifyAllExcept(peer, message);
    }

    public void notifyAllExcept(Peer peer, String message) {
        notifyAllExcept(peer, message.getBytes());
    }

    @Override
    public void tick() {
        super.tick();
        handler.dispatch();
    }

    @Override
    public void removePeer(Peer peer) {
        super.removePeer(peer);
        handler.addLeftPeer(peer.getId());
    }
}
