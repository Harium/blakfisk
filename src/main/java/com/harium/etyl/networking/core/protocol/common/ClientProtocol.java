package com.harium.etyl.networking.core.protocol.common;

import com.harium.etyl.networking.core.model.BaseClient;
import com.harium.etyl.networking.core.model.Peer;
import com.harium.etyl.networking.core.model.data.ConnectionData;
import com.harium.etyl.networking.core.model.data.ConnectionType;
import com.harium.etyl.networking.core.protocol.ProtocolHandler;

public abstract class ClientProtocol extends ProtocolImpl {

    protected static final Peer SERVER = new Peer() {
        @Override
        public int getId() {
            return Integer.MIN_VALUE;
        }
    };

    protected BaseClient client;

    public ClientProtocol(String prefix, BaseClient client) {
        super(prefix);
        this.client = client;
    }

    @Override
    public void sendTCP(Peer peer, byte[] message) {
        ConnectionData data = ProtocolHandler.packMessage(prefix, message);
        data.connectionType = ConnectionType.TCP;
        client.sendToTCP(data);
    }

    protected void sendTCP(byte[] message) {
        sendTCP(SERVER, message);
    }

    @Override
    public void sendUDP(Peer peer, byte[] message) {
        ConnectionData data = ProtocolHandler.packMessage(prefix, message);
        data.connectionType = ConnectionType.UDP;
        client.sendToUDP(data);
    }

    protected void sendUDP(byte[] message) {
        sendUDP(SERVER, message);
    }

    @Override
    public void sendWebSocket(Peer peer, byte[] message) {
        ConnectionData data = ProtocolHandler.packMessage(prefix, message);
        data.connectionType = ConnectionType.WEBSOCKET;
        client.sendToWebSocket(data);
    }

    public void sendWebSocket(byte[] message) {
        sendWebSocket(SERVER, message);
    }

}
