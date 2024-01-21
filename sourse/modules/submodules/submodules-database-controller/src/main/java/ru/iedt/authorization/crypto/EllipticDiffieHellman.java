package ru.iedt.authorization.crypto;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Класс реализующий протокол Диффи — Хеллмана на эллиптических кривых
 *
 * @version 1.0
 * @autor Роман Шиндеров 2020
 */
public class EllipticDiffieHellman {
    /**
     * Различные значения ЭК
     */
    static BigInteger curveQ =
            new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951");

    static BigInteger curveA =
            new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853948");
    static BigInteger curveB =
            new BigInteger("41058363725152142129326129780047268409114441015993725554835256314039467401291");
    static BigInteger x =
            new BigInteger("48439561293906451759052585252797914202762949526041747995844080717082404635286");
    static BigInteger y =
            new BigInteger("36134250956749795798585127919587881956611106672985015071877198253568414405109");
    static BigInteger z = new BigInteger("1");
    static BigInteger n =
            new BigInteger("115792089210356248762697446949407573529996955224135760342422259061068512044369");
    static int nSize = n.bitLength();

    static SecureRandom secure = new SecureRandom();
    /**
     * Приватный ключ
     */
    private final BigInteger securityKey;
    /**
     * Публичный ключ
     */
    private final ECDHPoint publicKey;
    /**
     * Конструктор с генерацией  приватного ключа
     */
    public EllipticDiffieHellman() {
        this.securityKey = new BigInteger(nSize, secure).mod(n).add(BigInteger.ONE);
        this.publicKey = new ECDHPoint(x, y, z, curveQ, curveA, curveB, n).multiply(securityKey);
    }
    /**
     * Конструктор с установленным значением {@link EllipticDiffieHellman#securityKey}
     */
    public EllipticDiffieHellman(BigInteger securityKey) {
        this.securityKey = securityKey;
        this.publicKey = new ECDHPoint(x, y, z, curveQ, curveA, curveB, n).multiply(securityKey);
    }
    /**
     * Функция получения приватного ключа
     */
    public BigInteger getSecurityKey() {
        return this.securityKey;
    }

    /**
     * Функция получения публичного ключа
     */
    public String getPublicKey() {
        return this.getPublicKeyX() + this.getPublicKeyY();
    }

    public String getPublicKeyX() {
        return this.getPublicKeyX(16);
    }

    public String getPublicKeyX(int radix) {
        return this.publicKey.getX().toString(radix);
    }

    public String getPublicKeyY() {
        return this.getPublicKeyY(16);
    }

    public String getPublicKeyY(int radix) {
        return this.publicKey.getY().toString(radix);
    }

    /**
     * Функция генерации общего секрета
     *
     * @return Общий секрет
     */
    public String getSecret(ECDHPoint publicKey) {
        publicKey.multiply(this.securityKey);
        return publicKey.getX().toString(16) + publicKey.getY().toString(16);
    }

    public String getSecret(BigInteger x, BigInteger y) {
        return this.getSecret(new ECDHPoint(x, y, z, curveQ, curveA, curveB, n));
    }

    public String getSecret(String x, String y, int radix) {
        try {
            return this.getSecret(new BigInteger(x, radix), new BigInteger(y, radix));
        } catch (Exception e) {
            return "";
        }
    }

    public String getSecret(String x, String y) {
        return this.getSecret(x, y, 16);
    }

    public BigInteger getSecretKeyValue() {
        return this.securityKey;
    }

    /**
     * Внутренний класс, для хранения данных о точке и операций с ней.
     *
     * @version 1.0
     * @autor Роман Шиндеров 2020
     */
    public static class ECDHPoint {
        private BigInteger x;
        private BigInteger y;
        private BigInteger z;
        private final BigInteger q;
        private final BigInteger a;
        private final BigInteger b;
        private final BigInteger module;

        /**
         * Конструктор создания точки {@link ECDHPoint#x},{@link ECDHPoint#y},{@link ECDHPoint#z} координаты точки
         * а {@link ECDHPoint#q},{@link ECDHPoint#a},{@link ECDHPoint#b},{@link ECDHPoint#module} различные параметры эллиптической  кривой
         */
        ECDHPoint(BigInteger x, BigInteger y, BigInteger z, BigInteger q, BigInteger a, BigInteger b, BigInteger n) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.q = q;
            this.a = a;
            this.b = b;
            module = n;
        }

