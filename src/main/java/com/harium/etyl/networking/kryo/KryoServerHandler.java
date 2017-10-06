package com.harium.etyl.networking.kryo;

import com.harium.etyl.networking.model.BaseServer;
import com.harium.etyl.networking.protocol.ProtocolHandler;

public class KryoServerHandler extends ProtocolHandler {

    KryoEndpoint endpoint;

    public KryoServerHandler(BaseServer server) {
        super();
        endpoint = new KryoEndpoint(server, this);
    }

    public KryoEndpoint getEndpoint() {
        return endpoint;
    }
}
