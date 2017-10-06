package com.harium.etyl.networking.model;

import com.harium.etyl.networking.model.data.ConnectionData;
import com.harium.etyl.networking.model.data.RawData;

public interface BaseClient {

    void sendToTCP(ConnectionData connectionData);

    void sendToTCP(RawData rawData);

    void sendToUDP(ConnectionData connectionData);

    void sendToUDP(RawData rawData);

    void sendToWebSocket(ConnectionData connectionData);

    void sendToWebSocket(RawData rawData);
}
