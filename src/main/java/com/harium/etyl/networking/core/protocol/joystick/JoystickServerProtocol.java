package com.harium.etyl.networking.core.protocol.joystick;

import com.harium.etyl.networking.core.model.BaseServer;
import com.harium.etyl.networking.core.model.Peer;
import com.harium.etyl.networking.core.protocol.common.StringServerProtocol;

import java.util.HashMap;
import java.util.Map;

public class JoystickServerProtocol extends StringServerProtocol {

    private int lastValue = JoystickProtocolUtils.UNDEFINED_VALUE;

    protected int size = Joystick.SIZE;
    Map<Integer, Joystick> joysticks;
    JoystickListener listener;

    public JoystickServerProtocol(String prefix, BaseServer server, JoystickListener listener) {
        super(prefix, server);

        this.listener = listener;
        joysticks = new HashMap<>();
    }

    @Override
    public void addPeer(Peer peer) {
        super.addPeer(peer);
        joysticks.put(peer.getId(), new Joystick(size));
    }

    @Override
    public void removePeer(Peer peer) {
        super.removePeer(peer);
        joysticks.remove(peer.getId());
    }

    @Override
    protected void receiveTCP(Peer peer, String message) {
        handleMessage(peer, message);
        sendTCP(peer, Integer.toString(lastValue));
    }

    @Override
    protected void receiveUDP(Peer peer, String message) {
        handleMessage(peer, message);
        sendUDP(peer, Integer.toString(lastValue));
    }

    private void handleMessage(Peer peer, String message) {
        Joystick joystick = getJoystick(peer);

        // It can be turned into byte array
        int value = Integer.parseInt(message);
        boolean[] array = JoystickProtocolUtils.intToBoolean(value, joystick.getSize());

        int index = 0;
        for (boolean key : array) {
            if (key) {
                if (joystick.press(index)) {
                    listener.onPressed(peer.getId(), index);
                }
            } else {
                if (joystick.release(index)) {
                    listener.onReleased(peer.getId(), index);
                }
            }
            index++;
        }
        lastValue = value;
    }

    private Joystick getJoystick(Peer peer) {
        return joysticks.get(peer.getId());
    }

}
