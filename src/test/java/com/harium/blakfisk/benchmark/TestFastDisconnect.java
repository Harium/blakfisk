package com.harium.blakfisk.benchmark;

import com.harium.blakfisk.backend.kryo.KryoClient;
import com.harium.blakfisk.backend.kryo.KryoEndpoint;
import com.harium.blakfisk.example.server.HandShakeServer;
import com.harium.blakfisk.example.server.SimpleServerProtocol;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestFastDisconnect extends TestCase {

    private static final String IP = KryoEndpoint.LOCAL_HOST;
    private static final int PORT = 10101;

    private String LISTENER_PREFIX = "/s";
    private HandShakeServer server;

    @Before
    public void setUp() {
        server = new HandShakeServer(PORT);

        SimpleServerProtocol listener = new SimpleServerProtocol(LISTENER_PREFIX, server);

        server.addProtocol(listener.getPrefix(), listener);
    }

    @Test
    public void testFastUsersConnected() throws Exception {
        server.start();

        List<KryoClient> clientSet = new ArrayList<>();

        for (int i = 0; i < 200; i++) {
            KryoClient client = new KryoClient(IP, PORT);
            client.start();
            clientSet.add(client);
        }

        Thread.sleep(2000);
        for (int c = clientSet.size() - 1; c >= 0; c--) {
            KryoClient client = clientSet.get(c);
            if (client.isConnected()) {
                client.close();
            }
        }

        int maxMemoryInMB = 32;
        Runtime rt = Runtime.getRuntime();
        long usedMB = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;

        System.out.println("Used Memory: " + usedMB + "MB");
        Assert.assertTrue(usedMB < maxMemoryInMB);

        Thread.sleep(2000);
        Assert.assertEquals(0, server.getConnections().length);
        server.close();
    }

}

