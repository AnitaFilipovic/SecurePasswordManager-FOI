package spm.encdec;

import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import static org.junit.jupiter.api.Assertions.*;

public class EncDecTest {

    @Test
    public void testEncryptionAndDecryptionWithSamePassword() {
        EncDec encDec = new EncDec("password123");
        String testString = "Moje ime je Medvjedić Lino!";

        try {
            byte[] encryptedString = encDec.encrypt(testString);
            String decryptedString = encDec.decrypt(encryptedString);
            assertEquals(testString, decryptedString);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            fail("Dogodila se greška u kriptiranju/dekriptiranju.");
        }
    }

    @Test
    public void testEncryptionAndDecryptionWithDifferentPassword() {
        EncDec encDec1 = new EncDec("password123");
        EncDec encDec2 = new EncDec("password456");
        String testString = "Moje ime je Medvjedić Lino!";

        try {
            byte[] encryptedString = encDec1.encrypt(testString);
            String decryptedString = encDec2.decrypt(encryptedString);
            assertNotEquals(testString, decryptedString);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            assertTrue(true, "Dogodila se greška u kriptiranju/dekriptiranju.");
        }
    }
}


