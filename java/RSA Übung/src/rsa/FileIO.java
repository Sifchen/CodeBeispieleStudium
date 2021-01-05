package rsa;

import java.math.BigInteger;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.nio.file.Files;

/**
 * Stellt Methoden zur Dateiarbeit in Verbindung mit den Klassen PrivateKey und
 * PublicKey zur Verfügung.
 *
 * @author kar(Übungsleiter), mhe(Übungsleiter), Daniel Pigotow (minf103095)
 *
 */
public class FileIO {

    /**
     * Wert für den Exponenten e des öffentlichen Schlüssels (vierte Fermatsche Zahl
     * 2^16+1)
     */
    public static final long DEFAULT_E = 65537;

    /**
     * Liest ein Byte-Array aus einer Datei.
     *
     * @param filename Name der Datei, aus der gelesen werden soll
     * @return Byte-Array mit allen in filename enthaltenen Bytes
     * @throws IOException Fehler, die beim Lesen auftreten
     * @pre filename != null
     */
    public static byte[] readBytes(String filename) throws IOException {
        assert filename != null;

        return Files.readAllBytes(Paths.get(filename));
    }

    /**
     * Schreibt ein Byte-Array in eine Datei.
     *
     * @param bytes    zu schreibende Bytes
     * @param filename Name der Datei, in die geschrieben werden soll
     * @throws IOException Fehler, die beim Schreiben auftreten
     * @pre bytes != null
     * @pre filename != null
     */
    public static void writeBytes(byte[] bytes, String filename) throws IOException {
        assert bytes != null;
        assert filename != null;

        Files.write(Paths.get(filename), bytes);
    }

    /**
     * Liest ein BigInteger-Array aus einer Datei.
     *
     * @param len      Länge eines jeweils zu lesenden BigInteger-Wertes in Byte
     * @param filename Name der Datei, aus der gelesen werden soll
     * @return Array mit allen in der angegebenen Datei enthaltenen Zahlen
     * @throws IOException Fehler, die beim Lesen auftreten
     * @pre len &gt; 0
     * @pre filename != null
     * @pre Die Anzahl der BigInteger stimmt mit der laengenangabe überein
     */
    public static BigInteger[] readBigIntegers(int len, String filename) throws IOException {
        assert filename != null;
        assert len > 0;

        byte[] readArr = readBytes(filename); // bytes aus Datei

        // Damit auch wirklich genau die Richtige Anzahl an BigInts in der Datei stehen
        assert (readArr.length % len) == 0;

        byte[] creationArr = new byte[len]; // Array für BigInteger Konstruktor
        BigInteger[] retArr = new BigInteger[readArr.length / len];
        int creationCounter = 0; // indexwert für retArr

        for (int i = 0; i < readArr.length; i++) {
            // modulo len zum nicht überschreiten der länge
            creationArr[i % len] = readArr[i];
            // bei befüllen des creationArr wird ein neuer BigInteger erstellt
            /*
             * Wenn alle bytes die ein BigInt repraesentieren abgelaufen sind dann wird der
             * BigInt erzeugt
             */
            if ((i % len == len - 1)) {
                retArr[creationCounter++] = new BigInteger(creationArr);
            }
        }
        return retArr;
    }

    /**
     * Erstellt die Byte-Repräsentation eines BigInteger-Wertes. Das Array ist bei
     * Bedarf vorne mit Nullen aufgefüllt, sodass es eine vordefinierte Länge hat.
     *
     * @param b   Zahl, deren Byte-Repräsentation erstellt werden soll
     * @param len Länge (Anzahl der Bytes), die das Ergebnis haben soll
     * @return Byte-Repräsentation von b
     * @pre b != null
     * @pre b kann durch die angegebene Anzahl an Bytes dargestellt werden (die
     *      Länge der Byte-Repräsentation von b ist kleiner/gleich len)
     * @pre len &gt; 0
     */
    public static byte[] bigIntegerToBytes(BigInteger b, int len) {
        assert b != null;
        assert (b.toByteArray().length) <= len;
        assert len > 0;

        byte[] byteArr = b.toByteArray(); // BigInteger der jetzt als byte Array dargestellt wird.
        byte[] returnArr = new byte[len];

        if (byteArr.length < len) {
            System.arraycopy(byteArr, 0, returnArr, (len - byteArr.length), byteArr.length);
        } else {
            returnArr = byteArr;
        }
        return returnArr;
    }

