package com.harium.etyl.networking.core.protocol;

import com.harium.etyl.networking.core.dummy.LogProtocol;
import com.harium.etyl.networking.core.model.ByteArrayKey;
import com.harium.etyl.networking.core.model.Peer;
import com.harium.etyl.networking.core.model.data.ConnectionData;
import com.harium.etyl.networking.core.model.data.ConnectionType;
import com.harium.etyl.networking.core.model.data.RawData;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ProtocolHandler {

    public static final int UNDEFINED_PORT = -1;
    public static final int DEFAULT_TIMEOUT = 5000;
    public static final String LOCAL_HOST = "127.0.0.1";

    protected Map<ByteArrayKey, Protocol> protocols = new HashMap<ByteArrayKey, Protocol>();

    private static final Protocol DUMMY_HANDSHAKER = new LogProtocol();
    public Protocol handshaker = DUMMY_HANDSHAKER;

    public ProtocolHandler() {
        super();
    }

    public void receiveMessageData(Peer peer, ConnectionData message) {
        Protocol protocol = getProtocol(message);

        if (protocol == null) {
            return;
        }

        byte connectionType = message.connectionType;

        if (ConnectionType.TCP == connectionType) {
            protocol.receiveTCP(peer, message.data);
        } else if (ConnectionType.UDP == connectionType) {
            protocol.receiveUDP(peer, message.data);
        } else if (ConnectionType.WEBSOCKET == connectionType) {
            protocol.receiveWebSocket(peer, message.data);
        } else {
            byte undefinedProtocol = connectionType;
            protocol.receive(undefinedProtocol, peer, message.data);
        }
    }

    protected Protocol getProtocol(ConnectionData message) {
        return protocols.get(new ByteArrayKey(message.prefix));
    }

    public Protocol getProtocol(String prefix) {
        return protocols.get(new ByteArrayKey(prefix.getBytes()));
    }

    /**
     * Adds the protocol with the default prefix
     *
     * @param prefix   - the prefix associated to the protocol
     * @param protocol - the protocol to respond by it's own prefix
     */
    public void addProtocol(byte[] prefix, Protocol protocol) {
        if (protocol == null)
            return;

        protocols.put(new ByteArrayKey(prefix), protocol);
    }

    public static ConnectionData packMessage(byte[] prefix, byte[] message) {
        ConnectionData data = new ConnectionData();
        data.prefix = prefix;
        data.data = message;
        return data;
    }

    public static ConnectionData packMessage(byte[] prefix, String message) {
        return packMessage(prefix, message.getBytes());
    }

    public static ConnectionData packMessage(String prefix, String message) {
        return packMessage(prefix.getBytes(), message.getBytes());
    }

    public static RawData packRawMessage(byte[] message) {
        RawData data = new RawData();
        data.data = message;
        return data;
    }

    public static RawData packRawMessage(String message) {
        return packRawMessage(message.getBytes());
    }

    public Map<ByteArrayKey, Protocol> getProtocols() {
        return protocols;
    }

    public void addPeer(Peer peer) {
        handshaker.addPeer(peer);
    }

    public void removePeer(Peer peer) {
        Iterator<Protocol> iterator = protocols.values().iterator();
        while (iterator.hasNext()) {
            Protocol protocol = iterator.next();
            protocol.removePeer(peer);
        }
    }
}
