package com.harium.etyl.networking;

import com.harium.etyl.networking.kryo.KryoClient;

public class EtylClient extends KryoClient {

    public EtylClient(String host, int tcpPort) {
        super(host, tcpPort);
    }

    public EtylClient(String host, int tcpPort, int udpPort) {
        super(host, tcpPort, udpPort);
    }

}
