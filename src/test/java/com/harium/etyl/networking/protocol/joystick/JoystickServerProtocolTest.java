package com.harium.etyl.networking.protocol.joystick;

import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.test.DummyPeer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class JoystickServerProtocolTest {

    Peer peer;
    JoystickListener listener;
    JoystickServerProtocol protocol;

    @Before
    public void setUp() {
        peer = new DummyPeer(999);
        listener = mock(JoystickListener.class);
        protocol = new JoystickServerProtocol("", null, listener);
        protocol.addPeer(peer);
        protocol.addPeer(new DummyPeer(1000));
    }

    @Test
    public void testAddPeer() {
        Assert.assertEquals(2, protocol.joysticks.size());
        Assert.assertFalse(getJoystick().getKeys()[0]);
    }

    @Test
    public void testReceiveUDP() {
        protocol.receiveUDP(peer, "1");
        Assert.assertTrue(getJoystick().getKeys()[0]);

        protocol.receiveUDP(peer, "0");
        Assert.assertFalse(getJoystick().getKeys()[0]);

        protocol.receiveUDP(peer, "3");
        Assert.assertTrue(getJoystick().getKeys()[0]);
        Assert.assertTrue(getJoystick().getKeys()[1]);

        verify(listener, times(2)).onPressed(peer.getId(), 0);
        verify(listener, times(1)).onReleased(peer.getId(), 0);
        verify(listener, times(1)).onPressed(peer.getId(), 1);
    }

    private Joystick getJoystick() {
        return protocol.joysticks.get(peer.getId());
    }

}