    /**
     * Schreibt ein aus einem Array von BigInteger-Werten erzeugtes Byte-Array in
     * die angegebene Datei.
     *
     * @param bs       zu schreibene BigInteger-Werte
     * @param len      Länge jeweils eines BigInteger-Wertes in Byte
     * @param filename Name der Datei, in die geschrieben werden soll
     * @throws IOException Fehler, die beim Schreiben auftreten
     * @pre bs != null
     * @pre filename != null
     * @pre len &gt; 0
     * @pre Für jedes Element b aus bs gilt: b kann durch die angegebene Anzahl an
     *      Bytes dargestellt werden (die Länge der Byte-Repräsentation von b ist
     *      kleiner/gleich len)
     */
    public static void writeBigIntegers(BigInteger[] bs, int len, String filename)
            throws IOException {
        assert bs != null;
        assert filename != null;
        assert len > 0;
        for (int i = 0; i < bs.length; i++) {
            assert bs[i].toByteArray().length <= len;
        }

        try (OutputStream stream = Files.newOutputStream(Paths.get(filename))) {
            for (int i = 0; i < bs.length; i++) {
                byte[] temp = bigIntegerToBytes(bs[i], len);
                stream.write(temp);
            }
        }

    }

    /**
     * Erzeugt das Chiffrat einer Datei und schreibt es in die angegebene Datei.
     *
     * @param publicKey         zu verwendender öffentlicher Schlüssel
     * @param plaintextFilename Name der zu verschlüsselnden Datei
     * @param encryptedFilename Name der zu erzeugenden Datei
     * @throws IOException Fehler, die beim Lesen oder Schreiben auftreten
     * @pre publicKey != null
     * @pre plaintextFilename != null
     * @pre encryptedFilename != null
     */
    public static void encrypt(PublicKey publicKey, String plaintextFilename,
            String encryptedFilename) throws IOException {
        assert publicKey != null;
        assert plaintextFilename != null;
        assert encryptedFilename != null;

        byte[] in = readBytes(plaintextFilename);

        BigInteger[] c = publicKey.encrypt(in);
        writeBigIntegers(c, publicKey.getN().toByteArray().length, encryptedFilename);
    }

    /**
     * Entschlüsselt eine Datei und liefert das Ergebnis.
     *
     * @param privateKey        zu verwendender privater Schlüssel
     * @param encryptedFilename Name der zu verschlüsselnden Datei
     * @param plaintextFilename Name der zu erzeugenden Datei
     * @throws IOException Fehler, die beim Lesen oder Schreiben auftreten
     * @pre privateKey != null
     * @pre plaintextFilename != null
     * @pre encryptedFilename != null
     */
    public static void decrypt(PrivateKey privateKey, String encryptedFilename,
            String plaintextFilename) throws IOException {
        assert privateKey != null;
        assert plaintextFilename != null;
        assert encryptedFilename != null;

        BigInteger[] c = readBigIntegers(privateKey.getN().toByteArray().length, encryptedFilename);
        writeBytes(privateKey.decrypt(c), plaintextFilename);
    }

    /**
     * Erzeugt die Signatur einer Datei.
     *
     * @param privateKey        zu verwendender privater Schlüssel
     * @param plaintextFilename Name der zu signierenden Datei
     * @param signatureFilename Name der zu erzeugenden Datei
     * @throws IOException Fehler, die beim Lesen oder Schreiben auftreten
     * @pre privateKey != null
     * @pre plaintextFilename != null
     * @pre signatureFilename != null
     */
    public static void createSignature(PrivateKey privateKey, String plaintextFilename,
            String signatureFilename) throws IOException {
        assert privateKey != null;
        assert plaintextFilename != null;
        assert signatureFilename != null;

        byte[] plain = readBytes(plaintextFilename);
        BigInteger signature = privateKey.hashAndSign(plain);
        writeBytes(bigIntegerToBytes(signature, privateKey.getN().toByteArray().length)
                , signatureFilename);
    }

