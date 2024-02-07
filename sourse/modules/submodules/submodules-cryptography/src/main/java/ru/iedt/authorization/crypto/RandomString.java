package ru.iedt.authorization.crypto;


import java.security.SecureRandom;

public class RandomString {
    static SecureRandom random = new SecureRandom();
    static String AlphaNumericStr = "0123456789ABCDEF";

    public static String getRandomString(int size) {
        StringBuilder result = new StringBuilder(size);
        for (int g = 0; g < size; g++) {
            int ch = random.nextInt(0, AlphaNumericStr.length());
            result.append(AlphaNumericStr.charAt(ch));
        }
        return result.toString();
    }

}
