package com.harium.etyl.networking.model;

import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryonet.Connection;

public class Peer extends Connection {
		
	private Map<String, String> values;

	public Peer() {
		super();
		values = new HashMap<String, String>();
	}

	public Map<String, String> getValues() {
		return values;
	}

	public void setValues(Map<String, String> values) {
		this.values = values;
	}
	
}
