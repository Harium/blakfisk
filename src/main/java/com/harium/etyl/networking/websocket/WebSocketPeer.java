package com.harium.etyl.networking.websocket;

import com.harium.etyl.networking.model.ByteArrayKey;
import com.harium.etyl.networking.model.Peer;
import org.java_websocket.WebSocket;

public class WebSocketPeer extends Peer {

    private int id;
    private String uniqueId;
    private WebSocket webSocket;

    public WebSocketPeer(int id) {
        this.id = id;
    }

    public WebSocketPeer(WebSocket webSocket) {
        this.webSocket = webSocket;
        this.uniqueId = webSocket.getResourceDescriptor();
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public ByteArrayKey getUniqueKey() {
        return new ByteArrayKey(webSocket.getResourceDescriptor().getBytes());
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }
}
