package com.harium.etyl.networking.server;

import com.harium.etyl.networking.EtylClient;
import com.harium.etyl.networking.kryo.KryoClient;
import com.harium.etyl.networking.example.server.HandShakeServer;
import com.harium.etyl.networking.example.server.SimpleServerProtocol;
import com.harium.etyl.networking.protocol.ProtocolHandler;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TCPServerTest extends TestCase {

    private static final String IP = ProtocolHandler.LOCAL_HOST;
    private static final int PORT = 10222;

    private String LISTENER_PREFIX = "/s";
    private HandShakeServer server;

    @Before
    public void setUp() {
        server = new HandShakeServer(PORT);

        SimpleServerProtocol listener = new SimpleServerProtocol(LISTENER_PREFIX, server);

        server.addProtocol(listener.getPrefix(), listener);
        server.start();
    }

    @Test
    public void testUserConnected() throws Exception {
        EtylClient client = new EtylClient(IP, PORT);
        client.connect();
        Thread.sleep(1000);

        Assert.assertEquals(1, server.getConnections().length);
        client.close();
        Thread.sleep(1000);
        Assert.assertEquals(0, server.getConnections().length);
        server.close();
    }

}