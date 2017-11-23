package com.harium.etyl.networking.websocket;

import com.harium.etyl.networking.model.BaseClient;
import com.harium.etyl.networking.model.data.ConnectionData;
import com.harium.etyl.networking.model.data.ConnectionType;
import com.harium.etyl.networking.model.data.RawData;
import com.harium.etyl.networking.protocol.ProtocolHandler;
import com.harium.etyl.networking.util.ByteMessageUtils;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class EtylWebSocketClient extends WebSocketClient implements BaseClient {

    protected String serverUri;

    protected final WebSocketPeer SERVER = new WebSocketPeer(Integer.MIN_VALUE);

    ProtocolHandler protocolHandler = new ProtocolHandler();

    public EtylWebSocketClient(String serverUri) {
        super(URI.create(serverUri));
        this.serverUri = serverUri;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {

    }

    @Override
    public void onMessage(String message) {
        ConnectionData data = new ConnectionData();
        data.connectionType = ConnectionType.WEBSOCKET;
        data.data = message.getBytes();
        protocolHandler.receiveMessageData(SERVER, data);
    }

    @Override
    public void onClose(int i, String message, boolean b) {

    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void sendToTCP(ConnectionData connectionData) {
        sendToWebSocket(connectionData);
    }

    @Override
    public void sendToTCP(RawData rawData) {
        sendToWebSocket(rawData);
    }

    @Override
    public void sendToUDP(ConnectionData connectionData) {

    }

    @Override
    public void sendToUDP(RawData rawData) {

    }

    @Override
    public void sendToWebSocket(ConnectionData connectionData) {
        byte[] data = ByteMessageUtils.concatenateMessage(connectionData.prefix, connectionData.data);
        send(data);
    }

    @Override
    public void sendToWebSocket(RawData rawData) {
        send(rawData.data);
    }
}
