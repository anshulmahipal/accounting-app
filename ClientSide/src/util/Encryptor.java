package util;

import java.util.Base64;

public class Encryptor
{
    public static String encode(String password)
    {
        String key = "encoding";
        return base64Encode(xorWithKey(password.getBytes(),key.getBytes()));
    }

    public static String decode(String password)
    {
        String key = "encoding";
        return new String(xorWithKey(base64Decode(password),key.getBytes()));
    }

    private static byte[] xorWithKey(byte[] password, byte[] key)
    {
        byte[] out = new byte[password.length];

        for(int i = 0;i<password.length;i++)
            out[i] = (byte)(password[i]^key[i%key.length]);

        return out;
    }

    private static String base64Encode(byte[] bytes)
    {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private static byte[] base64Decode(String password)
    {
        return Base64.getDecoder().decode(password);
    }
}
