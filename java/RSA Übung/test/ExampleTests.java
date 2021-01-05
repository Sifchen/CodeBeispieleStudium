import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.junit.Assert;

import rsa.PrivateKey;
import rsa.FileIO;

/**
 * JUnit-Testklasse mit einigen Beispieltests.
 * 
 * @author kar, mhe
 * 
 */
public class ExampleTests {

    /**
     * Hilfsmethode zum einfacheren Testen: Erzeugt eine BigInteger-Instanz aus dem übergebenen
     * long-Wert.
     * 
     * @param l Wert, aus dem die BigInteger-Instanz erzeugt werden soll
     * @return BigInteger-Instanz
     */
    public static BigInteger BI(long l) {
        return BigInteger.valueOf(l);
    }

    /**
     * Hilfsmethode zum einfacheren Testen: Wandelt ein Byte-Array mittels UTF-8-Kodierung in einen
     * String um.
     * 
     * @param b umzuwandelndes Byte-Array
     * @return resultierender String
     */
    public static String bytesToString(byte[] b) {
        return new String(b, StandardCharsets.UTF_8);
    }

    /**
     * Hilfsmethode zum einfacheren Testen: Wandelt einen String mittels UTF-8-Kodierung in ein
     * Byte-Array um.
     * 
     * @param s umzuwandelnder String
     * @return resultierendes Byte-Array
     */
    public static byte[] stringToBytes(String s) {
        return s.getBytes(StandardCharsets.UTF_8);
    }

    @Test
    public void test_isPrime() {
        //Von Wikipedia
        int [] primes = {2,     3,     5,     7,    11,    13,    17,    19,    23,    29,    31,    37,    41,    43,
                47,    53,    59,    61,    67,    71,    73,    79,    83,    89,    97,   101,   103,   107, 109};
        
        List<Integer> primesList = new ArrayList<>(); //Fuer Contains Methode 
        for (int i = 0; i < primes.length; i++) {
            primesList.add(primes[i]);
        }
        for (int i = 0; i <= 110; i++ ) {
            if (primesList.contains(i)) {
                Assert.assertTrue(String.valueOf(i), PrivateKey.isPrime(BI(i)));
            } else {
                Assert.assertFalse(String.valueOf(i), PrivateKey.isPrime(BI(i)));
            }
        }
        // Tests für isPrime
        Assert.assertTrue("Ist 2 prim?", PrivateKey.isPrime(BI(2)));
        Assert.assertTrue("Ist 13 prim?", PrivateKey.isPrime(BI(13)));
        Assert.assertFalse("Ist 42 prim?", PrivateKey.isPrime(BI(42)));
        Assert.assertFalse("Ist 300040 prim?", PrivateKey.isPrime(BI(300040)));
        Assert.assertTrue("Ist 5107 prim?", PrivateKey.isPrime(BI(5107)));
        //Added
        Assert.assertTrue("Ist 2147483647 prim (Mersenne)?", PrivateKey.isPrime(BI(2147483647)));
        
    }
    @Test(expected = AssertionError.class)
    public void test_isPrime_Negative() {
        BigInteger test = new BigInteger("-1");
        PrivateKey.isPrime(test);
    }

    @Test
    public void test_firstBezout_computeD() {
        // Tests für firstBezout, computeD und den PrivateKey-Konstruktor

        {
            BigInteger p = BigInteger.valueOf(50021);
            BigInteger q = BigInteger.valueOf(100003);
            BigInteger e = BigInteger.valueOf(65537);

            BigInteger phi = BI(5002100040L);
            BigInteger firstBezout = PrivateKey.firstBezout(e, phi);
            BigInteger d = PrivateKey.computeD(e, phi);

            Assert.assertEquals("Beispielaufruf von firstBezout", BI(-1131515527L), firstBezout);
            Assert.assertEquals("aus computeD resultierendes d", BI(3870584513L), d);
            Assert.assertEquals("resultierendes d im PrivateKey", BI(3870584513L),
                    new PrivateKey(p, q, e).getD());

            // Aufruf von modInverse ist nur zum Testen erlaubt:
            Assert.assertEquals("Vergleich von d mit Aufruf von modInverse", e.modInverse(phi), d);
        }
    }
    @Test
    public void test_firstBezout_phi_e_switched() {
        
        {
            BigInteger p = BigInteger.valueOf(5197);
            BigInteger q = BigInteger.valueOf(6571);
            BigInteger e = BigInteger.valueOf(65537);
            
            BigInteger phi = BI(34137720L);
            BigInteger firstBezout = PrivateKey.firstBezout(phi, e);
            BigInteger d = PrivateKey.computeD(e, phi);
            
            Assert.assertEquals("Beispielaufruf von firstBezout", BI(-30340), firstBezout);
            // Aufruf von modInverse ist nur zum Testen erlaubt:
            Assert.assertEquals("Vergleich von d mit Aufruf von modInverse", e.modInverse(phi), d);
        }
    }

    // Aufruf von firstBezout mit falschem Parameter soll Assertion auslösen:
    @Test(expected = AssertionError.class)
    public void test_firstBezout_wrongParameter() {
        PrivateKey.firstBezout(BI(-1), BI(-1));
    }

