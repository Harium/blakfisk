package com.harium.etyl.networking.protocol;

import com.harium.etyl.networking.model.Peer;

public interface Protocol {
	public byte[] getPrefix();
	
	public void addPeer(Peer peer);
	public void removePeer(Peer peer);
	
	public void receiveTCP(Peer peer, byte[] message);
	public void receiveUDP(Peer peer, byte[] message);
	public void receive(byte messageProtocol, Peer peer, byte[] message);
}
