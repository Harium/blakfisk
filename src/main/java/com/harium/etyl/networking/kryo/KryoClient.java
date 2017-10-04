package com.harium.etyl.networking.kryo;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.harium.etyl.networking.model.data.ConnectionData;
import com.harium.etyl.networking.protocol.Protocol;
import com.harium.etyl.networking.protocol.ProtocolHandler;

import java.io.IOException;

public class KryoClient extends Client {

    protected int tcpPort = ProtocolHandler.UNDEFINED_PORT;
    protected int udpPort = ProtocolHandler.UNDEFINED_PORT;

    protected String host = ProtocolHandler.LOCAL_HOST;
    protected final KryoPeer SERVER = new KryoPeer(Integer.MIN_VALUE);

    ProtocolHandler protocolHandler = new ProtocolHandler();

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
                    protocolHandler.receiveMessageData(SERVER, message);

                    return;
                }
            }

            public void disconnected(Connection c) {
                System.out.println("Disconnected from Server");

                for (Protocol protocol : protocolHandler.getProtocols().values()) {
                    protocol.removePeer(SERVER);
                }
            }
        });
    }

    public void connect() {
        super.start();

        try {
            if (udpPort == ProtocolHandler.UNDEFINED_PORT) {
                super.connect(ProtocolHandler.DEFAULT_TIMEOUT, host, tcpPort);
            } else {
                super.connect(ProtocolHandler.DEFAULT_TIMEOUT, host, tcpPort, udpPort);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*protected Connection newConnection() {
        return new KryoPeer().getConnection();
    }*/

    /**
     * Adds the protocol with the default prefix
     *
     * @param prefix   - the prefix associated to the protocol
     * @param protocol - the protocol to respond by it's own prefix
     */
    public void addProtocol(byte[] prefix, Protocol protocol) {
        protocolHandler.addProtocol(prefix, protocol);
    }


    public void addProtocol(String prefix, Protocol protocol) {
        this.addProtocol(prefix.getBytes(), protocol);
    }

    public void addProtocol(Protocol protocol) {
        this.addProtocol(protocol.getPrefix(), protocol);
    }

}
