package com.harium.etyl.networking.model;

import com.harium.etyl.networking.model.data.ConnectionData;
import com.harium.etyl.networking.model.data.RawData;

public interface BaseSender {

    void sendToTCP(int id, ConnectionData connectionData);

    void sendToTCP(int id, RawData rawData);

    void sendToAllTCP(ConnectionData connectionData);

    void sendToAllExceptTCP(int id, ConnectionData connectionData);

    void sendToUDP(int id, ConnectionData connectionData);

    void sendToUDP(int id, RawData rawData);

    void sendToAllUDP(ConnectionData connectionData);

    void sendToAllExceptUDP(int id, ConnectionData connectionData);
}
