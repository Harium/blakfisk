package com.harium.etyl.networking.kryo;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.harium.etyl.networking.EtylClient;
import com.harium.etyl.networking.model.BaseClient;
import com.harium.etyl.networking.model.data.ConnectionData;
import com.harium.etyl.networking.model.data.RawData;
import com.harium.etyl.networking.protocol.Protocol;
import com.harium.etyl.networking.protocol.ProtocolHandler;

import java.io.IOException;

public class KryoClient extends Client implements BaseClient {

    protected int tcpPort = ProtocolHandler.UNDEFINED_PORT;
    protected int udpPort = ProtocolHandler.UNDEFINED_PORT;

    protected String host = ProtocolHandler.LOCAL_HOST;
    public static final KryoPeer SERVER = new KryoPeer(Integer.MIN_VALUE);

    protected ProtocolHandler protocolHandler = new ProtocolHandler();

    public KryoClient(String host, int tcpPort) {
        super();
        this.host = host;
        this.tcpPort = tcpPort;
        init();
    }

    public KryoClient(String host, int tcpPort, int udpPort) {
        super();
        this.host = host;
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
        init();
    }

    private void init() {
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
        protocolHandler.addPeer(SERVER);
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

    @Override
    public void sendToTCP(ConnectionData connectionData) {
        sendTCP(connectionData);
    }

    @Override
    public void sendToTCP(RawData rawData) {
        sendTCP(rawData);
    }

    @Override
    public void sendToUDP(ConnectionData connectionData) {
        sendUDP(connectionData);
    }

    @Override
    public void sendToUDP(RawData rawData) {
        sendUDP(rawData);
    }

    @Override
    public void sendToWebSocket(ConnectionData connectionData) {
        sendTCP(connectionData);
    }

    @Override
    public void sendToWebSocket(RawData rawData) {
        sendTCP(rawData);
    }
}
