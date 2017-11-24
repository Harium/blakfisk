package com.harium.etyl.networking.protocol.joystick;

import com.harium.etyl.networking.model.BaseClient;
import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.protocol.common.StringClientProtocol;

import java.util.HashMap;
import java.util.Map;

public class JoystickClientProtocol extends StringClientProtocol implements Runnable {

    int mappedKeys = 0;

    Joystick joystick;
    Map<Integer, Integer> keyMap;

    public JoystickClientProtocol(String prefix, BaseClient client) {
        super(prefix, client);
        int size = Joystick.SIZE;
        joystick = new Joystick(size);
        keyMap = new HashMap<>(joystick.getSize());
        initKeyMap();
    }

    private void initKeyMap() {
        for (int i = 0; i < joystick.getSize(); i++) {
            keyMap.put(i, i);
        }
    }

    @Override
    protected void receiveTCP(Peer peer, String message) {
    }

    @Override
    protected void receiveUDP(Peer peer, String message) {
    }

    /**
     * Method to dispatch joystick's state as message over UDP
     */
    public void sendCommands() {
        int value = JoystickProtocolUtils.booleanToInt(joystick.getKeys());
        // It can be turned into byte array
        sendUDP(Integer.toString(value));
    }

    /**
     * Method to map a key to an index
     *
     * @param key   the original int value
     * @param index the array index
     */
    public void mapKey(int key, int index) {
        keyMap.put(key, index);
        mappedKeys++;
        mappedKeys %= joystick.getSize();
    }

    /**
     * Method to map a key to an index
     *
     * @param key the original int value
     */
    public void mapKey(int key) {
        keyMap.put(key, mappedKeys);
        mappedKeys++;
        mappedKeys %= joystick.getSize();
    }

    /**
     * Method to be called when the real key is pressed
     *
     * @param key the pressed key
     */
    public void press(int key) {
        int index = keyIndex(key);
        joystick.press(index);
    }

    /**
     * Method to be called when the real key is released
     *
     * @param key the released key
     */
    public void release(int key) {
        int index = keyIndex(key);
        joystick.release(index);
    }

    /**
     * Method to check the index associated to the key (may return null if key is not mapped)
     *
     * @param key the mapped key
     * @return the mapped index
     */
    public int keyIndex(int key) {
        return keyMap.get(key);
    }

    /**
     * Method to check if the key is mapped
     *
     * @param key the key to check
     * @return key is mapped
     */
    public boolean hasKey(int key) {
        return keyMap.containsKey(key);
    }

    @Override
    public void run() {
        sendCommands();
    }

}
