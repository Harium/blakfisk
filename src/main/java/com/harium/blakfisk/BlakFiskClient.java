package com.harium.blakfisk;

import com.harium.blakfisk.backend.kryo.KryoClient;

public class BlakFiskClient extends KryoClient {
	
	public BlakFiskClient(String host, int tcpPort) {
		super(host, tcpPort);
	}
	
	public BlakFiskClient(String host, int tcpPort, int udpPort) {
		super(host, tcpPort, udpPort);
	}
	
}
