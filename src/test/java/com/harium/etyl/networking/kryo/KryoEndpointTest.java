package com.harium.etyl.networking.kryo;

import com.esotericsoftware.kryonet.Connection;
import com.harium.etyl.networking.model.BaseServer;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class KryoEndpointTest {

    @Test
    public void testDisconnectInvalidPeer() {
        BaseServer server = mock(BaseServer.class);
        KryoEndpoint endpoint = new KryoEndpoint(server, null);
        endpoint.disconnected(new FakeConnection());
    }

    class FakeConnection extends Connection {

    }

}
