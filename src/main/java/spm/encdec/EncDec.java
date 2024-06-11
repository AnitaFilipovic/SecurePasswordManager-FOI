package spm.encdec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class EncDec {
    private static final String ALGORITHM = "Blowfish";

    private Cipher encryptingCipher;
    private Cipher decryptingCipher;
    private boolean isWorking;

    public EncDec(String password) {
        try {
            Key key = new SecretKeySpec(password.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            this.encryptingCipher = Cipher.getInstance(ALGORITHM);
            this.encryptingCipher.init(Cipher.ENCRYPT_MODE, key);
            this.decryptingCipher = Cipher.getInstance(ALGORITHM);
            this.decryptingCipher.init(Cipher.DECRYPT_MODE, key);
            this.isWorking = true;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            this.isWorking = false;
        }
    }

    public byte[] encrypt(String data) throws IllegalBlockSizeException, BadPaddingException {
        return this.encryptingCipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }

    public String decrypt(byte[] data) throws IllegalBlockSizeException, BadPaddingException {
        byte[] decryptedBytes = this.decryptingCipher.doFinal(data);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public boolean isWorking() {
        return this.isWorking;
    }
}


