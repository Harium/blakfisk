package com.harium.etyl.networking.core.protocol.reliable;

import com.harium.etyl.networking.core.helper.ByteHelper;
import com.harium.etyl.networking.core.helper.ByteMessageHelper;
import com.harium.etyl.networking.core.model.Packet;
import com.harium.etyl.networking.core.model.Peer;
import com.harium.etyl.networking.core.protocol.Protocol;
import com.harium.etyl.networking.core.protocol.common.ProtocolImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalMatchers;
import test.DummyPeer;

import static org.mockito.Mockito.*;

public class ReliableHandlerTest {

    private static final byte SEP = ByteMessageHelper.SEPARATOR_BYTES[0];

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
        byte[] hash = ByteHelper.shortToBytes((short) 123);

        byte[] prefixedHash = ByteMessageHelper.concatenateMessage(ReliableHandler.PREFIX_MESSAGE, hash);
        byte[] text = ByteMessageHelper.concatenateMessage(prefixedHash, CONTENT);

        handler.receiveUDP(server, text);
        Assert.assertEquals(1, handler.earlyPackets.size());

        verify(listener, times(0)).receiveUDP(eq(server), AdditionalMatchers.aryEq(CONTENT));
        verify(sender, times(1)).sendUDP(eq(server), AdditionalMatchers.aryEq(new byte[]{ReliableHandler.PREFIX_ACK[0], 32, hash[0], hash[1]}));

        // Offset lastValidPacket
        handler.lastValidPacket = 122;
        handler.receiveUDP(server, text);
        verify(listener, times(1)).receiveUDP(eq(server), AdditionalMatchers.aryEq(CONTENT));
    }

    @Test
    public void testNotify() {
        handler.notify(new DummyPeer(123), CONTENT);
        Assert.assertEquals(1, handler.queue.size());

        byte[] message = new byte[]{ReliableHandler.PREFIX_MESSAGE[0], SEP, 1, 0, SEP, CONTENT[0], CONTENT[1]};
        Assert.assertArrayEquals(message, handler.queue.get((short) 1).getMessage());
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
    public void testHandlePacket() {
        Packet packet1 = buildPacket((short) 1, "1");

        handler.handlePacket(packet1);
        verify(listener, times(1)).receiveUDP(any(Peer.class), eq("1".getBytes()));
        Assert.assertEquals(1, handler.lastValidPacket);

        // Simulate packet loss (2)
        // Simulate packet loss (3)

        Packet packet4 = buildPacket((short) 4, "4");

        handler.handlePacket(packet4);
        // Persisted packet 4
        verify(listener, times(1)).receiveUDP(any(Peer.class), any(byte[].class));
        Assert.assertEquals(1, handler.earlyPackets.size());
        Assert.assertEquals(1, handler.lastValidPacket);

        Packet packet2 = buildPacket((short) 2, "2");
        // Packet 2 arrives
        handler.handlePacket(packet2);
        // Received packet 2
        verify(listener, times(2)).receiveUDP(any(Peer.class), any(byte[].class));
        Assert.assertEquals(1, handler.earlyPackets.size());
        Assert.assertEquals(2, handler.lastValidPacket);

        // Packet 3 arrives
        Packet packet3 = buildPacket((short) 3, "3");
        handler.handlePacket(packet3);
        // Received packet 3 and 4
        verify(listener, times(4)).receiveUDP(any(Peer.class), any(byte[].class));
        Assert.assertEquals(0, handler.earlyPackets.size());
        Assert.assertEquals(4, handler.lastValidPacket);
    }

    private Packet buildPacket(short id, String message) {
        Packet packet = new Packet(null, message.getBytes());
        packet.setId(id);
        return packet;
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
