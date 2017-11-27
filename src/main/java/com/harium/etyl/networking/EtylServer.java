package com.harium.etyl.networking;

import com.harium.etyl.networking.kryo.KryoServer;
import com.harium.etyl.networking.model.Peer;

public class EtylServer extends KryoServer {

	public EtylServer(int port) {
		super(port);
	}

	public EtylServer(int port, int udpPort) {
		super(port, udpPort);
	}

	@Override
	public void joinPeer(Peer peer) {

	}

	@Override
	public void leftPeer(Peer peer) {

	}
}
