package com.harium.etyl.networking;

import com.harium.etyl.networking.kryo.KryoServer;

public abstract class EtylServer extends KryoServer {

	public EtylServer(int port) {
		super(port);
	}

	public EtylServer(int port, int udpPort) {
		super(port, udpPort);
	}

}
