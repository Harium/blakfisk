package com.harium.etyl.networking.protocol.notification;

import com.harium.etyl.networking.test.DummyPeer;
import com.harium.etyl.networking.test.DummyServer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NotificationServerProtocolTest {

    NotificationServerProtocol protocol;

    @Before
    public void setUp() {
        protocol = new NotificationServerProtocol("PREFIX", new DummyServer(1));
    }

    @Test
    public void testSendNotification() {
        protocol.sendNotification(new DummyPeer(123), "hi");
        Assert.assertEquals(1, protocol.notifications.size());
    }

}