        /**
         * Функция удвоения точки, аналогична вычислению {@link ECDHPoint#multiply(BigInteger)} с значением USERS_MODEL.xml
         *
         * @return ECDHPoint * USERS_MODEL.xml
         */
        public ECDHPoint twice() {
            BigInteger localX,
                    localY,
                    localZ,
                    three = new BigInteger("3"),
                    yz = this.y.multiply(this.z),
                    yz2 = yz.multiply(this.y).mod(this.q),
                    w = this.x.pow(2).multiply(three);
            if (!BigInteger.ZERO.equals(this.a)) {
                w = w.add(this.z.pow(2).multiply(this.a));
            }
            w = w.mod(this.q);
            localX = w.pow(2)
                    .subtract(this.x.shiftLeft(3).multiply(yz2))
                    .shiftLeft(1)
                    .multiply(yz)
                    .mod(this.q);
            localY = w.multiply(three)
                    .multiply(this.x)
                    .subtract(yz2.shiftLeft(1))
                    .shiftLeft(2)
                    .multiply(yz2)
                    .subtract(w.multiply(w.multiply(w)))
                    .mod(this.q);
            localZ = yz.multiply(yz.multiply(yz)).shiftLeft(3).mod(this.q);
            this.x = localX;
            this.y = localY;
            this.z = localZ;
            return this;
        }

        /**
         * Функция умножения точки на ЭК
         *
         * @param k то насколько мы умножим данную точку
         * @return ECDHPoint * K
         */
        public ECDHPoint multiply(BigInteger k) {
            BigInteger h = k.multiply(new BigInteger("3"));
            ECDHPoint negate = this.negate();
            for (int i = h.bitLength() - 2; i > 0; --i) {
                this.twice();
                if (h.testBit(i) != k.testBit(i)) {
                    this.add((h.testBit(i)) ? this : negate);
                }
            }
            return this;
        }

        /**
         * Функция сложения  точек на ЭК
         *
         * @param point Другая точка {@link ECDHPoint} которую мы будем прибавлять
         * @return ECDHPoint + point
         */
        public ECDHPoint add(ECDHPoint point) {
            BigInteger u =
                    point.y.multiply(this.z).subtract(this.y.multiply(point.z)).mod(this.q);
            BigInteger v =
                    point.x.multiply(this.z).subtract(this.x.multiply(point.z)).mod(this.q);
            if (BigInteger.ZERO.equals(v)) {
                if (BigInteger.ZERO.equals(u)) {
                    return this.twice();
                }
                return this.getInfinity();
            }
            BigInteger v2 = v.multiply(v), v3 = v2.multiply(v), three = new BigInteger("3");
            BigInteger xv2 = this.x.multiply(v2);
            BigInteger zu2 = u.multiply(u).multiply(this.z);
            BigInteger localX = zu2.subtract(xv2.shiftLeft(1))
                    .multiply(point.z)
                    .subtract(v3)
                    .multiply(v)
                    .mod(this.q);
            BigInteger localY = xv2.multiply(three)
                    .multiply(u)
                    .subtract(this.y.multiply(v3))
                    .subtract(zu2.multiply(u))
                    .multiply(point.z)
                    .add(u.multiply(v3))
                    .mod(this.q);
            BigInteger localZ = v3.multiply(this.z).multiply(point.z).mod(this.q);
            this.x = localX;
            this.y = localY;
            this.z = localZ;
            return this;
        }

        /**
         * Функция создания отрицательной точки
         */
        public ECDHPoint negate() {
            return new ECDHPoint(this.x, this.y.negate().mod(this.q), this.z, this.q, this.a, this.b, this.module);
        }

        /**
         * Возвращает эллиптическую кривую
         */
        public String toString() {
            return this.toString(10);
        }

        /**
         * Возвращает координату X ЭК
         */
        public BigInteger getX() {
            return reduce(this.x.multiply(this.z.modInverse(this.q)));
        }

        /**
         * Возвращает координату Y ЭК
         */
        public BigInteger getY() {
            return reduce(this.y.multiply(this.z.modInverse(this.q)));
        }

        /**
         * Возвращает эллиптическую кривую в указанной системе счисления
         */
        public String toString(int radix) {
            return "X:" + this.getX().toString(radix) + " Y:" + this.getY().toString(radix) + " Z:"
                    + reduce(this.z).toString(radix);
        }

        /**
         * TODO:barrettReduce x = x mod m (HAC 14.42)
         */
        public BigInteger reduce(BigInteger red) {
            return red.mod(this.q);
        }

        public ECDHPoint getInfinity() {
            return null;
        }
    }
}
