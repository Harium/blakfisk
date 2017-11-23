package com.harium.etyl.networking.protocol.notification;

import com.harium.etyl.networking.model.BaseClient;
import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.protocol.common.ClientProtocol;
import com.harium.etyl.networking.util.ByteMessageUtils;

import java.util.ArrayList;
import java.util.List;

public class NotificationClientProtocol extends ClientProtocol {

    private static final int MAX_SIZE = 16;
    private ClientProtocol listener;

    List<Integer> lastHashes = new ArrayList<>();

    public NotificationClientProtocol(String prefix, BaseClient client, ClientProtocol listener) {
        super(prefix, client);
        this.listener = listener;
        if (listener == null) {
            throw new NullPointerException("Listener cannot be null");
        }
    }

    @Override
    public void receiveTCP(Peer peer, byte[] message) {
        listener.receiveTCP(peer, message);
    }

    @Override
    public void receiveUDP(Peer peer, byte[] message) {
        byte[] hashId = notifyListener(peer, message);
        byte[] response = ByteMessageUtils.concatenateMessage(NotificationServerProtocol.PREFIX_ACK.getBytes(), hashId);
        sendUDP(response);
    }

    private byte[] notifyListener(Peer peer, byte[] message) {
        byte[] hashId = ByteMessageUtils.getPrefix(message);
        byte[] text = ByteMessageUtils.wipePrefix(hashId, message);
        int id = ByteMessageUtils.bytesToInt(hashId);
        // Avoid multiples calls to receiveUDP with the same message
        if (!lastHashes.contains(id)) {
            // Handle text
            listener.receiveUDP(peer, text);
            lastHashes.add(id);

            if (lastHashes.size() > MAX_SIZE) {
                lastHashes.remove(0);
            }
        }

        return hashId;
    }

}
