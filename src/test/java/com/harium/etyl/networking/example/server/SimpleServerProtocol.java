package com.harium.etyl.networking.example.server;

import com.harium.etyl.networking.EtylServer;
import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.protocol.common.StringServerProtocol;

public class SimpleServerProtocol extends StringServerProtocol {

    public SimpleServerProtocol(String prefix, EtylServer server) {
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
