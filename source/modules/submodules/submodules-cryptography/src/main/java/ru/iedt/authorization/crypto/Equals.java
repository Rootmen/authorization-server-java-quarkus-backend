package ru.iedt.authorization.crypto;

public class Equals {
    public static boolean equals(String a, String b) {
        int result = 0;
        if (a.length() != b.length()) {
            return false;
        }
        for (int g = 0; g < a.length(); g++) {
            result |= a.charAt(g) ^ b.charAt(g);
        }
        return result == 0;
    }
}
