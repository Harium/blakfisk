package com.harium.blakfisk.protocol.common;

import java.util.LinkedHashSet;
import java.util.Set;

import com.harium.blakfisk.model.Peer;
import com.harium.blakfisk.model.data.MessageProtocol;
import com.harium.blakfisk.protocol.Protocol;
import com.harium.blakfisk.util.ByteMessageUtils;

public abstract class ProtocolImpl implements Protocol {
	
	protected byte[] prefix = ByteMessageUtils.EMPTY_BYTES;
	protected Set<Peer> peers = new LinkedHashSet<Peer>();
		
	public ProtocolImpl(String prefix) {
		super();
		this.prefix = prefix.getBytes();
	}
	
	public byte[] getPrefix() {
		return prefix;
	}

	@Override
	public void receive(byte messageProtocol, Peer peer, byte[] message) {
		if (MessageProtocol.TCP == messageProtocol) {
			receiveTCP(peer, message);	
		} else if (MessageProtocol.UDP == messageProtocol) {
			receiveUDP(peer, message);	
		}
	}

	@Override
	public void addPeer(Peer peer) {
		peers.add(peer);
	}

	@Override
	public void removePeer(Peer peer) {
		peers.remove(peer);
	}
	
}
