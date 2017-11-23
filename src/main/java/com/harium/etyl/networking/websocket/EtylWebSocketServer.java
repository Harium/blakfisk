package com.harium.etyl.networking.websocket;

import com.harium.etyl.networking.model.BaseServer;
import com.harium.etyl.networking.model.ByteArrayKey;
import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.model.data.ConnectionData;
import com.harium.etyl.networking.model.data.ConnectionType;
import com.harium.etyl.networking.model.data.RawData;
import com.harium.etyl.networking.protocol.Protocol;
import com.harium.etyl.networking.protocol.ProtocolHandler;
import com.harium.etyl.networking.util.ByteMessageUtils;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public abstract class EtylWebSocketServer extends WebSocketServer implements BaseServer {

    int counter = 0;
    ProtocolHandler serverHandler = new ProtocolHandler();
    Map<String, WebSocketPeer> peers = new HashMap<>();
    Map<Integer, WebSocketPeer> peerIds = new HashMap<>();

    public EtylWebSocketServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        WebSocketPeer peer = new WebSocketPeer(webSocket);
        String uniqueId = peer.getUniqueId();

        // Verify if it is a different client
        if (!peers.containsKey(uniqueId)) {
            peer.setId(++counter);

            peers.put(uniqueId, peer);
            peerIds.put(peer.getId(), peer);
            onConnect(peer);
        }
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String message, boolean b) {
        Peer peer = peers.get(webSocket.getResourceDescriptor());
        removePeer(peer.getId());
    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {
        Peer peer = peers.get(webSocket.getResourceDescriptor());

        String prefix = ByteMessageUtils.getPrefix(message);
        String data = message.substring(prefix.length());

        ConnectionData connectionData = serverHandler.packMessage(prefix, data);
        connectionData.connectionType = ConnectionType.WEBSOCKET;
        serverHandler.receiveMessageData(peer, connectionData);
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void removePeer(int id) {
        WebSocketPeer peer = peerIds.get(id);
        if (peer != null) {
            leftPeer(peer);
            serverHandler.removePeer(peer);
            peers.remove(peer.getUniqueId());
            peerIds.remove(id);
        }
    }

    @Override
    public Peer getPeer(int id) {
        return peers.get(id);
    }

    @Override
    public boolean hasPeer(int id) {
        return peers.containsKey(id);
    }

    /**
     * Adds the protocol with the default prefix
     *
     * @param prefix   - the prefix associated to the protocol
     * @param protocol - the protocol to respond by it's own prefix
     */
    public void addProtocol(byte[] prefix, Protocol protocol) {
        serverHandler.addProtocol(prefix, protocol);
    }

    public void addProtocol(String prefix, Protocol protocol) {
        this.addProtocol(prefix.getBytes(), protocol);
    }

    public Map<ByteArrayKey, Protocol> getProtocols() {
        return serverHandler.getProtocols();
    }

    public Protocol getProtocol(String prefix) {
        return serverHandler.getProtocol(prefix);
    }

    @Override
    public void onConnect(Peer peer) {
        serverHandler.addPeer(peer);
        joinPeer(peer);
    }

    @Override
    public void sendToTCP(int id, ConnectionData connectionData) {
        WebSocketPeer peer = peerIds.get(id);
        WebSocket webSocket = peer.getWebSocket();

        byte[] data = ByteMessageUtils.concatenateMessage(connectionData.prefix, connectionData.data);
        webSocket.send(data);
    }

    @Override
    public void sendToTCP(int id, RawData rawData) {
        WebSocketPeer peer = peerIds.get(id);
        WebSocket webSocket = peer.getWebSocket();
        webSocket.send(rawData.data);
    }

    @Override
    public void sendToAllTCP(ConnectionData connectionData) {
        for (WebSocketPeer peer : peers.values()) {
            WebSocket webSocket = peer.getWebSocket();

            byte[] data = ByteMessageUtils.concatenateMessage(connectionData.prefix, connectionData.data);
            webSocket.send(data);
        }
    }

    @Override
    public void sendToAllExceptTCP(int id, ConnectionData connectionData) {
        for (WebSocketPeer peer : peers.values()) {
            if (id == peer.getId()) {
                continue;
            }
            WebSocket webSocket = peer.getWebSocket();

            byte[] data = ByteMessageUtils.concatenateMessage(connectionData.prefix, connectionData.data);
            webSocket.send(data);
        }
    }

    @Override
    public void sendToUDP(int id, ConnectionData connectionData) {

    }

    @Override
    public void sendToUDP(int id, RawData rawData) {

    }

    @Override
    public void sendToAllUDP(ConnectionData connectionData) {

    }

    @Override
    public void sendToAllExceptUDP(int id, ConnectionData connectionData) {

    }
}
