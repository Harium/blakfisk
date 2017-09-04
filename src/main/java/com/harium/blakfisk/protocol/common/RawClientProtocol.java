package com.harium.blakfisk.protocol.common;

import com.harium.blakfisk.backend.kryo.KryoClient;
import com.harium.blakfisk.backend.kryo.KryoEndpoint;

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