    /**
     * Überprüft, ob die übergebene Signatur zu der übergebenen Datei gehört.
     *
     * @param publicKey         zu verwendender öffentlicher Schlüssel
     * @param plaintextFilename Name der Datei, deren Signatur überprüft werden soll
     * @param signatureFilename Name der Datei mit der zu überprüfenden Signatur
     * @return true, wenn die Signatur stimmt, ansonsten false
     * @throws IOException Fehler, die beim Lesen oder Schreiben auftreten
     * @pre publicKey != null
     * @pre plaintextFilename != null
     * @pre signatureFilename != null
     */
    public static boolean checkSignature(PublicKey publicKey, String plaintextFilename,
            String signatureFilename) throws IOException {
        assert publicKey != null;
        assert plaintextFilename != null;
        assert signatureFilename != null;

        byte[] in = readBytes(plaintextFilename);
        BigInteger signature = new BigInteger(readBytes(signatureFilename));
        return publicKey.checkSignature(in, signature);
    }

    /**
     * Liest den öffentlichen Schlüssel aus einer Datei ein. Der Exponent e ist für
     * alle eingelesenen Schlüssel gleich der vierten Fermatschen Zahl (2^16+1 =
     * 65537). Der Dateiinhalt ist lediglich das RSA-Modul N.
     *
     * @param filename Name der Datei, aus der der RSA-Modul N gelesen werden soll
     * @return eingelesener Schlüssel
     * @throws IOException IOException Fehler, die beim Lesen auftreten
     * @pre filename != null
     */
    public static PublicKey readPublicKey(String filename) throws IOException {
        assert filename != null;
        byte[] arr = readBytes(filename);
        BigInteger fileN = new BigInteger(arr);
        return new PublicKey(fileN, BigInteger.valueOf(DEFAULT_E));
    }

    /**
     * Schreibt einen öffentlichen Schlüssel in eine Datei. Der Exponent e des
     * übergebenen Schlüssels muss gleich der vierten Fermatschen Zahl (2^16+1 =
     * 65537) sein. Der Dateiinhalt ist lediglich das RSA-Modul N.
     *
     * @param publicKey zu schreibender Schlüssel
     * @param filename  Name der Datei, in die der RSA-Modul N geschrieben werden
     *                  soll
     * @throws IOException Fehler, die beim Schreiben auftreten
     * @pre publicKey != null
     * @pre filename != null
     * @pre Der Exponent e von publicKey muss gleich 65537 sein.
     */
    public static void writePublicKey(PublicKey publicKey, String filename) throws IOException {
        assert publicKey != null;
        assert filename != null;
        assert publicKey.getE().equals(BigInteger.valueOf(DEFAULT_E));
        byte[] arr = bigIntegerToBytes(publicKey.getN(), publicKey.getN().toByteArray().length);
        writeBytes(arr, filename);
    }

    /**
     * Liest den privaten Schlüssel aus einer Datei ein. Der RSA-Modul N entspricht
     * dem des übergebenen öffentlichen Schlüssels. Der Dateiinhalt ist lediglich
     * der Exponent d.
     *
     * @param publicKey zu nutzender öffentlicher Schlüssel
     * @param filename  Name der Datei, aus der der Exponent d gelesen werden soll
     * @return eingelesener Schlüssel
     * @throws IOException IOException Fehler, die beim Lesen auftreten
     * @pre publicKey != null
     * @pre filename != null
     * @pre Der Exponent e von publicKey muss gleich 65537 sein.
     */
    public static PrivateKey readPrivateKey(PublicKey publicKey, String filename)
            throws IOException {
        assert publicKey != null;
        assert filename != null;
        assert publicKey.getE().equals(BigInteger.valueOf(DEFAULT_E));

        byte[] arr = readBytes(filename);
        BigInteger fileD = new BigInteger(arr);

        return new PrivateKey(publicKey, fileD);
    }

    /**
     * Schreibt einen privaten Schlüssel in eine Datei. Der zugehörige öffentliche
     * Schlüssel sowie der RSA-Modul N werden hierbei ignoriert. Der Dateiinhalt ist
     * lediglich der Exponent d.
     *
     * @param privateKey zu schreibender Schlüssel
     * @param filename   Name der Datei, in die der Exponent d geschrieben werden
     *                   soll
     * @throws IOException Fehler, die beim Schreiben auftreten
     * @pre privateKey != null
     * @pre filename != null
     * @pre Der Exponent e des zugehörigen öffentlichen Schlüssels muss gleich 65537
     *      sein.
     */
    public static void writePrivateKey(PrivateKey privateKey, String filename) throws IOException {
        assert privateKey != null;
        assert filename != null;
        assert privateKey.getPublicKey().getE().equals(BigInteger.valueOf(DEFAULT_E));

        byte[] d = bigIntegerToBytes(privateKey.getD(), privateKey.getN().toByteArray().length);
        writeBytes(d, filename);
    }

}