    @Test
    public void test_encryption_decryption() {
        // Tests für encrypt und decrypt

        PrivateKey key = new PrivateKey(BI(50021), BI(100003), BI(65537));

        {
            BigInteger c = key.getPublicKey().encrypt(BI(7));

            Assert.assertEquals("Verschlüsselung einer 7", BI(915380488L), c);
            Assert.assertEquals("Entschlüsselung zu einer 7", BI(7), key.decrypt(c));
        }

        {
            BigInteger[] c = key.getPublicKey().encrypt(stringToBytes("Hallo Welt!"));
            BigInteger[] expected = { BI(2851092477L), BI(3045658702L), BI(481445028L),
                    BI(481445028L), BI(4318764185L), BI(847941511L), BI(2362042497L),
                    BI(4389819658L), BI(481445028L), BI(4746565979L), BI(1956342872L) };

            Assert.assertArrayEquals("Verschlüsselung von: Hallo Welt!", expected, c);
            Assert.assertEquals("Entschlüsselung zu: Hallo Welt!", "Hallo Welt!",
                    bytesToString(key.decrypt(c)));
        }
    }

    @Test
    public void test_signature() {
        // Tests für hashAndSign und checkSignature

        PrivateKey key = new PrivateKey(BI(50021), BI(100003), BI(65537));

        {
            BigInteger s = key.hashAndSign(stringToBytes("Hallo Welt!"));

            Assert.assertEquals("Signierung von: Hallo Welt!", BI(4875086542L), s);
            Assert.assertTrue("Signatur-Überprüfung bei: Hallo Welt!",
                    key.getPublicKey().checkSignature(stringToBytes("Hallo Welt!"), s));
        }
    }

    @Test
    public void test_BigIntegerToByte_lan_fits() {
        BigInteger test = new BigInteger("2019");
        byte[] result = FileIO.bigIntegerToBytes(test, 2);
        byte[] actual = { 7, -29 };
        Assert.assertTrue(Arrays.equals(result, actual));
    }

    @Test(expected = AssertionError.class)
    public void test_BigIntegerToByte_len_too_small() {
        BigInteger test = new BigInteger("20192019");
        byte[] result = FileIO.bigIntegerToBytes(test, 2);
    }

    @Test
    public void test_BigIntegerToByte_len_too_big() {
        BigInteger test = new BigInteger("2019");
        byte[] result = FileIO.bigIntegerToBytes(test, 5);
        byte[] actual = { 0, 0, 0, 7, -29 };
        Assert.assertTrue(Arrays.equals(result, actual));
    }
    
    
    @Test
    public void test_fileio() throws IOException, InterruptedException {
        String results = "test/results/"; // Ordner, in den die Testergebnisse geschrieben werden
        String expected = "test/expected/"; // Ordner mit den erwarteten Testergebnissen

        PrivateKey key = new PrivateKey(BI(50021), BI(100003), BI(65537));

        // In Datei schreiben selfmade
        {
            BigInteger[] test = { new BigInteger("123456789"), new BigInteger("987654321") };
            FileIO.writeBigIntegers(test, 10, results + "writeTestOne.txt");
            Assert.assertTrue(
                    Arrays.equals(FileIO.readBigIntegers(10, results + "writeTestOne.txt"), test));
            System.out.print("Hello");
        }

        {
            // Tests für encrypt, decrypt, createSignature und checkSignature (aus FileIO)

            FileIO.encrypt(key.getPublicKey(), expected + "hallowelt.txt",
                    results + "hallowelt.txt.enc");
            Assert.assertTrue("encrypt: hallowelt.txt", TestToolkit
                    .filesAreEqual(expected + "hallowelt.txt.enc", results + "hallowelt.txt.enc"));
            FileIO.decrypt(key, expected + "hallowelt.txt.enc", results + "hallowelt.txt");
            Assert.assertTrue("decrypt: hallowelt.txt", TestToolkit
                    .filesAreEqual(expected + "hallowelt.txt", results + "hallowelt.txt"));

            FileIO.createSignature(key, results + "hallowelt.txt", results + "hallowelt.txt.sig");
            Assert.assertTrue("createSignature: hallowelt.txt", TestToolkit
                    .filesAreEqual(expected + "hallowelt.txt.sig", results + "hallowelt.txt.sig"));
            Assert.assertTrue("checkSignature: hallowelt.txt",
                    FileIO.checkSignature(key.getPublicKey(), expected + "hallowelt.txt",
                            expected + "hallowelt.txt.sig"));
        }

        {
            // Tests für write-/readPublicKey und write-/readPrivateKey

            FileIO.writePublicKey(key.getPublicKey(), results + "example.pub");
            Assert.assertTrue("writePublicKey (Beispielschlüssel)",
                    TestToolkit.filesAreEqual(expected + "example.pub", results + "example.pub"));
            FileIO.writePrivateKey(key, results + "example.key");
            Assert.assertTrue("writePrivateKey (Beispielschlüssel)",
                    TestToolkit.filesAreEqual(expected + "example.key", results + "example.key"));

            Assert.assertEquals("readPublicKey (Beispielschlüssel)", key.getPublicKey(),
                    FileIO.readPublicKey(expected + "example.pub"));
            Assert.assertEquals("readPrivateKey (Beispielschlüssel)", key,
                    FileIO.readPrivateKey(key.getPublicKey(), results + "example.key"));
        }

    }

}
