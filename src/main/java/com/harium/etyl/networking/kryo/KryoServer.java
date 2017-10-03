package com.harium.etyl.networking.kryo;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.harium.etyl.networking.model.BaseServer;
import com.harium.etyl.networking.model.ByteArrayKey;
import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.protocol.Protocol;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class KryoServer extends Server implements BaseServer {

    protected String name;
    protected int tcpPort = KryoEndpoint.UNDEFINED_PORT;
    protected int udpPort = KryoEndpoint.UNDEFINED_PORT;

    protected ServerHandler endPoint;
    protected Map<String, Peer> peers = new HashMap<>();

    public KryoServer() {
        super();

        KryoEndpoint.register(this);
        endPoint = new ServerHandler(this);
        addListener(endPoint);
    }

    public KryoServer(int tcpPort) {
        this();
        this.tcpPort = tcpPort;
    }

    public KryoServer(int tcpPort, int udpPort) {
        this(tcpPort);
        this.udpPort = udpPort;
    }

    @Override
    public void start() {
        try {
            if (udpPort == KryoEndpoint.UNDEFINED_PORT) {
                super.bind(tcpPort);
            } else {
                super.bind(tcpPort, udpPort);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        super.start();
    }

    protected Connection newConnection() {
        KryoPeer peer = new KryoPeer();
        peers.put(peer.getId(), peer);
        // By providing our own connection implementation, we can store per
        // connection state without a connection ID to state look up.
        return peer.getConnection();
    }

    public abstract void joinPeer(Peer peer);

    public abstract void leftPeer(Peer peer);

    @Override
    public void removePeer(String id) {
        peers.remove(id);
        /*for(Protocol protocol : protocols.values()) {
            protocol.removePeer(peer);
		}*/
    }

    /**
     * Adds the protocol with the default prefix
     *
     * @param prefix   - the prefix associated to the protocol
     * @param protocol - the protocol to respond by it's own prefix
     */
    public void addProtocol(byte[] prefix, Protocol protocol) {
        endPoint.addProtocol(prefix, protocol);
    }


    public void addProtocol(String prefix, Protocol protocol) {
        this.addProtocol(prefix.getBytes(), protocol);
    }

    public void setHandshaker(Protocol handshaker) {
        endPoint.handshaker = handshaker;
    }

    public Map<ByteArrayKey, Protocol> getProtocols() {
        return endPoint.protocols;
    }

    public Protocol getProtocol(String prefix) {
        return endPoint.getProtocol(prefix);
    }
}
