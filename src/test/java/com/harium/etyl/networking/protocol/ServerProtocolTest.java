package com.harium.etyl.networking.protocol;

import com.harium.etyl.networking.EtylServer;
import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.model.data.ConnectionData;
import com.harium.etyl.networking.model.data.ConnectionType;
import com.harium.etyl.networking.protocol.common.ServerProtocol;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ServerProtocolTest {

    private static final int PORT = 1111;
    private static final byte[] PREFIX = "PREFIX".getBytes();

    ServerProtocol protocol;
    TestServer server;

    @Before
    public void setUp() {
        server = new TestServer();
        protocol = new ServerProtocol(PREFIX, server) {
            @Override
            public void receiveTCP(Peer peer, byte[] message) {

            }

            @Override
            public void receiveUDP(Peer peer, byte[] message) {

            }
        };
    }

    @Test
    public void testSendTCPConnectionType() {
        byte[] message = new byte[0];
        protocol.sendTCP(1, message);

        Assert.assertEquals(ConnectionType.TCP, server.lastData.connectionType);
    }

    @Test
    public void testSendUDPConnectionType() {
        byte[] message = new byte[0];
        protocol.sendUDP(1, message);

        Assert.assertEquals(ConnectionType.UDP, server.lastData.connectionType);
    }

    class TestServer extends EtylServer {

        public ConnectionData lastData;

        public TestServer() {
            super(PORT);
        }

        @Override
        public void joinPeer(Peer peer) {

        }

        @Override
        public void leftPeer(Peer peer) {

        }

        @Override
        public void sendToTCP(int id, ConnectionData connectionData) {
            lastData = connectionData;
        }

        @Override
        public void sendToUDP(int id, ConnectionData connectionData) {
            lastData = connectionData;
        }

    }
}
