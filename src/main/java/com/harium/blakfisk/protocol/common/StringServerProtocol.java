package com.harium.blakfisk.protocol.common;

import com.harium.blakfisk.BlakFiskServer;
import com.harium.blakfisk.model.Peer;

public abstract class StringServerProtocol extends ServerProtocol {

	public StringServerProtocol(String prefix, BlakFiskServer server) {
		super(prefix, server);
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
