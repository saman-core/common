package io.samancore.common.transformer;

public interface Encrypt {

    byte[] encrypt(String value);

    String decrypt(byte[] value);
}
