package com.harium.etyl.networking.kryo;

import com.harium.etyl.networking.model.Peer;

public class KryoPeer extends Peer {

    private String id;
    private KryoConnection connection;

    public KryoPeer() {
        connection = new KryoConnection();
        id = Integer.toString(connection.getID());
    }

    public KryoConnection getConnection() {
        return connection;
    }

    public String getId() {
        return id;
    }
}
