package com.harium.etyl.networking.example.server;

import com.harium.etyl.networking.EtylServer;
import com.harium.etyl.networking.example.client.SimpleClientProtocol;
import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.protocol.HandshakerProtocol;

public class SimpleHandshaker extends HandshakerProtocol {

    public SimpleHandshaker(EtylServer server) {
        super(SimpleClientProtocol.DEFAULT_PREFIX, server);
    }

    @Override
    public String handshakeText(Peer peer) {
        return " Hello, player " + peer.getID() + "!";
    }

}
