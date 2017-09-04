package com.harium.blakfisk;

import com.harium.blakfisk.backend.kryo.KryoServer;

public abstract class BlakFiskServer extends KryoServer {

	public BlakFiskServer() {
		super();
	}
	
	public BlakFiskServer(int port) {
		super(port);
	}

	public BlakFiskServer(int port, int udpPort) {
		super(port, udpPort);
	}

}
