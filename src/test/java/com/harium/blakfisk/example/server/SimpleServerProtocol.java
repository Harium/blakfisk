package com.harium.blakfisk.example.server;

import com.harium.blakfisk.BlakFiskServer;
import com.harium.blakfisk.model.Peer;
import com.harium.blakfisk.protocol.common.StringServerProtocol;

public class SimpleServerProtocol extends StringServerProtocol {

    public SimpleServerProtocol(String prefix, BlakFiskServer server) {
        super(prefix, server);
    }

    private boolean receivedTcp = false;

    @Override
    public void receiveUDP(Peer peer, String msg) {
        // TODO Auto-generated method stub
    }

    @Override
    public void receiveTCP(Peer peer, String msg) {
        System.out.println(getClass().getSimpleName() + " - Received TCP: " + msg);
        receivedTcp = true;

        sendTCPtoAll("Hallu " + peer.getID());
    }

    public boolean receivedTcp() {
        return receivedTcp;
    }

}
