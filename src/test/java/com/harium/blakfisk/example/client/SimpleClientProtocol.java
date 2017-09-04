package com.harium.blakfisk.example.client;

import com.harium.blakfisk.BlakFiskClient;
import com.harium.blakfisk.model.Peer;
import com.harium.blakfisk.protocol.common.StringClientProtocol;

public class SimpleClientProtocol extends StringClientProtocol {

    public static final String DEFAULT_PREFIX = "/s";

    public SimpleClientProtocol(BlakFiskClient client) {
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
