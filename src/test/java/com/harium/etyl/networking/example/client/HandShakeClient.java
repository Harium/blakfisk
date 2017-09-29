package com.harium.etyl.networking.example.client;

import com.harium.etyl.networking.EtylClient;

public class HandShakeClient extends EtylClient {

    private boolean running = true;
    private SimpleClientProtocol simpleProtocol;

    public HandShakeClient(String ip, int tcpPort) {
        super(ip, tcpPort);

        simpleProtocol = new SimpleClientProtocol(this);
        addProtocol(simpleProtocol);
    }

    public SimpleClientProtocol getSimpleProtocol() {
        return simpleProtocol;
    }

    public void sendMessages(final int delay) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    System.out.println("Sending message...");
                    simpleProtocol.sendHandShake();
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        }).start();
    }

    public void stopMessages() {
        setRunning(false);
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

}
