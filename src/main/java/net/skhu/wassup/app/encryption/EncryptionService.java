package net.skhu.wassup.app.encryption;

public interface EncryptionService {

    String encrypt(String input);

    boolean isMatch(String input, String encrypted);

}
