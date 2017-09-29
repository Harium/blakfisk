package com.harium.etyl.networking.protocol.common;

import com.harium.etyl.networking.EtylServer;
import com.harium.etyl.networking.model.Peer;

public abstract class StringServerProtocol extends ServerProtocol {

	public StringServerProtocol(String prefix, EtylServer server) {
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
