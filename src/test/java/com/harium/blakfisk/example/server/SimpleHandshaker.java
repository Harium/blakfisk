package com.harium.blakfisk.example.server;

import com.harium.blakfisk.BlakFiskServer;
import com.harium.blakfisk.example.client.SimpleClientProtocol;
import com.harium.blakfisk.model.Peer;
import com.harium.blakfisk.protocol.HandshakerProtocol;

public class SimpleHandshaker extends HandshakerProtocol {

    public SimpleHandshaker(BlakFiskServer server) {
        super(SimpleClientProtocol.DEFAULT_PREFIX, server);
    }

    @Override
    public String handshakeText(Peer peer) {
        return " Hello, player " + peer.getID() + "!";
    }

}
