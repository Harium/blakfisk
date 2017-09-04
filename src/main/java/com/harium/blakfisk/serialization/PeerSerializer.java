package com.harium.blakfisk.serialization;

public interface PeerSerializer<T> {
    String serialize(T peer);

    T deserialize(String text);
}
