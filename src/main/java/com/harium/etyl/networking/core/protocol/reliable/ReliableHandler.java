package com.harium.etyl.networking.core.protocol.reliable;

import com.harium.etyl.networking.core.helper.ByteHelper;
import com.harium.etyl.networking.core.helper.ByteMessageHelper;
import com.harium.etyl.networking.core.model.Packet;
import com.harium.etyl.networking.core.model.Peer;
import com.harium.etyl.networking.core.protocol.Protocol;

import java.util.*;

public class ReliableHandler {

    private static final int MAX_SIZE = 16;
    private static final int HASH_SIZE = 2;// Size of Short

    public static final byte[] PREFIX_ACK = "A".getBytes();
    public static final byte[] PREFIX_MESSAGE = "M".getBytes();

    // Package scope for test purpose
    Protocol sender;
    Protocol listener;

    short lastValidPacket = 0;
    short count = 0;

    Map<Short, Packet> earlyPackets = new LinkedHashMap<>(MAX_SIZE);
    Map<Short, Packet> queue = new LinkedHashMap<>(MAX_SIZE);

    private Set<Integer> leftPeers = new HashSet<>(MAX_SIZE);

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
            removePacket(hash);
        } else if (ByteMessageHelper.equals(PREFIX_MESSAGE, prefix)) {
            // Remove Prefix
            byte[] text = ByteMessageHelper.subByte(message, 2);
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
        short id = packet.getId();
        // If packet is the next id, order is fine
        if (id == lastValidPacket + 1) {
            lastValidPacket++;
            // Receive the packet
            receivePacket(packet);

            // Handle next packets
            short nextId = (short) (lastValidPacket + 1);
            while (earlyPackets.containsKey(nextId)) {
                // Handle packet
                Packet p = earlyPackets.get(nextId);
                lastValidPacket++;
                receivePacket(p);

                // Remove the received packet
                earlyPackets.remove(nextId);
                nextId++;
            }
        } else if (id > lastValidPacket) {
            // If packet arrived early, add to early packets
            earlyPackets.put(id, packet);
        }
    }

    private void receivePacket(Packet packet) {
        listener.receiveUDP(packet.getPeer(), packet.getMessage());
    }

    public void notify(Peer peer, byte[] message) {
        short hashId = generateId();
        byte[] bytesId = ByteHelper.shortToBytes(hashId);
        byte[] text = ByteMessageHelper.concatenateMessages(PREFIX_MESSAGE, bytesId, message);

        Packet packet = new Packet(peer, text);
        packet.setId(hashId);
        addPacket(packet);
    }

    private void addPacket(Packet packet) {
        queue.put(packet.getId(), packet);
    }

    public void notifyAll(byte[] message) {
        Iterator<Peer> iterator = listener.getPeers().values().iterator();

        while (iterator.hasNext()) {
            Peer peer = iterator.next();
            notify(peer, message);
        }
    }

    public void dispatch() {
        if (queue.isEmpty()) {
            return;
        }

        sendMessages();
    }

    private void sendMessages() {
        Iterator<Map.Entry<Short, Packet>> iterator = queue.entrySet().iterator();

        if (leftPeers.isEmpty()) {
            while (iterator.hasNext()) {
                Packet packet = iterator.next().getValue();
                sendUDP(packet.getPeer(), packet.getMessage());
            }
        } else {
            while (iterator.hasNext()) {
                Packet packet = iterator.next().getValue();

                if (isLeftPeer(packet.getPeer().getId())) {
                    iterator.remove();
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

    private void removePacket(short key) {
        queue.remove(key);
    }

    private short generateId() {
        return ++count;
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

    private void sendUDP(Peer peer, byte[] message) {
        sender.sendUDP(peer, message);
    }
}
