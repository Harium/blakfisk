package com.harium.etyl.networking.protocol.reliable;

import com.harium.etyl.networking.model.BaseClient;
import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.protocol.Protocol;
import com.harium.etyl.networking.protocol.common.ClientProtocol;
import com.harium.etyl.networking.util.ByteMessageUtils;

import java.util.ArrayList;
import java.util.List;

public class ReliableClientProtocol extends ClientProtocol {

    protected ReliableHandler handler;

    public ReliableClientProtocol(String prefix, BaseClient client, Protocol listener) {
        super(prefix, client);
        handler = new ReliableHandler(this, listener);
    }

    @Override
    public void receiveTCP(Peer peer, byte[] message) {
        handler.receiveTCP(peer,message);
    }

    @Override
    public void receiveUDP(Peer peer, byte[] message) {
        handler.receiveUDP(peer, message);
    }

}
