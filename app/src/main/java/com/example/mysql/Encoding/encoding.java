package com.example.mysql.Encoding;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class encoding {
    public static String hashPassword(String pass)
    {
        try{

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encoding = digest.digest(pass.getBytes());

            StringBuilder stringbiBuilder = new StringBuilder();

            for (byte b : encoding)
            {
                String hex = Integer.toHexString(0xff & b);

                if (hex.length() == 1)
                {
                    stringbiBuilder.append("0");
                }
                stringbiBuilder.append(hex);
            }
            return stringbiBuilder.toString();
        }catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
