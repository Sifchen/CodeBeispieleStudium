package huffman;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import coding.Bit;
import coding.BitChain;
import coding.BitIterator;

/**
 * Ein Huffman-Baum.
 * 
 * @author kar, mhe, Daniel Pigotow
 */
public abstract class Tree {

    /**
     * Gibt einen leeren Baum zurück.
     * 
     * @return Ein leerer Baum
     */
    public static Tree empty() {
        return new Empty();
    }

    /**
     * Erzeugt einen Baum, der nur das übergebene Byte enthält.
     * 
     * @param b Das zu nutzende Byte, muss zwischen 0 und 255 sein
     * @param count Die Anzahl von b, muss mindestens 1 sein
     * @return Der neue Baum
     */
    public static Tree fromByte(int b, long count) {
        if (count < 1) {
            throw new IllegalArgumentException("count muss mindestens 1 sein");
        }
        if ((b < 0) || (b >= Statistics.LENGTH)) {
            throw new IllegalArgumentException(
                    "Das zu nutzende Byte, muss zwischen 0 und 255 sein");
        }
        return new Leaf(b, count);
    }

    /**
     * Erzeugt einen neuen Baum(knoten) mit den zwei übergebenen Bäumen als Unterbäume.
     * 
     * @param l Der linke Unterbaum, darf nicht null sein
     * @param r Der rechte Unterbaum, darf nicht null sein
     * @return Der Baum, der den neu erzeugten Baum(knoten) als Wurzel hat
     */
    public static Tree merge(Tree l, Tree r) {
        if (l == null) {
            throw new IllegalArgumentException("linker Baum darf nicht null sein");
        }
        if (r == null) {
            throw new IllegalArgumentException("rechter Baum darf nicht null sein");
        }
        return new Node(l.getCount() + r.getCount(), l, r);
    }

    /**
     * Gibt zurück, ob der Baum leer ist.
     * 
     * @return true, falls der Baum leer ist, sonst false
     */
    public abstract boolean isEmpty();

    /**
     * Wenn der Baum ein Blatt ist, wird das darin genutzte Byte zurückgegeben.
     * 
     * @return Das Byte des Blatts
     * @throws IllegalArgumentException wenn der Baum kein Blatt ist
     */
    public abstract int getByte();

    /**
     * Gibt die Gesamtanzahl der repräsentierten Bytes des Baums zurück.
     * 
     * @return Die Gesamtanzahl der repräsentierten Bytes des Baums
     */
    public abstract long getCount();

    /**
     * Erstellt eine Kodierungstabelle aus den im Baum enthaltenen Bytes. Das resultierende Array
     * enthält 256 Plätze für die möglichen Bytes, die aufsteigend nach ihrem Wert sortiert sind.
     * Jeder dieser Plätze verweist auf eine {@link BitChain}, welche die Kodierung für das
     * entsprechende Byte enthält.
     * 
     * @return Die Kodierungstabelle ist mit leeren BitChains gefuellt
     */
    public BitChain[] toCodetable() {
        BitChain[] codetable = new BitChain[Statistics.LENGTH];
        // Das Array wird initial mit leeren BitChains gefuellt
        for (int i = 0; i < codetable.length; i++) {
            codetable[i] = new BitChain();
        }
        BitChain bitChain = new BitChain();
        this.toCodetable(bitChain, codetable);

        return codetable;
    }

    /**
     * Hilfsmethode, welche rekursiv aufgerufen wird, um den Baum abzulaufen und einem Zeichen eine
     * BitChain zuzuordnen um somit eine Kodierungstabelle zu erstellen.
     * 
     * @param bitChain die sich aufbauende BitChain
     * @param codetable die Kodierungstabelle welche sukzessiv befuellt wird
     */
    protected abstract void toCodetable(BitChain bitChain, BitChain[] codetable);

    /**
     * Gibt die Graphviz-Repäsentation des Baums als String zurück.
     * 
     * @return Die Graphviz-Repäsentation des Baums
     */
    public String toGraphviz() {
        final StringBuilder sb = new StringBuilder("digraph G {\n");
        this.toGraphviz(sb, "_");
        sb.append("}\n");

        return sb.toString();
    }

    /**
     * Schreibt die Graphviz-Repräsentation des Baums in den übergebenen StringBuilder.
     * 
     * @param sb Der StringBuilder, in den die Repräsentation des Baum geschrieben werden soll
     * @param prefix Das Prefix für das entsprechende Element des Baums
     * @pre sb != null
     * @pre prefix != null
     */
    protected abstract void toGraphviz(StringBuilder sb, String prefix);

    /**
     * Gibt das in der Graphviz-Repräsentation zu nutzende Label für ein übergebenes Byte zurück.
     * 
     * Für druckbare Zeichen ist dies das Zeichen selbst (wenn nötig an die Graphviz-Ausgabe
     * angepasst), für andere Werte der Index.
     * 
     * @param b Das umzuwandelnde Zeichen
     * @return Das zu nutzende Label
     */
    protected static String byteToLabel(int b) {
        final int start = 0x20, end = 0x7E;

        if ((char) b == '"') {
            return "'\\\"'"; // doppeltes Anführungszeichen maskieren
        } else if ((char) b == '\\') {
            return "'\\\\'"; // Backslash maskieren
        } else if (b >= start && b <= end) {
            return "'" + (char) b + "'"; // druckbares Zeichen
        } else {
            return "" + b; // Index des Zeichens
        }
    }

    /**
     * Traversiert den Baum anhand der Bitsequenz aus dem übergebenen {@link BitIterator} und
     * schreibt gefundene Bytes in den übergebenen OutputStream. Es werden 'numberOfBytes' Bytes
     * geschrieben.
     * 
     * @param iterator Iterator, der die Bitsequenz enthält, darf nicht null sein
     * @param numberOfBytes zu schreibende Anzahl an Bytes, muss &ge; 0 sein
     * @param destination Zielstrom, darf nicht null sein
     * @throws IOException Fehler beim Lesen oder Schreiben der Dateien
     * 
     */
    public void translate(Iterator<Bit> iterator, long numberOfBytes, OutputStream destination)
            throws IOException {
        assert (numberOfBytes >= 0);
        for (long i = 0; i < numberOfBytes; i++) {
            int symbol = translateByte(iterator);
            destination.write(symbol);
        }
    }

    /**
     * Hilfsmethode die dabei hilft den Baum anhand des BitIterators zu traversieren. Diese liefert
     * das Symbol welches sich aus dem uebergebenen Iterator ergibt
     * 
     * @param iterator der Itarator der die Bits liefert
     * @return der dekodierte Wert
     */

    protected abstract int translateByte(Iterator<Bit> iterator);

}
