package com.harium.etyl.networking.example.server;

import com.harium.etyl.networking.EtylServer;
import com.harium.etyl.networking.example.client.SimpleClientProtocol;
import com.harium.etyl.networking.model.BaseServer;
import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.protocol.HandshakerProtocol;

public class SimpleHandshaker extends HandshakerProtocol {

    public SimpleHandshaker(BaseServer server) {
        super(SimpleClientProtocol.DEFAULT_PREFIX, server);
    }

    @Override
    public String buildHandshake(Peer peer) {
        return " Hello, player " + peer.getId() + "!";
    }

}
