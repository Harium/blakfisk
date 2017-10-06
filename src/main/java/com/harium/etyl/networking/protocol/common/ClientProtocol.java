package com.harium.etyl.networking.protocol.common;

import com.harium.etyl.networking.model.BaseClient;
import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.model.data.MessageProtocol;

public abstract class ClientProtocol extends ProtocolImpl {

    protected BaseClient client;

    public ClientProtocol(String prefix, BaseClient client) {
        super(prefix);
        this.client = client;
    }

}
