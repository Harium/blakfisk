package com.harium.etyl.networking.protocol.common;

import com.harium.etyl.networking.kryo.KryoClient;
import com.harium.etyl.networking.kryo.KryoEndpoint;

public abstract class RawClientProtocol extends ClientProtocol {
	
	public RawClientProtocol(String prefix, KryoClient client) {
		super(prefix, client);
	}

	protected void sendTCP(byte[] message) {
		client.sendTCP(KryoEndpoint.packMessage(prefix, message));
	}
	
	protected void sendUDP(byte[] message) {
		client.sendUDP(KryoEndpoint.packMessage(prefix, message));
	}
}
