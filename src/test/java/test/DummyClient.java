package test;

import com.harium.etyl.networking.core.model.BaseClient;
import com.harium.etyl.networking.core.model.data.ConnectionData;
import com.harium.etyl.networking.core.model.data.RawData;

public class DummyClient implements BaseClient {

    public DummyClient(int tcpPort) {

    }

    @Override
    public void sendToTCP(ConnectionData connectionData) {

    }

    @Override
    public void sendToTCP(RawData rawData) {

    }

    @Override
    public void sendToUDP(ConnectionData connectionData) {

    }

    @Override
    public void sendToUDP(RawData rawData) {

    }

    @Override
    public void sendToWebSocket(ConnectionData connectionData) {

    }

    @Override
    public void sendToWebSocket(RawData rawData) {

    }
}
