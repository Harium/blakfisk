package com.harium.etyl.networking.protocol;

import com.harium.etyl.networking.kryo.KryoServer;
import com.harium.etyl.networking.kryo.KryoEndpoint;
import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.protocol.common.ProtocolImpl;

public abstract class HandshakerProtocol extends ProtocolImpl implements Protocol {

	protected KryoServer server;
	
	public HandshakerProtocol(String prefix, KryoServer server) {
		super(prefix);
		this.server = server;
	}
	
	public void addPeer(Peer peer) {
		String message = handshakeText(peer);
				
		server.sendToTCP(peer.getId(), KryoEndpoint.packMessage(prefix, message));
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
