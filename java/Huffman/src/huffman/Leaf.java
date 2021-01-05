package huffman;

import java.util.Iterator;
import coding.Bit;
import coding.BitChain;

/**
 * Die Klasse Leaf, welche ein Blatt des Baums darstellt und das Symbol und seine Häufigkeit im Text
 * enthält.
 * 
 * @author Daniel Pigotow
 *
 */

class Leaf extends Tree {
    private int symbol;
    private long occs;

    /**
     * Konstruktor fuer eine Blatt eines Baumes
     * 
     * @param symbol das Symbol des Blattes
     * @param occs die häufigkeiten des Blattes
     */
    Leaf(int symbol, long occs) {
        this.symbol = symbol;
        this.occs = occs;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int getByte() {
        return this.symbol;
    }

    @Override
    public long getCount() {
        return this.occs;
    }

    @Override
    public void toCodetable(BitChain bitChain, BitChain[] codetable) {
        codetable[this.symbol] = bitChain;
    }

    @Override
    protected void toGraphviz(StringBuilder sb, String prefix) {
        sb.append(prefix).append(" [label=\"").append(byteToLabel(this.symbol)).append(" (")
                .append(this.occs).append(")\"]\n");

    }

    /**
     * toString Hilfsmethode zum besseren Debuggen
     * 
     * @return die Stringdarstellung
     */
    public String toString() {
        return "{" + this.symbol + ", " + this.occs + "}";
    }

    @Override
    protected int translateByte(Iterator<Bit> iterator) {
        return this.symbol;
    }

}
