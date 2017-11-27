package com.harium.etyl.networking.protocol.reliable;

import com.harium.etyl.networking.protocol.Protocol;
import com.harium.etyl.networking.test.DummyPeer;
import com.harium.etyl.networking.util.ByteMessageUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalMatchers;

import static org.mockito.Mockito.*;

public class ReliableHandlerTest {

    private static final byte SEP = ByteMessageUtils.SEPARATOR_BYTES[0];

    Protocol listener;
    ReliableHandler handler;

    @Before
    public void setUp() {
        listener = mock(Protocol.class);
        handler = new ReliableHandler(listener);
    }

    @Test
    public void testReceiveNotification() {
        DummyPeer server = new DummyPeer();
        byte[] hash = ByteMessageUtils.intToBytes(123);
        byte[] content = "1".getBytes();

        byte[] prefixedHash = ByteMessageUtils.concatenateMessage(ReliableHandler.PREFIX_MESSAGE, hash);
        byte[] text = ByteMessageUtils.concatenateMessage(prefixedHash, content);

        handler.receiveUDP(server, text);
        Assert.assertEquals(1, handler.earlyPackets.size());

        verify(listener, times(1)).receiveUDP(eq(server), AdditionalMatchers.aryEq(new byte[]{49}));
        verify(listener, times(1)).sendUDP(eq(server), AdditionalMatchers.aryEq(new byte[]{ReliableHandler.PREFIX_ACK[0], 32, hash[0], hash[1], hash[2], hash[3]}));
    }

    @Test
    public void testNotify() {
        byte[] text = "hi".getBytes();
        handler.notify(new DummyPeer(123), text);
        Assert.assertEquals(1, handler.queue.size());

        byte[] message = new byte[]{1, 0, 0, 0, SEP, ReliableHandler.PREFIX_MESSAGE[0], SEP, text[0], text[1]};
        Assert.assertArrayEquals(message, handler.queue.get(1).getMessage());
    }

    @Test
    public void testHandleHashKey() {
        handler.handleHashKey(1);
        Assert.assertEquals(1, handler.lastValidPacket);

        // Simulate packet loss (2)
        // Simulate packet loss (3)

        handler.handleHashKey(4);
        Assert.assertEquals(1, handler.earlyPackets.size());
        Assert.assertEquals(1, handler.lastValidPacket);

        // Packet 2 arrives
        handler.handleHashKey(2);
        Assert.assertEquals(1, handler.earlyPackets.size());
        Assert.assertEquals(2, handler.lastValidPacket);

        // Packet 3 arrives
        handler.handleHashKey(3);
        Assert.assertEquals(0, handler.earlyPackets.size());
        Assert.assertEquals(4, handler.lastValidPacket);
    }
}
