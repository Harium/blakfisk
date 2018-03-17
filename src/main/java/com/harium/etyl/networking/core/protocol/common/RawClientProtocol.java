package com.harium.etyl.networking.core.protocol.common;

import com.harium.etyl.networking.core.model.BaseClient;
import com.harium.etyl.networking.core.protocol.ProtocolHandler;

public abstract class RawClientProtocol extends ClientProtocol {

    public RawClientProtocol(String prefix, BaseClient client) {
        super(prefix, client);
    }

    public void sendTCP(byte[] message) {
        client.sendToTCP(ProtocolHandler.packMessage(prefix, message));
    }

    public void sendUDP(byte[] message) {
        client.sendToUDP(ProtocolHandler.packMessage(prefix, message));
    }

    public void sendWebSocket(byte[] message) {
        client.sendToWebSocket(ProtocolHandler.packMessage(prefix, message));
    }
}
