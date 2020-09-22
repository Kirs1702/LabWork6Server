package main.entity;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

public class HashMachine {
    public static String generateSalt() {
        int length = 5 + (int)(Math.random()*20);
        Random r = new Random();
        String s = r.ints(48, 122)
                .filter(i -> (i < 57 || i > 65) && (i < 90 || i > 97))
                .mapToObj(i -> (char) i)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
        return s;
    }
    public static String addSaltPepperAndHash(String password, String salt, String pepper) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String s1 = password.substring(0,Math.round((float)password.length()/3));
        String s2 = password.substring((int)((float)password.length()/3+1));
        String result = s1+salt+s2;
        s1 = result.substring(0,result.length()-(Math.round((float)result.length()/5)));
        s2 = result.substring(result.length()-(Math.round((float)result.length()/5))+1);
        result = s1+pepper+s2;
        MessageDigest md = MessageDigest.getInstance("SHA-384");
        byte[] b = md.digest(result.getBytes("UTF-8"));

        return new String(b);
    }



}
