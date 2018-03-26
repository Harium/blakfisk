package com.harium.etyl.networking.core.protocol.reliable;

import com.harium.etyl.networking.core.helper.ByteHelper;
import com.harium.etyl.networking.core.helper.ByteMessageHelper;
import com.harium.etyl.networking.core.model.Packet;
import com.harium.etyl.networking.core.model.Peer;
import com.harium.etyl.networking.core.protocol.Protocol;

import java.util.*;

public class ReliableHandler {
    private static final int INITIAL_SIZE = 16;
    private static final int HASH_SIZE = 2;// Size of Short

    public static final byte[] PREFIX_ACK = "A".getBytes();
    public static final byte[] PREFIX_MESSAGE = "M".getBytes();

    // Package scope for test purpose
    Protocol sender;
    Protocol listener;

    Map<Integer, Short> outIndexes = new HashMap<>(INITIAL_SIZE);
    Map<Integer, Short> inIndexes = new HashMap<>(INITIAL_SIZE);

    // Queue of packets
    Map<Integer, Map<Short, Packet>> queue = new HashMap<>(INITIAL_SIZE);

    private Set<Integer> leftPeers = new HashSet<>(INITIAL_SIZE);

    public ReliableHandler(Protocol sender, Protocol listener) {
        this.sender = sender;
        this.listener = listener;
    }

    public void receiveTCP(Peer peer, byte[] message) {
        listener.receiveTCP(peer, message);
    }

    public void receiveUDP(Peer peer, byte[] message) {
        byte[] prefix = ByteMessageHelper.getPrefix(message);

        if (ByteMessageHelper.equals(PREFIX_ACK, prefix)) {
            byte[] text = ByteMessageHelper.wipePrefix(prefix, message);
            short hash = ByteHelper.bytesToShort(text);

            removePacket(peer, hash);
        } else if (ByteMessageHelper.equals(PREFIX_MESSAGE, prefix)) {
            // Remove Prefix
            byte[] text = ByteMessageHelper.subByte(message, PREFIX_MESSAGE.length + 1);
            byte[] hashId = notifyListener(peer, text);
            byte[] response = ByteMessageHelper.concatenateMessages(PREFIX_ACK, hashId);

            // Send ack message
            sendUDP(peer, response);
        }
    }

    private byte[] notifyListener(Peer peer, byte[] message) {
        // Get Hash Id
        byte[] hashId = ByteMessageHelper.getPrefix(message, HASH_SIZE);
        byte[] text = ByteMessageHelper.wipePrefix(message, HASH_SIZE);
        short id = ByteHelper.bytesToShort(hashId);

        Packet packet = new Packet(peer, text);
        packet.setId(id);

        handlePacket(packet);

        return hashId;
    }

    // Package scope for test purpose only
    void handlePacket(Packet packet) {
        short lastValidPacket = inIndexes.get(packet.getPeer().getId());
        short packetId = packet.getId();

        // If packet is the next id, order is fine
        if (packetId == lastValidPacket + 1) {
            // Update nextPacket
            inIndexes.put(packet.getPeer().getId(), packetId);
            // Notify listener
            listener.receiveUDP(packet.getPeer(), packet.getMessage());
        }
    }

    public void notify(Peer peer, byte[] message) {
        short hashId = generateId(peer.getId());
        byte[] bytesId = ByteHelper.shortToBytes(hashId);
        byte[] text = ByteMessageHelper.concatenateMessages(PREFIX_MESSAGE, bytesId, message);

        Packet packet = new Packet(peer, text);
        packet.setId(hashId);
        addPacket(packet);
    }

    private void addPacket(Packet packet) {
        final int peerId = packet.getPeer().getId();

        Map<Short, Packet> packets = queue.get(peerId);
        packets.put(packet.getId(), packet);
    }

    public void notifyAll(byte[] message) {
        Iterator<Peer> iterator = listener.getPeers().values().iterator();

        while (iterator.hasNext()) {
            Peer peer = iterator.next();
            notify(peer, message);
        }
    }

    public void dispatch() {
        checkLeftPeers();
        sendMessages();
    }

    private void checkLeftPeers() {
        if (leftPeers.isEmpty()) {
            return;
        }
        synchronized (queue) {
            for (Integer peerId : leftPeers) {
                queue.remove(peerId);
            }
        }

        leftPeers.clear();
    }

    private void sendMessages() {
        Iterator<Map.Entry<Integer, Map<Short, Packet>>> iterator = queue.entrySet().iterator();

        while (iterator.hasNext()) {
            Map<Short, Packet> packets = iterator.next().getValue();
            Iterator<Map.Entry<Short, Packet>> packetIterator = packets.entrySet().iterator();

            while (packetIterator.hasNext()) {
                Packet packet = packetIterator.next().getValue();
                sendUDP(packet.getPeer(), packet.getMessage());
            }
        }
    }

    private void removePacket(Peer peer, short packetId) {
        final int peerId = peer.getId();
        Map<Short, Packet> packets = queue.get(peerId);
        packets.remove(packetId);
    }

    private short generateId(int peerId) {
        short nextPacket = (short) (outIndexes.get(peerId) + 1);
        outIndexes.put(peerId, nextPacket);
        return nextPacket;
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

    private void sendUDP(Peer peer, byte[] message) {
        sender.sendUDP(peer, message);
    }

    public void addPeer(Peer peer) {
        final int peerId = peer.getId();
        synchronized (queue) {
            queue.put(peerId, new LinkedHashMap<Short, Packet>());
        }
        synchronized (outIndexes) {
            outIndexes.put(peerId, (short) 0);
        }
        synchronized (inIndexes) {
            inIndexes.put(peerId, (short) 0);
        }
    }

    public void removePeer(Peer peer) {
        final int peerId = peer.getId();
        synchronized (leftPeers) {
            // Add peer to delete list
            leftPeers.add(peerId);
        }
        // Peers are removed in checkLeftPeers()
        // queue.remove(peerId);
        synchronized (outIndexes) {
            outIndexes.remove(peerId);
        }
        synchronized (inIndexes) {
            inIndexes.remove(peerId);
        }
    }
}
