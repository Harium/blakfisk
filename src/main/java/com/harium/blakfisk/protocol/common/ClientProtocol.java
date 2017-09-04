package com.harium.blakfisk.protocol.common;

import com.harium.blakfisk.backend.kryo.KryoClient;
import com.harium.blakfisk.model.Peer;
import com.harium.blakfisk.model.data.MessageProtocol;

public abstract class ClientProtocol extends ProtocolImpl {
	
	protected KryoClient client;
	
	public ClientProtocol(String prefix, KryoClient client) {
		super(prefix);
		this.client = client;
	}

	public abstract void receiveTCP(Peer peer, byte[] message);

	public abstract void receiveUDP(Peer peer, byte[] message);
		
	@Override
	public void receive(byte messageProtocol, Peer peer, byte[] message) {
		if (MessageProtocol.TCP == messageProtocol) {
			receiveTCP(peer, message);	
		} else if (MessageProtocol.UDP == messageProtocol) {
			receiveUDP(peer, message);	
		}
	}
	
}
