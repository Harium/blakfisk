package com.harium.etyl.networking.core.dummy;

import com.harium.etyl.networking.core.model.Peer;
import com.harium.etyl.networking.core.protocol.Protocol;
import com.harium.etyl.networking.core.helper.ByteMessageHelper;

import java.util.Map;

public class LogProtocol implements Protocol {

    @Override
    public void receiveTCP(Peer peer, byte[] message) {
        System.out.println("Log Protocol - Recv TCP: " + new String(message));
    }

    @Override
    public void receiveUDP(Peer peer, byte[] message) {
        System.out.println("Log Protocol - Recv UDP: " + new String(message));
    }

    @Override
    public void receiveWebSocket(Peer peer, byte[] message) {
        System.out.println("Log Protocol - Recv WebSocket: " + new String(message));
    }

    @Override
    public byte[] getPrefix() {
        return ByteMessageHelper.EMPTY_BYTES;
    }

    @Override
    public void addPeer(Peer peer) {
        System.out.println("Log Protocol - added peer " + peer.getId());
    }

    @Override
    public void removePeer(Peer peer) {
        System.out.println("Log Protocol - removed peer " + peer.getId());
    }

    @Override
    public void receive(byte messageProtocol, Peer peer, byte[] message) {
        System.out.println("Log Protocol - Recv: " + new String(message));
    }

    @Override
    public void sendTCP(Peer peer, byte[] message) {
        System.out.println("Log Protocol - Send TCP: " + new String(message));
    }

    @Override
    public void sendUDP(Peer peer, byte[] message) {
        System.out.println("Log Protocol - Send UDP: " + new String(message));
    }

    @Override
    public void sendWebSocket(Peer peer, byte[] message) {
        System.out.println("Log Protocol - Send WebSocket: " + new String(message));
    }

    @Override
    public void tick() {
        System.out.println("Log Protocol - tick");
    }

    @Override
    public Map<Integer, Peer> getPeers() {
        return null;
    }

}
