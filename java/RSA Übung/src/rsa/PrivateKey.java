package rsa;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;

/**
 * Ein privater Schlüssel. Enthält eine Referenz auf den zugehörigen
 * öffentlichen Schlüssel. Stellt Methoden zum Entschlüsseln von Nachrichten und
 * zum Erstellen von Signaturen zur Verfügung.
 *
 * @author kar(Übungsleiter), mhe(Übungsleiter), Daniel Pigotow (minf103095)
 *
 */
public class PrivateKey {

    /** Größter in einem vorzeichenlosen Int ablegbarer Wert */
    public static final BigInteger U_INT_MAX = BigInteger.valueOf(4294967295L);

    /** RSA-Modul */
    private final BigInteger n;

    /** Exponent */
    private final BigInteger d;

    /** zugehöriger öffentlicher Schlüssel */
    private final PublicKey publicKey;

    /**
     * Konstruktor, der die zu nutzenden Werte aus den Parametern übernimmt.
     *
     * @param publicKey zu nutzender öffentlicher Schlüssel
     * @param d         Exponent des privaten Schlüssels
     * @pre publicKey != null
     * @pre d != null
     * @pre d &gt; 0
     */
    public PrivateKey(PublicKey publicKey, BigInteger d) {
        assert publicKey != null;
        assert d != null; 
        assert d.signum() > 0;

        this.publicKey = publicKey;
        this.n = publicKey.getN();
        this.d = d;
    }

    /**
     * Konstruktor, der die zu nutzenden Werte aus den Parametern generiert.
     *
     * @param p Primzahl p
     * @param q Primzahl q
     * @param e Exponent des zu generierenden öffentlichen Schlüssels
     * @pre p != null
     * @pre q != null
     * @pre e != null
     * @pre p &gt; 0
     * @pre q &gt; 0
     * @pre p und q sind Primzahlen
     * @pre p und q sind ungleiche Zahlen
     * @pre p * q &gt; U_INT_MAX, damit Signaturen erstellt werden können
     * @pre e &gt; 0
     * @post d &gt; 0
     */
    public PrivateKey(BigInteger p, BigInteger q, BigInteger e) {
        assert p != null;
        assert q != null;
        assert e != null;
        assert p.compareTo(BigInteger.ZERO) > 0;
        assert q.compareTo(BigInteger.ZERO) > 0;
        assert isPrime(p) && isPrime(q);
        assert p.compareTo(q) != 0;
        assert p.multiply(q).compareTo(U_INT_MAX) > 0;
        assert e.compareTo(BigInteger.ZERO) > 0;

        this.n = p.multiply(q);
        BigInteger pMinusOne = p.subtract(BigInteger.ONE);
        BigInteger qMinusOne = q.subtract(BigInteger.ONE);
        BigInteger phi = pMinusOne.multiply(qMinusOne); // p -1 q -1 ==> phi
        this.publicKey = new PublicKey(this.n, e);
        this.d = computeD(e, phi);
        assert this.d.compareTo(BigInteger.ZERO) > 0; 
    }

    /**
     * Gibt zurück, ob die übergebene Zahl eine Primzahl ist.
     *
     * @param i zu überprüfende Zahl
     * @return true, wenn i eine Primzahl ist, ansonsten false
     * @pre i != null
     * @pre i &ge; 0
     */
    public static boolean isPrime(BigInteger i) {
        assert i != null;
        assert i.signum() >= 0;

        if (i.equals(BigInteger.ONE)) {
            return false;
        }
        if (i.equals(new BigInteger("2"))) {
            return true;
        }
        if (i.mod(new BigInteger("2")).equals(BigInteger.ZERO)) {
            return false;
        }

        BigInteger counter = new BigInteger("3");
        while ((counter.multiply(counter).compareTo(i) <= 0)) {
            if (i.mod(counter).equals(BigInteger.ZERO)) {
                return false;
            }
            counter = counter.add(new BigInteger("2"));
        }
        return true;
    }

