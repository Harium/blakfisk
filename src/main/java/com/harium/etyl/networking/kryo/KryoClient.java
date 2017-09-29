package com.harium.etyl.networking.kryo;

import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.model.data.ConnectionData;
import com.harium.etyl.networking.protocol.Protocol;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

public class KryoClient extends Client {

    protected int tcpPort = KryoEndpoint.UNDEFINED_PORT;
    protected int udpPort = KryoEndpoint.UNDEFINED_PORT;

    protected String host = KryoEndpoint.LOCAL_HOST;
    protected final Peer SERVER = new Peer();

    KryoEndpoint kryoEndpoint = new KryoEndpoint();

    public KryoClient(String host, int tcpPort) {
        this();
        this.host = host;
        this.tcpPort = tcpPort;
    }

    public KryoClient(String host, int tcpPort, int udpPort) {
        this(host, tcpPort);

        this.udpPort = udpPort;
    }

    public KryoClient() {
        super();

        KryoEndpoint.register(this);
        addListener(new Listener() {
            public void received(Connection c, Object object) {
                if (object instanceof ConnectionData) {
                    ConnectionData message = (ConnectionData) object;
                    kryoEndpoint.receiveMessageData(SERVER, message);

                    return;
                }
            }

            public void disconnected(Connection c) {
                System.out.println("Disconnected from: " + SERVER.getID());

                for (Protocol protocol : kryoEndpoint.protocols.values()) {
                    protocol.removePeer(SERVER);
                }
            }
        });
    }

    public void connect() {
        super.start();

        try {
            if (udpPort == KryoEndpoint.UNDEFINED_PORT) {
                super.connect(KryoEndpoint.DEFAULT_TIMEOUT, host, tcpPort);
            } else {
                super.connect(KryoEndpoint.DEFAULT_TIMEOUT, host, tcpPort, udpPort);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected Connection newConnection() {
        return new Peer();
    }

    /**
     * Adds the protocol with the default prefix
     *
     * @param prefix   - the prefix associated to the protocol
     * @param protocol - the protocol to respond by it's own prefix
     */
    public void addProtocol(byte[] prefix, Protocol protocol) {
        kryoEndpoint.addProtocol(prefix, protocol);
    }


    public void addProtocol(String prefix, Protocol protocol) {
        this.addProtocol(prefix.getBytes(), protocol);
    }

    public void addProtocol(Protocol protocol) {
        this.addProtocol(protocol.getPrefix(), protocol);
    }

}
