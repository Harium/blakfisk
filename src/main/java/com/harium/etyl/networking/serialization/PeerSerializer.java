package com.harium.etyl.networking.serialization;

public interface PeerSerializer<T> {
    String serialize(T peer);

    T deserialize(String text);
}
