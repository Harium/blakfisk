package com.harium.etyl.networking.kryo;

import com.esotericsoftware.kryonet.Connection;
import com.harium.etyl.networking.model.Peer;

public class KryoPeer extends Peer {

    private int id;
    private Connection connection;

    public KryoPeer(int id) {
        this.id = id;
    }

    public KryoPeer(Connection connection) {
        this.connection = connection;
        id = connection.getID();
    }

    public Connection getConnection() {
        return connection;
    }

    public int getId() {
        return id;
    }
}
