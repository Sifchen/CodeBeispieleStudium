package rsa;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;

/**
 * Ein öffentlicher Schlüssel. Stellt Methoden zum Verschlüsseln von Nachrichten
 * und zum Überprüfen von Signaturen zur Verfügung.
 *
 * @author kar(Übungsleiter), mhe(Übungsleiter), Daniel Pigotow (minf103095)
 *
 */
public class PublicKey {

    /** Größter in einem vorzeichenlosen Int ablegbarer Wert */
    public static final BigInteger U_INT_MAX = BigInteger.valueOf(4294967295L);

    /** RSA-Modul */
    private final BigInteger n;

    /** Exponent */
    private final BigInteger e;

    /**
     * Konstruktor.
     *
     * @param n RSA-Modul
     * @param e Exponent
     * @pre n != null
     * @pre e != null
     * @pre n &gt; U_INT_MAX, damit Signaturen überprüft werden können
     * @pre e &gt; 0
     */
    public PublicKey(BigInteger n, BigInteger e) {
        assert n != null;
        assert e != null;
        assert n.compareTo(U_INT_MAX) > 0;
        assert e.signum() > 0;

        this.n = n;
        this.e = e;
    }

    /**
     * @return RSA-Modul N
     */
    public BigInteger getN() {
        return n;
    }

    /**
     * @return Exponent e
     */
    public BigInteger getE() {
        return e;
    }

    /**
     * Verschlüsselt eine Nachricht, die nur aus einer einzelnen Zahl besteht.
     *
     * @param m Nachricht (Klartext-Zahl)
     * @return verschlüsselte Nachricht
     * @pre m != null
     * @pre m &ge; 0
     * @pre m &lt; n
     */
    public BigInteger encrypt(BigInteger m) {
        assert m != null;
        assert m.compareTo(BigInteger.ZERO) >= 0;
        assert m.compareTo(this.n) < 0;
        return m.modPow(this.e, this.n);
    }

    /**
     * Verschlüsselt eine Nachricht, die aus mehreren Zahlen besteht (ein
     * Byte-Array).
     *
     * @param m Nachricht (Klartext)
     * @return verschlüsselte Nachricht
     * @pre m != null
     */
    public BigInteger[] encrypt(byte[] m) {
        assert m != null;

        BigInteger[] retArr = new BigInteger[m.length];

        for (int i = 0; i < retArr.length; i++) {
            retArr[i] = encrypt(BigInteger.valueOf(Byte.toUnsignedInt(m[i])));
        }
        return retArr;
    }

    /**
     * Entpackt den Hash-Wert aus einer Signatur.
     *
     * @param s Signatur
     * @return in s verpackter Hash-Wert
     * @pre s != null
     * @pre s &ge; 0
     * @pre s &lt; n
     */
    public BigInteger getHashCode(BigInteger s) {
        assert s != null;
        assert s.compareTo(BigInteger.ZERO) >= 0;
        assert s.compareTo(this.n) < 0;
        return encrypt(s);
    }

    /**
     * Überprüft die Signatur einer Nachricht.
     *
     * Diese Methode verwendet die kryptographisch unsichere Hash-Funktion
     * Arrays.hashCode(byte[]).
     *
     * @param m         Nachricht
     * @param signature zu überprüfende Signatur
     * @return true, wenn signature die Signatur von m ist, ansonsten false
     * @pre m != null
     * @pre signature != null
     * @pre signature &ge; 0
     * @pre signature &lt; n
     */
    public boolean checkSignature(byte[] m, BigInteger signature) {
        assert m != null;
        assert signature != null;
        assert signature.compareTo(BigInteger.ZERO) >= 0;
        assert signature.compareTo(this.n) < 0;
        long hashActual = Arrays.hashCode(m);
        hashActual = hashActual - Integer.MIN_VALUE; 
        BigInteger hashExpected = this.getHashCode(signature);
        return (BigInteger.valueOf(hashActual).equals(hashExpected));
    }

    @Override
    public int hashCode() {
        return Objects.hash(n, e);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof PublicKey)) {
            return false;
        }
        PublicKey other = (PublicKey) obj;
        return Objects.equals(n, other.n) && Objects.equals(e, other.e);
    }

    @Override
    public String toString() {
        return "PublicKey [n=" + n + ", e=" + e + "]";
    }

}
