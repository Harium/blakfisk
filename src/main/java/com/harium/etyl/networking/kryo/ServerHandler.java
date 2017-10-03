package com.harium.etyl.networking.kryo;

import com.harium.etyl.networking.model.BaseServer;
import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.model.data.ConnectionData;
import com.harium.etyl.networking.protocol.NullProtocol;
import com.harium.etyl.networking.protocol.Protocol;
import com.esotericsoftware.kryonet.Connection;

public class ServerHandler extends KryoEndpoint {

    private BaseServer server;

    //TODO It is really needed?
    private static final Protocol NULL_HANDSHAKER = new NullProtocol();
    public Protocol handshaker = NULL_HANDSHAKER;

    public ServerHandler(BaseServer server) {
        super();
        this.server = server;
    }

    @Override
    public void connected(Connection c) {
        Peer peer = server.getPeer(Integer.toString(c.getID()));
        server.joinPeer(peer);
        handshaker.addPeer(peer);
    }

    @Override
    public void received(Connection c, Object object) {
        Peer peer = server.getPeer(Integer.toString(c.getID()));
        if (object instanceof ConnectionData) {

            ConnectionData message = (ConnectionData) object;
            receiveMessageData(peer, message);

            return;
        }
    }

    @Override
    public void disconnected(Connection c) {
        Peer peer = server.getPeer(Integer.toString(c.getID()));
        server.removePeer(Integer.toString(c.getID()));
        server.leftPeer(peer);
    }

}
