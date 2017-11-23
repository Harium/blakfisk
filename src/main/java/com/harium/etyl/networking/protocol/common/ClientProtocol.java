package com.harium.etyl.networking.protocol.common;

import com.harium.etyl.networking.model.BaseClient;
import com.harium.etyl.networking.model.data.ConnectionData;
import com.harium.etyl.networking.model.data.ConnectionType;
import com.harium.etyl.networking.protocol.ProtocolHandler;

public abstract class ClientProtocol extends ProtocolImpl {

    protected BaseClient client;

    public ClientProtocol(String prefix, BaseClient client) {
        super(prefix);
        this.client = client;
    }

    protected void sendTCP(byte[] message) {
        ConnectionData data = ProtocolHandler.packMessage(prefix, message);
        data.connectionType = ConnectionType.TCP;
        client.sendToTCP(data);
    }

    protected void sendUDP(byte[] message) {
        ConnectionData data = ProtocolHandler.packMessage(prefix, message);
        data.connectionType = ConnectionType.UDP;
        client.sendToUDP(data);
    }

    protected void sendWebSocket(byte[] message) {
        ConnectionData data = ProtocolHandler.packMessage(prefix, message);
        data.connectionType = ConnectionType.WEBSOCKET;
        client.sendToWebSocket(data);
    }

}
