package com.harium.etyl.networking.protocol.common;

import java.util.Collection;

import com.harium.etyl.networking.kryo.KryoServer;
import com.harium.etyl.networking.kryo.KryoEndpoint;
import com.harium.etyl.networking.model.Peer;

public abstract class ServerProtocol extends ProtocolImpl {

	protected KryoServer server;
	
	public ServerProtocol(String prefix, KryoServer server) {
		super(prefix);
		this.server = server;
	}
	
	public void sendTCP(int peerId, String message) {
		server.sendToTCP(peerId, KryoEndpoint.packMessage(prefix, message));
	}
	
	public void sendTCP(Peer peer, String message) {
		this.sendTCP(peer.getId(), message);
	}
	
	public void sendTCPNoPrefix(Peer peer, String message) {
		server.sendToTCP(peer.getId(), KryoEndpoint.packRawMessage(message));
	}

	public void sendTCPtoAll(String message) {
		server.sendToAllTCP(KryoEndpoint.packMessage(prefix, message));
	}
	
	public void sendTCPtoAll(Collection<Integer> peerIds, String message) {
		for(Integer peer: peerIds) {
			this.sendTCP(peer, message);
		}
	}
	
	public void sendTCPtoAllExcept(Peer peer, String message) {
		server.sendToAllExceptTCP(peer.getId(), KryoEndpoint.packMessage(prefix, message));
	}
	
	public void sendUDP(int peerId, String message) {
		server.sendToUDP(peerId, KryoEndpoint.packMessage(prefix, message));
	}
	
	public void sendUDP(Peer peer, String message) {
		sendUDP(peer.getId(), message);
	}
		
	public void sendUDPNoPrefix(Peer peer, String message) {
		server.sendToUDP(peer.getId(), KryoEndpoint.packRawMessage(message));
	}
	
	public void sendUDPtoAll(String message) {
		server.sendToAllUDP(KryoEndpoint.packMessage(prefix, message));
	}
	
	public void sendUDPtoAll(Collection<Integer> peers, String message) {
		for(Integer peer: peers) {
			this.sendUDP(peer, message);
		}
	}
		
	public void sendUDPtoAllExcept(Peer peer, String message) {
		server.sendToAllExceptUDP(peer.getId(), KryoEndpoint.packMessage(prefix, message));
	}
	
}