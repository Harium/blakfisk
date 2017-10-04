package com.harium.etyl.networking.protocol.common;

import com.harium.etyl.networking.model.BaseClient;
import com.harium.etyl.networking.protocol.ProtocolHandler;

public abstract class RawClientProtocol extends ClientProtocol {

    public RawClientProtocol(String prefix, BaseClient client) {
        super(prefix, client);
    }

    protected void sendTCP(byte[] message) {
        client.sendToTCP(ProtocolHandler.packMessage(prefix, message));
    }

    protected void sendUDP(byte[] message) {
        client.sendToUDP(ProtocolHandler.packMessage(prefix, message));
    }

    protected void sendWebSocket(byte[] message) {
        client.sendToWebSocket(ProtocolHandler.packMessage(prefix, message));
    }
}
