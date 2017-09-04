package com.harium.blakfisk.protocol.common;

import com.harium.blakfisk.BlakFiskClient;
import com.harium.blakfisk.backend.kryo.KryoEndpoint;
import com.harium.blakfisk.model.Peer;

public abstract class StringClientProtocol extends ClientProtocol {
	
	public StringClientProtocol(String prefix, BlakFiskClient client) {
		super(prefix, client);
	}

	protected void sendTCP(String message) {
		client.sendTCP(KryoEndpoint.packMessage(prefix, message.getBytes()));
	}
	
	protected void sendUDP(String message) {
		client.sendUDP(KryoEndpoint.packMessage(prefix, message.getBytes()));
	}
	
	@Override
	public void receiveTCP(Peer peer, byte[] message) {
		receiveTCP(peer, new String(message));
	}

	protected abstract void receiveTCP(Peer peer, String message);

	@Override
	public void receiveUDP(Peer peer, byte[] message) {
		receiveUDP(peer, new String(message));
	}
	
	protected abstract void receiveUDP(Peer peer, String message);
	
}
