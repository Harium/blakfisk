package com.harium.etyl.networking.protocol.reliable;

import com.harium.etyl.networking.EtylClient;
import com.harium.etyl.networking.EtylServer;
import com.harium.etyl.networking.dummy.DummyProtocol;
import com.harium.etyl.networking.protocol.Protocol;
import com.harium.etyl.networking.test.DummyPeer;
import com.harium.etyl.networking.util.ByteMessageUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalMatchers;

import static org.mockito.Mockito.*;

public class ReliableHandlerIntegrationTest {

    private static final String PREFIX = "XUDP";
    private static final String HOST = "localhost";

    EtylServer server;
    EtylClient client;

    DummyProtocol clientProtocol;

    @Before
    public void setUp() {
        server = new EtylServer(1234, 4321);
        server.start();
        server.addProtocol(new ReliableServerProtocol(PREFIX, server, new DummyProtocol()));

        clientProtocol = new DummyProtocol();

        client = new EtylClient(HOST, 1234, 4321);
        client.addProtocol(new ReliableClientProtocol(PREFIX, client, clientProtocol));
        client.connect();
    }

    @Test
    public void testInit() {
        Assert.assertNotNull(client.getProtocol(PREFIX));
    }

}
