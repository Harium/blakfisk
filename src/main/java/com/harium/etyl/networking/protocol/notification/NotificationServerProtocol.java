package com.harium.etyl.networking.protocol.notification;

import com.harium.etyl.networking.model.BaseServer;
import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.protocol.common.ServerProtocol;
import com.harium.etyl.networking.util.ByteMessageUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NotificationServerProtocol extends ServerProtocol {

    private int count = 0;
    public static final String PREFIX_ACK = "a";

    // Package scope for test purpose
    Map<Integer, Notification> notifications = new HashMap<>(16);

    public NotificationServerProtocol(String prefix, BaseServer server) {
        super(prefix, server);
    }

    @Override
    public void receiveTCP(Peer peer, byte[] message) {
    }

    @Override
    public void receiveUDP(Peer peer, byte[] message) {
        byte[] prefix = ByteMessageUtils.getPrefix(message);

        if (PREFIX_ACK.equals(prefix)) {
            byte[] text = ByteMessageUtils.wipePrefix(prefix, message);
            String hashText = new String(text);
            int hash = Integer.parseInt(hashText);
            notifications.remove(hash);
        }
    }

    public void sendNotification(Peer peer, String message) {
        sendNotification(peer, message.getBytes());
    }

    public void sendNotification(Peer peer, byte[] message) {
        Notification notification = new Notification(peer, message);
        notifications.put(generateId(), notification);
    }

    public void dispatch() {
        Iterator<Map.Entry<Integer, Notification>> it = notifications.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Notification> pair = it.next();
            int id = pair.getKey();
            Notification notification = pair.getValue();

            byte[] hashId = ByteMessageUtils.intToBytes(id);
            byte[] text = ByteMessageUtils.concatenateMessage(hashId, notification.message);
            sendUDP(notification.peer, text);
        }
    }

    private int generateId() {
        return count++;
    }
}
