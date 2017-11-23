package com.harium.etyl.networking.protocol.notification;

import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.protocol.common.ClientProtocol;
import com.harium.etyl.networking.test.DummyClient;
import com.harium.etyl.networking.test.DummyPeer;
import com.harium.etyl.networking.util.ByteMessageUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NotificationClientProtocolTest {

    NotificationListener listener;
    NotificationClientProtocol protocol;

    @Before
    public void setUp() {
        listener = new NotificationListener();
        protocol = new NotificationClientProtocol("PREFIX", new DummyClient(1), listener);
    }

    @Test
    public void testReceiveNotification() {
        DummyPeer server = new DummyPeer();
        byte[] hash = ByteMessageUtils.intToBytes(123);
        byte[] message = "message".getBytes();

        protocol.receiveUDP(server, ByteMessageUtils.concatenateMessage(hash, message));
        Assert.assertEquals(1, protocol.lastHashes.size());
        Assert.assertTrue(listener.received);
    }

    class NotificationListener extends ClientProtocol {

        boolean received = false;

        public NotificationListener() {
            super("", null);
        }

        @Override
        public void receiveTCP(Peer peer, byte[] message) {

        }

        @Override
        public void receiveUDP(Peer peer, byte[] message) {
            received = true;
        }
    }
}
