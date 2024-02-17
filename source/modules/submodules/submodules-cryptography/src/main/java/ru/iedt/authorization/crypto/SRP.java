package ru.iedt.authorization.crypto;

import java.math.BigInteger;
import java.security.SecureRandom;

public class SRP {

    public static BigInteger n = new BigInteger(
        "00c037c37588b4329887e61c2da3324b1ba4b81a63f9748fed2d8a410c2fc21b1232f0d3bfa024276cfd88448197aae486a63bfca7b8bf7754dfb327c7201f6fd17fd7fd74158bd31ce772c9f5f8ab584548a99a759b5a2c0532162b7b6218e8f142bce2c30d7784689a483e095e701618437913a8c39c3dd0d4ca3c500b885fe3",
        16
    );
    public static BigInteger g = new BigInteger("2");
    public static BigInteger k = new BigInteger(H(n.toString(16) + g.toString(16)), 16);
    public static int nSize = n.bitLength();
    private static SecureRandom random;

    public static BigInteger generateServerPublicKey(String passwordVerifier, BigInteger secretServerKey) {
        return g.modPow(secretServerKey, n).add(k.multiply(new BigInteger(passwordVerifier, 16)));
    }

    public static BigInteger generateServerPrivateKey() {
        if (random == null) random = new SecureRandom();
        return new BigInteger(nSize, random).mod(n).add(BigInteger.ONE);
    }

    public static BigInteger getVerifier(String password, String salt) {
        return g.modPow(new BigInteger(H(salt + password), 16), n);
    }

    public static String getKeyServer(BigInteger serverPublicKey, BigInteger userPublicKey, BigInteger verifier, BigInteger serverPrivateKey) {
        return H(userPublicKey.multiply(verifier.modPow(new BigInteger(H(serverPublicKey.toString(16) + userPublicKey.toString(16)), 16), n)).modPow(serverPrivateKey, n).toString(16));
    }

    public static String getKeyClients(BigInteger serverPublicKey, BigInteger userPublicKey, BigInteger passwordHash, BigInteger userPrivateKey) {
        return H(serverPublicKey.subtract(k.multiply(g.modPow(passwordHash, n))).modPow(userPrivateKey.add(new BigInteger(H(serverPublicKey.toString(16) + userPublicKey.toString(16)), 16).multiply(passwordHash)), n).toString(16));
    }

    public static String H(String msg) {
        return Streebog.getHash(msg);
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    public static String getRandomString(int length) {
        if (random == null) random = new SecureRandom();
        byte[] token = new byte[length];
        random.nextBytes(token);
        return new BigInteger(1, token).toString(16).toLowerCase();
    }
}
