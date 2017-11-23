package com.harium.etyl.networking.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Listener;
import com.harium.etyl.networking.model.BaseServer;
import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.model.data.ConnectionData;
import com.harium.etyl.networking.model.data.RawData;
import com.harium.etyl.networking.protocol.ProtocolHandler;

public class KryoEndpoint extends Listener {

    private BaseServer server;
    private final ProtocolHandler serverHandler;

    public KryoEndpoint(BaseServer server, ProtocolHandler serverHandler) {
        this.server = server;
        this.serverHandler = serverHandler;
    }

    @Override
    public void connected(Connection c) {
        KryoPeer peer = new KryoPeer(c);

        ((KryoServer) server).peers.put(peer.getId(), peer);
        server.onConnect(peer);
    }

    @Override
    public void received(Connection c, Object object) {
        Peer peer = server.getPeer(c.getID());
        if (object instanceof ConnectionData) {
            ConnectionData message = (ConnectionData) object;
            serverHandler.receiveMessageData(peer, message);

            return;
        }
    }

    @Override
    public void disconnected(Connection c) {
        if (!server.hasPeer(c.getID())) {
            return;
        }
        Peer peer = server.getPeer(c.getID());
        server.removePeer(c.getID());
        server.leftPeer(peer);
    }

    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(byte.class);
        kryo.register(byte[].class);
        kryo.register(RawData.class);
        kryo.register(ConnectionData.class);
    }

}
