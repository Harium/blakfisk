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

import java.util.Map;

import static org.mockito.Mockito.*;

public class ReliableHandlerTest {

    private static final byte SEP = ByteMessageHelper.SEPARATOR_BYTES[0];

    DummyPeer server;

    Protocol sender;
    Protocol listener;
    ReliableHandler handler;

    private static final byte[] CONTENT = "hi".getBytes();

    @Before
    public void setUp() {
        server = new DummyPeer();
        listener = mock(Protocol.class);
        sender = mock(Protocol.class);
        handler = new ReliableHandler(sender, listener);
        handler.addPeer(server);
    }

    @Test
    public void testReceiveNotification() {
        byte[] hash = ByteHelper.shortToBytes((short) 123);

        byte[] prefixedHash = ByteMessageHelper.concatenateMessage(ReliableHandler.PREFIX_MESSAGE, hash);
        byte[] text = ByteMessageHelper.concatenateMessage(prefixedHash, CONTENT);

        handler.addPeer(server);
        Assert.assertEquals((short) 0, (short) handler.inIndexes.get(server.getId()));

        handler.receiveUDP(server, text);
        // Ignore the package because of the order
        verify(listener, times(0)).receiveUDP(eq(server), AdditionalMatchers.aryEq(CONTENT));
        // Send ACK after receive the message
        verify(sender, times(1)).sendUDP(eq(server), AdditionalMatchers.aryEq(new byte[]{ReliableHandler.PREFIX_ACK[0], 32, hash[0], hash[1]}));

        // Offset lastValidPacket
        handler.inIndexes.put(server.getId(), (short) 122);
        handler.receiveUDP(server, text);
        verify(listener, times(1)).receiveUDP(eq(server), AdditionalMatchers.aryEq(CONTENT));
    }

    @Test
    public void testNotify() {
        Peer peer = new DummyPeer(123);

        handler.addPeer(peer);
        Assert.assertEquals(2, handler.queue.size());

        handler.notify(peer, CONTENT);
        Assert.assertEquals((short) 1, (short) handler.outIndexes.get(peer.getId()));

        byte[] message = new byte[]{ReliableHandler.PREFIX_MESSAGE[0], SEP, 1, 0, SEP, CONTENT[0], CONTENT[1]};
        Assert.assertArrayEquals(message, handler.queue.get(peer.getId()).get((short) 1).getMessage());
    }

    @Test
    public void testNotifyMultiple() {
        Peer peer = new DummyPeer(123);
        Peer otherPeer = new DummyPeer(124);

        handler.addPeer(peer);
        handler.addPeer(otherPeer);

        Assert.assertEquals(3, handler.queue.size());
        Assert.assertEquals((short) 0, (short) handler.outIndexes.get(peer.getId()));
        Assert.assertEquals((short) 0, (short) handler.inIndexes.get(peer.getId()));
        Assert.assertEquals((short) 0, (short) handler.outIndexes.get(otherPeer.getId()));
        Assert.assertEquals((short) 0, (short) handler.inIndexes.get(otherPeer.getId()));

        handler.notify(peer, CONTENT);
        Assert.assertEquals((short) 1, (short) handler.outIndexes.get(peer.getId()));
        // Keep the same otherPeer outIndex
        Assert.assertEquals((short) 0, (short) handler.outIndexes.get(otherPeer.getId()));

        byte[] message = new byte[]{ReliableHandler.PREFIX_MESSAGE[0], SEP, 1, 0, SEP, CONTENT[0], CONTENT[1]};
        Assert.assertArrayEquals(message, handler.queue.get(peer.getId()).get((short) 1).getMessage());

        handler.notify(otherPeer, CONTENT);
        // Update otherPeer outIndex
        Assert.assertEquals((short) 1, (short) handler.outIndexes.get(otherPeer.getId()));
    }

    @Test
    public void testNotifyAll() {
        Peer peer1 = new DummyPeer(1);
        Peer peer2 = new DummyPeer(2);

        handler.listener = otherListener;
        otherListener.addPeer(peer1);
        otherListener.addPeer(peer2);
        handler.addPeer(peer1);
        handler.addPeer(peer2);
        handler.notifyAll(CONTENT);

        // Server + peer1 + peer2
        Assert.assertEquals(3, handler.queue.size());
        Assert.assertEquals((short) 1, (short) handler.outIndexes.get(peer1.getId()));
        Assert.assertEquals((short) 1, (short) handler.outIndexes.get(peer2.getId()));
    }

    @Test
    public void testHandlePacket() {
        Packet packet1 = buildPacket((short) 1, "1");

        handler.handlePacket(packet1);
        verify(listener, times(1)).receiveUDP(any(Peer.class), eq("1".getBytes()));
        Assert.assertEquals((short) 1, (short) handler.inIndexes.get(server.getId()));

        // Simulate packet loss (2)
        // Simulate packet loss (3)

        Packet packet4 = buildPacket((short) 4, "4");

        handler.handlePacket(packet4);
        // Ignore packet 4
        verify(listener, times(1)).receiveUDP(any(Peer.class), any(byte[].class));
        Assert.assertEquals((short) 1, (short) handler.inIndexes.get(server.getId()));

        Packet packet2 = buildPacket((short) 2, "2");
        // Packet 2 arrives
        handler.handlePacket(packet2);
        // Received packet 2
        verify(listener, times(2)).receiveUDP(any(Peer.class), any(byte[].class));
        Assert.assertEquals((short) 2, (short) handler.inIndexes.get(server.getId()));

        // Packet 3 arrives
        Packet packet3 = buildPacket((short) 3, "3");
        handler.handlePacket(packet3);
        // Received packet 3
        verify(listener, times(3)).receiveUDP(any(Peer.class), any(byte[].class));
        Assert.assertEquals((short) 3, (short) handler.inIndexes.get(server.getId()));
    }

    @Test
    public void testSendMessages() {
        handler.notify(server, "Hello".getBytes());

        Map<Short, Packet> packets = handler.queue.get(server.getId());
        Assert.assertEquals(1, packets.size());

        // Force send messages
        handler.dispatch();
        Assert.assertEquals(1, packets.size());
    }

    private Packet buildPacket(short id, String message) {
        Packet packet = new Packet(server, message.getBytes());
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
