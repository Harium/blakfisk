package com.harium.etyl.networking.protocol.notification;

import com.harium.etyl.networking.model.BaseServer;
import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.protocol.common.ServerProtocol;
import com.harium.etyl.networking.util.ByteMessageUtils;

import java.util.*;

public class NotificationServerProtocol extends ServerProtocol {

    private int count = 0;
    public static final String PREFIX_ACK = "a";

    // Package scope for test purpose
    Map<Integer, Notification> notifications = new HashMap<>(16);
    Set<Integer> leftPeers = new HashSet<>();
    Set<Integer> leftBucket = new HashSet<>();

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

    public void notify(Peer peer, String message) {
        notify(peer, message.getBytes());
    }

    public void notify(Peer peer, byte[] message) {
        Notification notification = new Notification(peer, message);
        notifications.put(generateId(), notification);
    }

    public void notifyAll(byte[] message) {
        Iterator<Peer> iterator = peers.values().iterator();
        while (iterator.hasNext()) {
            Peer peer = iterator.next();
            notify(peer, message);
        }
    }

    public void notifyAll(String message) {
        notifyAll(message.getBytes());
    }

    public void notifyAllExcept(Peer peer, byte[] message) {
        Iterator<Peer> iterator = peers.values().iterator();
        while (iterator.hasNext()) {
            Peer p = iterator.next();
            if (p.equals(peer)) {
                continue;
            }
            notify(p, message);
        }
    }

    public void notifyAllExcept(Peer peer, String message) {
        notifyAllExcept(peer, message.getBytes());
    }

    public void dispatch() {
        if (notifications.isEmpty()) {
            return;
        }

        Iterator<Map.Entry<Integer, Notification>> it = notifications.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Notification> pair = it.next();

            int id = pair.getKey();
            Notification notification = pair.getValue();

            if (isLeftPeer(notification.peer.getId())) {
                leftBucket.add(id);
                continue;
            }

            byte[] hashId = ByteMessageUtils.intToBytes(id);
            byte[] text = ByteMessageUtils.concatenateMessage(hashId, notification.message);
            sendUDP(notification.peer, text);
        }

        handleLeftBucket();
        leftPeers.clear();
    }

    private boolean isLeftPeer(int id) {
        return !leftPeers.isEmpty() && leftPeers.contains(id);
    }

    private void handleLeftBucket() {
        if (!leftBucket.isEmpty()) {
            for (Integer key : leftBucket) {
                notifications.remove(key);
            }
            leftBucket.clear();
        }
    }

    private int generateId() {
        return count++;
    }

    @Override
    public void removePeer(Peer peer) {
        super.removePeer(peer);
        leftPeers.add(peer.getId());
    }
}
