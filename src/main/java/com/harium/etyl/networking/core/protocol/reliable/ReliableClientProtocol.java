package com.harium.etyl.networking.core.protocol.reliable;

import com.harium.etyl.networking.core.model.BaseClient;
import com.harium.etyl.networking.core.model.Peer;
import com.harium.etyl.networking.core.protocol.Protocol;
import com.harium.etyl.networking.core.protocol.common.ClientProtocol;

public class ReliableClientProtocol extends ClientProtocol {

    protected ReliableHandler handler;

    public ReliableClientProtocol(String prefix, BaseClient client, Protocol listener) {
        super(prefix, client);
        handler = new ReliableHandler(this, listener);
        handler.addPeer(ClientProtocol.SERVER);
    }

    public void notify(String message) {
        notify(message.getBytes());
    }

    public void notify(byte[] message) {
        handler.notify(SERVER, message);
    }

    @Override
    public void receiveTCP(Peer peer, byte[] message) {
        handler.receiveTCP(peer,message);
    }

    @Override
    public void receiveUDP(Peer peer, byte[] message) {
        handler.receiveUDP(peer, message);
    }

    @Override
    public void tick() {
        super.tick();
        handler.dispatch();
    }
}
