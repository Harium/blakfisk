package com.prodec.dronepark.simulator.networking.server.protocol;

import com.harium.etyl.networking.model.BaseServer;
import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.protocol.common.StringServerProtocol;
import com.harium.etyl.networking.util.ByteMessageUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NotificationServerProtocol extends StringServerProtocol {

    private int count = 0;
    public static final String PREFIX_ACK = "a";
    private Map<Integer, Notification> notifications = new HashMap<>(16);

    public NotificationServerProtocol(String prefix, BaseServer server) {
        super(prefix, server);
    }

    @Override
    protected void receiveTCP(Peer peer, String message) {
    }

    @Override
    protected void receiveUDP(Peer peer, String message) {
        String prefix = ByteMessageUtils.getPrefix(message);
        if (PREFIX_ACK.equals(prefix)) {
            String hashText = message.substring(PREFIX_ACK.length() + 1);
            int hash = Integer.parseInt(hashText);
            notifications.remove(hash);
        }
    }

    public void sendNotification(Peer peer, String message) {
        Notification notification = new Notification(peer, message);
        notifications.put(generateId(), notification);
    }

    public void dispatch() {
        Iterator<Map.Entry<Integer, Notification>> it = notifications.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Notification> pair = it.next();
            int id = pair.getKey();
            Notification notification = pair.getValue();
            sendUDP(notification.peer, id + " " + notification.message);
        }
    }

    private int generateId() {
        return count++;
    }
}
