package com.harium.etyl.networking.protocol.reliable;

import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.protocol.Protocol;
import com.harium.etyl.networking.protocol.common.ProtocolImpl;
import com.harium.etyl.networking.test.DummyPeer;
import com.harium.etyl.networking.util.ByteMessageUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalMatchers;

import static org.mockito.Mockito.*;

public class ReliableHandlerTest {

    private static final byte SEP = ByteMessageUtils.SEPARATOR_BYTES[0];

    Protocol sender;
    Protocol listener;
    ReliableHandler handler;

    private static final byte[] CONTENT = "hi".getBytes();

    @Before
    public void setUp() {
        listener = mock(Protocol.class);
        sender = mock(Protocol.class);
        handler = new ReliableHandler(sender, listener);
    }

    @Test
    public void testReceiveNotification() {
        DummyPeer server = new DummyPeer();
        byte[] hash = ByteMessageUtils.shortToBytes((short)123);

        byte[] prefixedHash = ByteMessageUtils.concatenateMessage(ReliableHandler.PREFIX_MESSAGE, hash);
        byte[] text = ByteMessageUtils.concatenateMessage(prefixedHash, CONTENT);

        handler.receiveUDP(server, text);
        Assert.assertEquals(1, handler.earlyPackets.size());

        verify(listener, times(1)).receiveUDP(eq(server), AdditionalMatchers.aryEq(CONTENT));
        verify(sender, times(1)).sendUDP(eq(server), AdditionalMatchers.aryEq(new byte[]{ReliableHandler.PREFIX_ACK[0], 32, hash[0], hash[1]}));
    }

    @Test
    public void testNotify() {
        handler.notify(new DummyPeer(123), CONTENT);
        Assert.assertEquals(1, handler.queue.size());

        short packetId = 1;
        byte[] message = new byte[]{ReliableHandler.PREFIX_MESSAGE[0], SEP, 1, 0, SEP, CONTENT[0], CONTENT[1]};
        Assert.assertArrayEquals(message, handler.queue.get(packetId).getMessage());
    }

    @Test
    public void testNotifyAll() {
        handler.listener = otherListener;
        otherListener.addPeer(new DummyPeer(1));
        otherListener.addPeer(new DummyPeer(2));
        handler.notifyAll(CONTENT);

        Assert.assertEquals(2, handler.queue.size());
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

    Protocol otherListener = new ProtocolImpl(new byte[]{1}) {
        @Override
        public void receiveTCP(Peer peer, byte[] message) {

        }

        @Override
        public void receiveUDP(Peer peer, byte[] message) {

        }

        @Override
        public void sendTCP(Peer peer, byte[] message) {

        }

        @Override
        public void sendUDP(Peer peer, byte[] message) {

        }

        @Override
        public void sendWebSocket(Peer peer, byte[] message) {

        }
    };
}
