package com.harium.etyl.networking.protocol.reliable;

import com.harium.etyl.networking.model.Packet;
import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.protocol.Protocol;
import com.harium.etyl.networking.util.ByteMessageUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class ReliableHandler {

    private static final int MAX_SIZE = 16;
    private static final int HASH_SIZE = 2;// Size of Short

    public static final byte[] PREFIX_ACK = "A".getBytes();
    public static final byte[] PREFIX_MESSAGE = "M".getBytes();

    // Package scope for test purpose
    Protocol sender;
    Protocol listener;

    int lastValidPacket = 0;
    short count = 0;

    List<Short> earlyPackets = new ArrayList<>(MAX_SIZE);
    List<Packet> queue = new ArrayList<>(MAX_SIZE);

    private Set<Integer> leftPeers = new HashSet<>(MAX_SIZE);
    private Set<Short> ignoredPackets = new HashSet<>(MAX_SIZE);

    public ReliableHandler(Protocol sender, Protocol listener) {
        this.sender = sender;
        this.listener = listener;
    }

    public void receiveTCP(Peer peer, byte[] message) {
        listener.receiveTCP(peer, message);
    }

    public void receiveUDP(Peer peer, byte[] message) {
        byte[] prefix = ByteMessageUtils.getPrefix(message);

        if (ByteMessageUtils.equals(PREFIX_ACK, prefix)) {
            byte[] text = ByteMessageUtils.wipePrefix(prefix, message);
            short hash = ByteMessageUtils.bytesToShort(text);
            removePacket(hash);
        } else if (ByteMessageUtils.equals(PREFIX_MESSAGE, prefix)) {
            // Remove Prefix
            byte[] text = ByteMessageUtils.subByte(message, 2);
            byte[] hashId = notifyListener(peer, text);
            byte[] response = ByteMessageUtils.concatenateMessages(PREFIX_ACK, hashId);
            // Send ack message
            sendUDP(peer, response);
        }
    }

    private byte[] notifyListener(Peer peer, byte[] message) {
        // Get Hash Id
        byte[] hashId = ByteMessageUtils.getPrefix(message, HASH_SIZE);
        byte[] text = ByteMessageUtils.wipePrefix(message, HASH_SIZE);
        short id = ByteMessageUtils.bytesToShort(hashId);

        // Avoid multiples calls to receiveUDP with the same message
        if (!earlyPackets.contains(id)) {
            handleHashKey(id);
            // Handle text
            listener.receiveUDP(peer, text);
        }

        return hashId;
    }

    // Package scope for test purpose only
    void handleHashKey(Short id) {
        if (id == lastValidPacket + 1) {
            lastValidPacket++;
            List<Short> toRemove = new ArrayList<>();
            for (Short key : earlyPackets) {
                if (key == lastValidPacket + 1) {
                    lastValidPacket++;
                    toRemove.add(key);
                } else if (key < lastValidPacket) {
                    toRemove.add(key);
                }
            }
            for (Short key : toRemove) {
                earlyPackets.remove(key);
            }
        } else {
            earlyPackets.add(id);
        }
    }

    public void notify(Peer peer, byte[] message) {
        short hashId = generateId();
        byte[] bytesId = ByteMessageUtils.shortToBytes(hashId);
        byte[] text = ByteMessageUtils.concatenateMessages(PREFIX_MESSAGE, bytesId, message);

        Packet packet = new Packet(peer, text);
        packet.setId(hashId);
        addPacket(packet);
    }

    private void addPacket(Packet packet) {
        queue.add(packet);
    }

    public void notifyAll(byte[] message) {
        List<Peer> peers = new ArrayList(listener.getPeers().values());

        for (Peer peer : peers) {
            notify(peer, message);
        }
    }

    public void dispatch() {
        if (queue.isEmpty()) {
            return;
        }

        sendMessages();
        handleIgnoredPackets();
    }

    private void sendMessages() {
        if (leftPeers.isEmpty()) {
            List<Packet> list = new CopyOnWriteArrayList<>(queue);
            for (Packet packet : list) {
                sendUDP(packet.getPeer(), packet.getMessage());
            }
        } else {
            List<Packet> list = new CopyOnWriteArrayList<>(queue);
            for (Packet packet : list) {
                short id = packet.getId();

                if (isLeftPeer(packet.getPeer().getId())) {
                    ignoredPackets.add(id);
                    continue;
                }

                sendUDP(packet.getPeer(), packet.getMessage());
            }
            leftPeers.clear();
        }
    }

    private boolean isLeftPeer(int id) {
        return !leftPeers.isEmpty() && leftPeers.contains(id);
    }

    private void handleIgnoredPackets() {
        if (ignoredPackets.isEmpty()) {
            return;
        }
        for (Short key : ignoredPackets) {
            removePacket(key);
        }
        ignoredPackets.clear();
    }

    private void removePacket(short key) {
        for (int i = queue.size() - 1; i >= 0; i--) {
            Packet packet = queue.get(i);
            if (key == packet.getId()) {
                queue.remove(i);
                break;
            }
        }
    }

    private short generateId() {
        return ++count;
    }

    public void addLeftPeer(int peerId) {
        leftPeers.add(peerId);
    }

    public void notifyAllExcept(Peer peer, byte[] message) {
        List<Peer> peers = new ArrayList<>(listener.getPeers().values());

        for (Peer p : peers) {
            if (p.equals(peer)) {
                continue;
            }
            notify(p, message);
        }
    }

    private void sendUDP(Peer peer, byte[] message) {
        sender.sendUDP(peer, message);
    }
}
