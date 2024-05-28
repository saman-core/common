package io.samancore.common.transformer;

public interface Encrypt {

    byte[] encrypt(String value);

    String decrypt(byte[] value);

    byte[] encryptAsymmetric(String value);

    String decryptAsymmetric(byte[] value);
}
