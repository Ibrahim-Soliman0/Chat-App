package org.client.chatapp.ui.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

public class EncryptionUtil {
    private static SecretKeySpec secretKey;
    private static void setKey() {
        try {
            byte[] mac = null;
            var en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface ni = en.nextElement();
                mac = ni.getHardwareAddress();
                if (mac != null && mac.length > 0)
                    break;
            }
            if (mac == null)
                mac = "AhmedRamadan1234".getBytes();

            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            mac = sha.digest(mac);
            mac = Arrays.copyOf(mac, 16);
            secretKey = new SecretKeySpec(mac, "AES");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String encrypt(String strToEncrypt) {
        try {
            setKey();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }
    public static String decrypt(String strToDecrypt) {
        try {
            setKey();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
}
