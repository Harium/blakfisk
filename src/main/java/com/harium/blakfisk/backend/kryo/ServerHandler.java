package com.harium.blakfisk.backend.kryo;

import com.harium.blakfisk.model.Peer;
import com.harium.blakfisk.model.data.ConnectionData;
import com.harium.blakfisk.protocol.NullProtocol;
import com.harium.blakfisk.protocol.Protocol;
import com.esotericsoftware.kryonet.Connection;

public class ServerHandler extends KryoEndpoint {

    private KryoServer server;

    //TODO It is really needed?
    private static final Protocol NULL_HANDSHAKER = new NullProtocol();
    public Protocol handshaker = NULL_HANDSHAKER;

    public ServerHandler(KryoServer server) {
        super();
        this.server = server;
    }

    @Override
    public void connected(Connection c) {
        Peer peer = (Peer) c;
        server.joinPeer(peer);
        handshaker.addPeer(peer);
    }

    @Override
    public void received(Connection c, Object object) {
        Peer peer = (Peer) c;
        if (object instanceof ConnectionData) {

            ConnectionData message = (ConnectionData) object;
            receiveMessageData(peer, message);

            return;
        }
    }

    @Override
    public void disconnected(Connection c) {
        Peer peer = (Peer) c;
        server.leftPeer(peer);

		/*for(Protocol protocol : protocols.values()) {
			protocol.removePeer(peer);
		}*/
    }

}
