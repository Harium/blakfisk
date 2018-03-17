package com.harium.etyl.networking.core.protocol.common;

import com.harium.etyl.networking.core.model.BaseServer;
import com.harium.etyl.networking.core.model.Peer;
import com.harium.etyl.networking.core.model.data.ConnectionData;
import com.harium.etyl.networking.core.model.data.ConnectionType;
import com.harium.etyl.networking.core.model.data.RawData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ServerProtocolTest {

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

    class TestServer implements BaseServer {

        public ConnectionData lastData;

        public TestServer() {
            super();
        }

        @Override
        public void onConnect(Peer peer) {

        }

        @Override
        public void joinPeer(Peer peer) {

        }

        @Override
        public void leftPeer(Peer peer) {

        }

        @Override
        public boolean hasPeer(int id) {
            return false;
        }

        @Override
        public Peer getPeer(int id) {
            return null;
        }

        @Override
        public void removePeer(int id) {

        }

        @Override
        public void sendToTCP(int id, ConnectionData connectionData) {
            lastData = connectionData;
        }

        @Override
        public void sendToTCP(int id, RawData rawData) {

        }

        @Override
        public void sendToAllTCP(ConnectionData connectionData) {

        }

        @Override
        public void sendToAllExceptTCP(int id, ConnectionData connectionData) {

        }

        @Override
        public void sendToUDP(int id, ConnectionData connectionData) {
            lastData = connectionData;
        }

        @Override
        public void sendToUDP(int id, RawData rawData) {

        }

        @Override
        public void sendToAllUDP(ConnectionData connectionData) {

        }

        @Override
        public void sendToAllExceptUDP(int id, ConnectionData connectionData) {

        }

    }
}
