package com.harium.etyl.networking.core.protocol.reliable;

import com.harium.etyl.networking.core.model.BaseClient;
import com.harium.etyl.networking.core.protocol.Protocol;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class ReliableClientProtocolTest {

    private static final String PREFIX = "p";

    BaseClient client;
    Protocol listener;
    ReliableClientProtocol protocol;

    @Before
    public void setUp() {
        client = mock(BaseClient.class);
        listener = mock(Protocol.class);
        protocol = new ReliableClientProtocol(PREFIX, client, listener);
    }

    @Test
    public void testNotify() {
        Assert.assertEquals(0, protocol.handler.queue.get(Integer.MIN_VALUE).size());
        protocol.notify("Hello");

        // Force send messages
        protocol.tick();
        // Add messages to queue
        Assert.assertEquals(1, protocol.handler.queue.get(Integer.MIN_VALUE).size());
    }

}
