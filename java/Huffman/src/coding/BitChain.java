package coding;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Eine Kette von aneinandergereihten {@link Bit}s.
 * 
 * @author kar, mhe, Daniel Pigotow
 *
 */
public class BitChain {

    private final Queue<Bit> chain = new LinkedList<>();

    /**
     * Hängt das übergebene Bit hinten an diese Kette an.
     * 
     * @param bit Das anzuhängende Bit
     */
    public void append(Bit bit) {
        chain.add(bit);
    }

    /**
     * Hängt die Bits der übergebenen BitChain hinten an diese Kette an.
     * 
     * @param other Die BitChain mit den anzuhängenden Bits, darf nicht null sein, wird nicht
     *            verändert
     */
    public void append(BitChain other) {
        if (other == null) {
            throw new IllegalArgumentException("other darf nicht null sein");
        }
        chain.addAll(other.chain);
    }

    /**
     * Erzeugt eine Kopie des aktuellen Objekts, welche die gleichen Bits enthält. Änderungen der
     * Kopie dürfen keine Auswirkung auf das aktuelle Objekt haben.
     * 
     * @return Kopie des aktuellen Objekts
     */
    public BitChain copy() {
        BitChain result = new BitChain();
        result.append(this);
        return result;
    }

    /**
     * Gibt die Länge dieser BitChain zurück.
     * 
     * @return Länge, also die Anzahl der Bits in dieser Kette
     */
    public int length() {
        return this.chain.size();
    }

    /**
     * Schreibt die Bits dieser BitChain byteweise in den übergebenen Datenstrom. Bits, die nicht
     * mehr in ein Byte passen (also weniger als 8), verbleiben in dieser BitChain.
     * 
     * Diese Methode muss zwingend Bitoperationen zum Zusammenbauen eines Bytes verwenden.
     *
     * @param output Zu nutzender Datenstrom, darf nicht null sein
     * @throws IOException Fehler beim Schreiben
     */
    public void writeTo(OutputStream output) throws IOException {
        if (output == null) {
            throw new IllegalArgumentException("output darf nicht null sein");
        }

        byte currBit = 0;
        byte toAdd = 0;
        while (this.length() > Byte.SIZE - 1) {
            toAdd = 0;
            for (int i = 0; i < Byte.SIZE; i++) {
                currBit = (byte) this.chain.poll().getValue();
                toAdd <<= 1;
                toAdd |= currBit;
            }
            output.write(toAdd);
        }
    }

    // Diese Methoden sind Hilfsmethoden zum Testen und dürfen auch nur in Tests
    // verwendet werden:

    /**
     * Konvertiert diese BitChain in ein Array. Die Reihenfolge der enthaltenen Bits wird
     * beibehalten.
     *
     * @return Das der BitChain entsprechende Array
     */
    public Bit[] toArray() {
        Bit[] result = new Bit[this.length()];
        this.chain.toArray(result);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof BitChain)) {
            return false;
        }
        BitChain other = (BitChain) obj;

        // Um in dieser Klasse die konkrete Implementierung nicht vorzuschreiben,
        // wird der ineffiziente Aufruf der Methode toArray() genutzt:
        return Arrays.deepEquals(this.toArray(), other.toArray());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(toArray());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Um in dieser Klasse die konkrete Implementierung nicht vorzuschreiben,
        // wird der ineffiziente Aufruf der Methode toArray() genutzt:
        for (Bit b : this.toArray()) {
            sb.append(b);
        }

        return sb.toString();
    }

}