    /**
     * Bestimmt den ersten Bézout-Koeffizienten s für die zwei übergebenen Zahlen.
     *
     * Lemma von Bézout: ggt(a,b) = s * a + t * b
     *
     * @param a erste Zahl
     * @param b zweite Zahl
     * @return erster Bézout-Koeffizient s bezüglich a und b
     * @pre a != null
     * @pre b != null
     * @pre a &gt; 0
     * @pre b &gt; 0
     */
    public static BigInteger firstBezout(BigInteger a, BigInteger b) {
        assert a != null;
        assert b != null;
        assert a.compareTo(BigInteger.ZERO) > 0;
        assert b.compareTo(BigInteger.ZERO) > 0;
        BigInteger s;
        BigInteger oldS;
        BigInteger t;
        BigInteger oldT;
        BigInteger r;
        BigInteger oldR;

        if (a.compareTo(b) >= 0) {
            s = BigInteger.ZERO; // Fallunterscheidung notwendig wenn a>b
            oldS = BigInteger.ONE;
            t = BigInteger.ONE;
            oldT = BigInteger.ZERO;
            r = b;
            oldR = a;
        } else {
            s = BigInteger.ONE; // Fallunterscheidung notwendig wenn a<b
            oldS = BigInteger.ZERO;
            t = BigInteger.ZERO;
            oldT = BigInteger.ONE;
            r = a;
            oldR = b;
        }

        while ((r.compareTo(BigInteger.ZERO) != 0)) {
            BigInteger quotient = oldR.divide(r);

            BigInteger temp = oldR;
            oldR = r;
            r = temp.subtract(quotient.multiply(r));

            temp = oldS;
            oldS = s;
            s = temp.subtract(quotient.multiply(s));

            temp = oldT;
            oldT = t;
            t = temp.subtract(quotient.multiply(t));
        }

        return oldS;
    }

    /**
     * Berechnet den Exponenten d aus den Werten e und phi mit Hilfe der Methode
     * firstBezout.
     *
     * @param e   Der öffentliche Exponent e
     * @param phi Der Wert phi
     * @return resultierendes d
     * @pre e != null
     * @pre phi != null
     * @pre e &gt; 0
     * @pre phi &gt; 0
     */
    public static BigInteger computeD(BigInteger e, BigInteger phi) {
        assert e != null;
        assert phi != null;
        assert e.compareTo(BigInteger.ZERO) > 0;
        assert phi.compareTo(BigInteger.ZERO) > 0;

        return firstBezout(e, phi).mod(phi);
    }

    /**
     * @return RSA-Modul N
     */
    public BigInteger getN() {
        return n;
    }

    /**
     * @return Exponent d
     */
    public BigInteger getD() {
        return d;
    }

    /**
     * @return zugehöriger öffentlicher Schlüssel
     */
    public PublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * Entschlüsselt eine Nachricht, die aus einer einzelne Zahl besteht.
     *
     * @param c verschlüsselte Nachricht
     * @return entschlüsselte Nachricht (Klartext-Zahl)
     * @pre c != null
     * @pre c &ge; 0
     * @pre c &lt; n
     */
    public BigInteger decrypt(BigInteger c) {
        assert c != null;
        assert c.compareTo(BigInteger.ZERO) >= 0;
        assert c.compareTo(this.n) < 0;
        return c.modPow(this.d, this.n);
    }

    /**
     * Entschlüsselt eine Nachricht aus mehreren Zahlen (ein BigInteger-Array).
     *
     * @param c verschlüsselte Nachricht
     * @return entschlüsselte Nachricht (Klartext-Bytes)
     * @pre c != null
     * @pre Jedes Element aus c ist &ge; 0 und &lt; n
     */
    public byte[] decrypt(BigInteger[] c) {
        assert c != null;
        for (int i = 0; i < c.length; i++) {
            assert c[i].signum() >= 0;
            assert c[i].compareTo(n) < 0;
        }
        byte[] decResult = new byte[c.length];
        BigInteger[] decArr = new BigInteger[c.length];
        for (int i = 0; i < decArr.length; i++) {
            decArr[i] = decrypt(c[i]);
            decResult[i] = decArr[i].byteValue();
        }
        return decResult;
    }

    /**
     * Erstellt die Signatur für einen Hash-Wert.
     *
     * @param h zu signierender Hash-Wert
     * @return Signatur von h
     * @pre h != null
     * @pre h &ge; 0
     * @pre h &lt; n
     */
    public BigInteger sign(BigInteger h) {
        assert h != null;
        assert h.signum() >= 0;
        assert h.compareTo(this.n) < 0;
        return decrypt(h);
    }

    /**
     * Erstellt die Signatur für eine Nachricht.
     *
     * Diese Methode verwendet die kryptographisch unsichere Hash-Funktion
     * Arrays.hashCode(byte[]).
     *
     * @param m Nachricht
     * @return Signatur
     * @pre m != null
     */
    public BigInteger hashAndSign(byte[] m) {
        assert m != null;
        long hash = Arrays.hashCode(m);
        hash = hash - Integer.MIN_VALUE;
        BigInteger toSign = BigInteger.valueOf(hash);
        return sign(toSign);
    }

    @Override
    public int hashCode() {
        return Objects.hash(d, n, publicKey);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof PrivateKey)) {
            return false;
        }
        PrivateKey other = (PrivateKey) obj;
        return Objects.equals(d, other.d)
                && Objects.equals(n, other.n)
                    && Objects.equals(publicKey, other.publicKey);
    }

    @Override
    public String toString() {
        return "PrivateKey [n=" + n + ", d=" + d + ", publicKey=" + publicKey + "]";
    }

}
