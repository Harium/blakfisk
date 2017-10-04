package com.harium.etyl.networking.protocol.common;

import com.harium.etyl.networking.kryo.KryoClient;
import com.harium.etyl.networking.protocol.ProtocolHandler;

public abstract class RawClientProtocol extends ClientProtocol {

    public RawClientProtocol(String prefix, KryoClient client) {
        super(prefix, client);
    }

    protected void sendTCP(byte[] message) {
        client.sendTCP(ProtocolHandler.packMessage(prefix, message));
    }

    protected void sendUDP(byte[] message) {
        client.sendUDP(ProtocolHandler.packMessage(prefix, message));
    }
}
