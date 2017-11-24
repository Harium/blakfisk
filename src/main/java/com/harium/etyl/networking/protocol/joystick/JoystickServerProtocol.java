package com.harium.etyl.networking.protocol.joystick;

import com.harium.etyl.networking.model.BaseServer;
import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.protocol.common.StringServerProtocol;

import java.util.HashMap;
import java.util.Map;

public class JoystickServerProtocol extends StringServerProtocol {

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
        joysticks.put(peer.getId(), new Joystick());
    }

    @Override
    public void removePeer(Peer peer) {
        super.removePeer(peer);
        joysticks.remove(peer.getId());
    }

    @Override
    protected void receiveTCP(Peer peer, String message) {
        handleMessage(peer, message);
    }

    @Override
    protected void receiveUDP(Peer peer, String message) {
        handleMessage(peer, message);
    }

    private void handleMessage(Peer peer, String message) {
        Joystick joystick = getJoystick(peer);

        // It can be turned into byte array
        int value = Integer.parseInt(message);
        boolean[] array = JoystickProtocolUtils.intToBoolean(value, Joystick.SIZE);

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
    }

    private Joystick getJoystick(Peer peer) {
        return joysticks.get(peer.getId());
    }

}
