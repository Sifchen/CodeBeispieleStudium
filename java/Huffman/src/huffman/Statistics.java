package huffman;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Eine Statistik, die für alle 256 möglichen Symbole deren Häufigkeit enthält. Die Einträge sind
 * aufsteigend nach dem Ordinalwert des Symbols sortiert.
 * 
 * @author kar, mhe, Daniel Pigotow
 */
public class Statistics {

    public static final int LENGTH = 256;
    private long[] stat = new long[LENGTH];

    /**
     * Erstellt eine Statistik aus den übergebenen Bytes.
     * 
     * @param input Bytes, aus welchen die Statistik erstellt werden soll, darf nicht null sein.
     * @return Die Statistik mit 256 Einträgen
     */
    public static Statistics analyse(byte[] input) {
        if (input == null) {
            throw new IllegalArgumentException("input darf nicht null sein");
        }

        Statistics result = new Statistics();
        for (int i = 0; i < input.length; i++) {
            result.stat[Byte.toUnsignedInt(input[i])]++;
        }

        return result;
    }

    /**
     * Liest die Statistik aus dem übergebenen Datenstrom und gibt sie zurück.
     * 
     * Die Statistik besteht aus 256 Long-Werten.
     * 
     * @param input Der zu nutzende Datenstrom, darf nicht null sein. Steht zu Beginn der Methode
     *            auf dem ersten Statistikeintrag und nach ihrem Ende auf dem ersten Byte nach dem
     *            letzten Statistikeintrag.
     * @return Die gelesene Statistik
     * @throws IOException Fehler beim Lesen
     */
    public static Statistics readFromFile(DataInput input) throws IOException {
        if (input == null) {
            throw new IllegalArgumentException("input darf nicht null sein");
        }
        Statistics result = new Statistics();
        for (int i = 0; i < LENGTH; i++) {
            result.stat[i] = input.readLong();
        }
        return result;
    }

    /**
     * Schreibt die Statistik in den übergebenen Datenstrom.
     * 
     * @param output Der zu nutzende Datenstrom, darf nicht null sein
     * @throws IOException Fehler beim Schreiben
     */
    public void writeToFile(DataOutput output) throws IOException {
        if (output == null) {
            throw new IllegalArgumentException("output darf nicht null sein");
        }
        for (int i = 0; i < LENGTH; i++) {
            output.writeLong(this.stat[i]);
        }
    }

    /**
     * Erstellt einen {@link Forest} aus dieser Statistik. Der Forest enthält nur einelementige
     * Bäume (Blätter) für Bytes, deren Statistikeintrag mindestens 1 ist.
     * 
     * @return Der Wald mit einelementigen Bäumen
     */
    public Forest toForest() {
        Forest result = new Forest();
        Tree toAdd;
        for (int i = 0; i < LENGTH; i++) {
            if (this.stat[i] > 0) {
                toAdd = Tree.fromByte(i, this.stat[i]);
                result.insert(toAdd);
            }
        }
        return result;
    }

    /**
     * Gibt den Statistikeintrag an der übergebenen Position zurück.
     * 
     * @param pos Position des Eintrags, muss zwischen 0 und einschließlich 255 liegen
     * @return Der Statistikeintrag
     */
    public long getValue(int pos) {
        if (pos < 0 || pos > LENGTH) {
            throw new IllegalArgumentException("pos muss zwischen 0 und einschließlich 255 sein");
        }
        return this.stat[pos];
    }

    /**
     * Liefert die Summe der Statistikwerte zurück. Überläufe des Datentyps long werden hierbei
     * ignoriert.
     * 
     * @return Die Summe der Statistikwerte
     */
    public long sum() {
        long result = 0;
        for (int i = 0; i < LENGTH; i++) {
            result += this.stat[i];
        }
        return result;
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < stat.length; i++) {
            result += " [" + i + "]:" + stat[i];
        }
        return result;
    }

}
