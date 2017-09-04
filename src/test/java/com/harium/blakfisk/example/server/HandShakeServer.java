package com.harium.blakfisk.example.server;

import com.harium.blakfisk.example.client.SimpleClientProtocol;
import com.harium.blakfisk.model.Peer;
import com.harium.blakfisk.BlakFiskServer;

public class HandShakeServer extends BlakFiskServer {

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
