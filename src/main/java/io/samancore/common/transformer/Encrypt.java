package io.samancore.common.transformer;

public interface Encrypt {

    byte[] encrypt(Object value);

    String decrypt(byte[] value);
}
