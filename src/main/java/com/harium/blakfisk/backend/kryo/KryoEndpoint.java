package com.harium.blakfisk.backend.kryo;

import com.harium.blakfisk.model.ByteArrayKey;
import com.harium.blakfisk.model.Peer;
import com.harium.blakfisk.model.data.ConnectionData;
import com.harium.blakfisk.model.data.MessageProtocol;
import com.harium.blakfisk.model.data.RawData;
import com.harium.blakfisk.protocol.Protocol;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Listener;

import java.util.HashMap;
import java.util.Map;

public class KryoEndpoint extends Listener {

    public static final int UNDEFINED_PORT = -1;
    public static final int DEFAULT_TIMEOUT = 5000;
    public static final String LOCAL_HOST = "127.0.0.1";

    protected Map<ByteArrayKey, Protocol> protocols = new HashMap<ByteArrayKey, Protocol>();

    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(byte.class);
        kryo.register(byte[].class);
        kryo.register(RawData.class);
        kryo.register(ConnectionData.class);
    }

    public void receiveMessageData(Peer peer, ConnectionData message) {
        Protocol protocol = getProtocol(message);

        if (protocol == null) {
            return;
        }

        byte connectionType = message.connectionType;
        if (MessageProtocol.TCP == connectionType) {
            protocol.receiveTCP(peer, message.data);
        } else if (MessageProtocol.UDP == connectionType) {
            protocol.receiveUDP(peer, message.data);
        } else if (MessageProtocol.WEBSOCKET == connectionType) {
            protocol.receive(MessageProtocol.WEBSOCKET, peer, message.data);
        }

    }

    protected Protocol getProtocol(ConnectionData message) {
        return protocols.get(new ByteArrayKey(message.prefix));
    }

    protected Protocol getProtocol(String prefix) {
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

}
