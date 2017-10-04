package com.harium.etyl.networking.protocol.common;

import com.harium.etyl.networking.model.BaseClient;
import com.harium.etyl.networking.model.data.ConnectionData;
import com.harium.etyl.networking.model.data.MessageProtocol;
import com.harium.etyl.networking.protocol.ProtocolHandler;
import com.harium.etyl.networking.model.Peer;

public abstract class StringClientProtocol extends ClientProtocol {

    public StringClientProtocol(String prefix, BaseClient client) {
        super(prefix, client);
    }

    protected void sendTCP(String message) {
        ConnectionData data = ProtocolHandler.packMessage(prefix, message.getBytes());
        data.connectionType = MessageProtocol.TCP;
        client.sendToTCP(data);
    }

    protected void sendUDP(String message) {
        ConnectionData data = ProtocolHandler.packMessage(prefix, message.getBytes());
        data.connectionType = MessageProtocol.UDP;
        client.sendToUDP(data);
    }

    protected void sendWebSocket(String message) {
        ConnectionData data = ProtocolHandler.packMessage(prefix, message.getBytes());
        data.connectionType = MessageProtocol.WEBSOCKET;
        client.sendToWebSocket(data);
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
