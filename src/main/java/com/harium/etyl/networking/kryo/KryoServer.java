package com.harium.etyl.networking.kryo;

import com.esotericsoftware.kryonet.Server;
import com.harium.etyl.networking.model.BaseServer;
import com.harium.etyl.networking.model.ByteArrayKey;
import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.model.data.ConnectionData;
import com.harium.etyl.networking.model.data.RawData;
import com.harium.etyl.networking.protocol.Protocol;
import com.harium.etyl.networking.protocol.ProtocolHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class KryoServer extends Server implements BaseServer {

    protected String name;
    protected int tcpPort = ProtocolHandler.UNDEFINED_PORT;
    protected int udpPort = ProtocolHandler.UNDEFINED_PORT;

    protected KryoServerHandler serverHandler;
    protected Map<Integer, Peer> peers = new HashMap<>();

    public KryoServer(int tcpPort) {
        super();
        this.tcpPort = tcpPort;
        init();
    }

    public KryoServer(int tcpPort, int udpPort) {
        super();
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
        init();
    }

    private void init() {
        KryoEndpoint.register(this);
        serverHandler = new KryoServerHandler(this);
        addListener(serverHandler.getEndpoint());
    }

    @Override
    public void start() {
        try {
            if (udpPort == ProtocolHandler.UNDEFINED_PORT) {
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

    @Override
    public void onConnect(Peer peer) {
        serverHandler.addPeer(peer);
        joinPeer(peer);
    }

    @Override
    public void removePeer(int id) {
        Peer peer = getPeer(id);
        if (peer != null) {
            leftPeer(peer);
            serverHandler.removePeer(peer);
            peers.remove(id);
        }
    }

    @Override
    public Peer getPeer(int id) {
        return peers.get(id);
    }

    @Override
    public boolean hasPeer(int id) {
        return peers.containsKey(id);
    }

    public void setHandshaker(Protocol handshaker) {
        serverHandler.handshaker = handshaker;
    }

    /**
     * Adds the protocol with the default prefix
     *
     * @param prefix   - the prefix associated to the protocol
     * @param protocol - the protocol to respond by it's own prefix
     */
    public void addProtocol(byte[] prefix, Protocol protocol) {
        serverHandler.addProtocol(prefix, protocol);
    }

    public void addProtocol(String prefix, Protocol protocol) {
        this.addProtocol(prefix.getBytes(), protocol);
    }

    public void addProtocol(Protocol protocol) {
        this.addProtocol(protocol.getPrefix(), protocol);
    }

    public Map<ByteArrayKey, Protocol> getProtocols() {
        return serverHandler.getProtocols();
    }

    public Protocol getProtocol(String prefix) {
        return serverHandler.getProtocol(prefix);
    }

    @Override
    public void sendToTCP(int id, ConnectionData connectionData) {
        super.sendToTCP(id, connectionData);
    }

    @Override
    public void sendToTCP(int id, RawData rawData) {
        super.sendToTCP(id, rawData);
    }

    @Override
    public void sendToAllTCP(ConnectionData connectionData) {
        super.sendToAllTCP(connectionData);
    }

    @Override
    public void sendToAllExceptTCP(int id, ConnectionData connectionData) {
        super.sendToAllExceptTCP(id, connectionData);
    }

    @Override
    public void sendToUDP(int id, ConnectionData connectionData) {
        super.sendToUDP(id, connectionData);
    }

    @Override
    public void sendToUDP(int id, RawData rawData) {
        super.sendToUDP(id, rawData);
    }

    @Override
    public void sendToAllUDP(ConnectionData connectionData) {
        super.sendToAllUDP(connectionData);
    }

    @Override
    public void sendToAllExceptUDP(int id, ConnectionData connectionData) {
        super.sendToAllExceptUDP(id, connectionData);
    }

}
