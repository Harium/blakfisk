package com.harium.etyl.networking.protocol.reliable;

import com.harium.etyl.networking.model.Packet;
import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.protocol.Protocol;
import com.harium.etyl.networking.util.ByteMessageUtils;

import java.util.*;

public class ReliableHandler {

    private static final int MAX_SIZE = 16;
    private static final int HASH_SIZE = 4;

    public static final byte[] PREFIX_ACK = "A".getBytes();
    public static final byte[] PREFIX_MESSAGE = "M".getBytes();

    // Package scope for test purpose
    Protocol listener;

    int lastValidPacket = 0;
    int count = 0;

    List<Integer> earlyPackets = new ArrayList<>(MAX_SIZE);

    private Map<Integer, Packet> packets = new HashMap<>(MAX_SIZE);
    private Set<Integer> leftPeers = new HashSet<>();
    private Set<Integer> leftBucket = new HashSet<>();

    public ReliableHandler(Protocol listener) {
        this.listener = listener;
    }

    public void receiveTCP(Peer peer, byte[] message) {
        listener.receiveTCP(peer, message);
    }

    public void receiveUDP(Peer peer, byte[] message) {
        byte[] prefix = ByteMessageUtils.getPrefix(message);

        if (ByteMessageUtils.equals(PREFIX_ACK, prefix)) {
            byte[] text = ByteMessageUtils.wipePrefix(prefix, message);
            int hash = ByteMessageUtils.bytesToInt(text);
            packets.remove(hash);
        } else if (ByteMessageUtils.equals(PREFIX_MESSAGE, prefix)) {
            // Remove Prefix
            byte[] text = ByteMessageUtils.subByte(message, 2);
            byte[] hashId = notifyListener(peer, text);
            byte[] response = ByteMessageUtils.concatenateMessage(PREFIX_ACK, hashId);
            // Send ack message
            listener.sendUDP(peer, response);
        }
    }

    private byte[] notifyListener(Peer peer, byte[] message) {
        // Get Hash Id
        byte[] hashId = ByteMessageUtils.getPrefix(message, HASH_SIZE);
        byte[] text = ByteMessageUtils.wipePrefix(message, HASH_SIZE);
        int id = ByteMessageUtils.bytesToInt(hashId);

        // Avoid multiples calls to receiveUDP with the same message
        if (!earlyPackets.contains(id)) {
            handleHashKey(id);
            // Handle text
            listener.receiveUDP(peer, text);
        }

        return hashId;
    }

    // Package scope for test purpose only
    void handleHashKey(int id) {
        if (id == lastValidPacket + 1) {
            lastValidPacket++;
            List<Integer> toRemove = new ArrayList<>();
            for (Integer key : earlyPackets) {
                if (key == lastValidPacket + 1) {
                    lastValidPacket++;
                    toRemove.add(key);
                } else if (key < lastValidPacket) {
                    toRemove.add(key);
                }
            }
            for (Integer key : toRemove) {
                earlyPackets.remove(key);
            }
        } else {
            earlyPackets.add(id);
        }
    }

    public void notify(Peer peer, byte[] message) {
        Packet packet = buildPacket(peer, message);
        packets.put(generateId(), packet);
    }

    public void notifyAll(byte[] message) {
        Iterator<Peer> iterator = listener.getPeers().values().iterator();
        while (iterator.hasNext()) {
            Peer peer = iterator.next();
            notify(peer, message);
        }
    }

    private Packet buildPacket(Peer peer, byte[] message) {
        byte[] prefixedMessage = ByteMessageUtils.concatenateMessage(PREFIX_MESSAGE, message);
        return new Packet(peer, prefixedMessage);
    }

    public void dispatch() {
        if (packets.isEmpty()) {
            return;
        }

        Iterator<Map.Entry<Integer, Packet>> iterator = packets.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Packet> pair = iterator.next();

            int id = pair.getKey();
            Packet packet = pair.getValue();

            if (isLeftPeer(packet.getPeer().getId())) {
                leftBucket.add(id);
                continue;
            }

            byte[] hashId = ByteMessageUtils.intToBytes(id);
            byte[] text = ByteMessageUtils.concatenateMessage(hashId, packet.getMessage());
            listener.sendUDP(packet.getPeer(), text);
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
                packets.remove(key);
            }
            leftBucket.clear();
        }
    }

    private int generateId() {
        return count++;
    }

    public void addLeftPeer(int peerId) {
        leftPeers.add(peerId);
    }

    public void notifyAllExcept(Peer peer, byte[] message) {
        Iterator<Peer> iterator = listener.getPeers().values().iterator();
        while (iterator.hasNext()) {
            Peer p = iterator.next();
            if (p.equals(peer)) {
                continue;
            }
            notify(p, message);
        }
    }
}
