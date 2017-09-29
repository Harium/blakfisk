package com.harium.etyl.networking.example.client;

import com.harium.etyl.networking.EtylClient;
import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.protocol.common.StringClientProtocol;

public class SimpleClientProtocol extends StringClientProtocol {

    public static final String DEFAULT_PREFIX = "/s";

    public SimpleClientProtocol(EtylClient client) {
        super(DEFAULT_PREFIX, client);
    }

    public void sendHandShake() {
        sendTCP("hi");
    }

    public void sendUDPHandShake() {
        sendUDP("hi");
    }

    @Override
    public void receiveTCP(Peer peer, String msg) {
        System.out.println(this.getClass().getSimpleName() + "(TCP) received " + msg);
    }

    @Override
    public void receiveUDP(Peer peer, String msg) {
        System.out.println(this.getClass().getSimpleName() + "(UDP) received " + msg);
    }

}
