package com.harium.etyl.networking.kryo.udp;

import com.harium.etyl.networking.EtylClient;
import com.harium.etyl.networking.EtylServer;
import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.protocol.ProtocolHandler;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class UDPTest {

    public static final int TCP_PORT = 9901;
    public static final int UDP_PORT = 9902;

    static UDPServer server;

    @BeforeClass
    public static void setUp() {
        server = new UDPServer();
        server.start();
    }

    @Test
    public void testUDPConnection() {
        UDPClient client = new UDPClient();
        client.connect();
        waitServer();

        Assert.assertTrue(server.getCount() > 0);
    }

    private void waitServer() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public class UDPClient extends EtylClient {
        public UDPClient() {
            super(ProtocolHandler.LOCAL_HOST, TCP_PORT, UDP_PORT);
        }
    }

    public static class UDPServer extends EtylServer {
        int count = 0;

        public UDPServer() {
            super(TCP_PORT, UDP_PORT);
        }

        @Override
        public void joinPeer(Peer peer) {
            count++;
        }

        @Override
        public void leftPeer(Peer peer) {

        }

        public int getCount() {
            return count;
        }
    }

}
