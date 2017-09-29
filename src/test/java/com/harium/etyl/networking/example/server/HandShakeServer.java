package com.harium.etyl.networking.example.server;

import com.harium.etyl.networking.example.client.SimpleClientProtocol;
import com.harium.etyl.networking.model.Peer;
import com.harium.etyl.networking.EtylServer;

public class HandShakeServer extends EtylServer {

	private SimpleServerProtocol listener;

	public HandShakeServer(int port, int udpPort) {
		this(port);
		this.udpPort = udpPort;
	}
	
	public HandShakeServer(int port) {
		super(port);
		
		name = "HandShake Server";
		
		setHandshaker(new SimpleHandshaker(this));
		listener = new SimpleServerProtocol(SimpleClientProtocol.DEFAULT_PREFIX, this);
		addProtocol(SimpleClientProtocol.DEFAULT_PREFIX, listener);
	}
		
	@Override
	public void joinPeer(Peer peer) {
		System.out.println("Joined: "+peer.getID());
		listener.addPeer(peer);
	}

	@Override
	public void leftPeer(Peer peer) {
		System.out.println("Left: "+peer.getID());
		listener.removePeer(peer);
	}
	
}
