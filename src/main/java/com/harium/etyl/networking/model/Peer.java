package com.harium.etyl.networking.model;

import java.util.HashMap;
import java.util.Map;

public abstract class Peer {

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

    public abstract int getId();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Peer peer = (Peer) o;

        return getId() == peer.getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }
}
