package com.harium.etyl.networking.core.model;

import com.harium.etyl.networking.core.model.data.ConnectionData;
import com.harium.etyl.networking.core.model.data.RawData;

public interface BaseClient {

    void sendToTCP(ConnectionData connectionData);

    void sendToTCP(RawData rawData);

    void sendToUDP(ConnectionData connectionData);

    void sendToUDP(RawData rawData);

    void sendToWebSocket(ConnectionData connectionData);

    void sendToWebSocket(RawData rawData);
}
