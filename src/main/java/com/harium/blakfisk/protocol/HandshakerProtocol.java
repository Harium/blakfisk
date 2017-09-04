package com.harium.blakfisk.protocol;

import com.harium.blakfisk.backend.kryo.KryoServer;
import com.harium.blakfisk.backend.kryo.KryoEndpoint;
import com.harium.blakfisk.model.Peer;
import com.harium.blakfisk.protocol.common.ProtocolImpl;

public abstract class HandshakerProtocol extends ProtocolImpl implements Protocol {

	protected KryoServer server;
	
	public HandshakerProtocol(String prefix, KryoServer server) {
		super(prefix);
		this.server = server;
	}
	
	public void addPeer(Peer peer) {
		String message = handshakeText(peer);
				
		server.sendToTCP(peer.getID(), KryoEndpoint.packMessage(prefix, message));
	}
	
	public abstract String handshakeText(Peer peer);

	@Override
	public void receiveTCP(Peer peer, byte[] message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receiveUDP(Peer peer, byte[] message) {
		// TODO Auto-generated method stub
		
	}

}
