package com.harium.etyl.networking.model;

import java.util.HashMap;
import java.util.Map;

public abstract class Peer {

    private Map<String, String> values;
    private Object websocket;

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

    public abstract int getId();

}
