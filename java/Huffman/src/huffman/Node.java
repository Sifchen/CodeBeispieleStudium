package huffman;

import java.util.Iterator;
import coding.Bit;
import coding.BitChain;

/**
 * Die Klasse Node, welche immer genau 2 Kindelemente beinhaltet (Leafs oder Nodes) und eine Angabe
 * über die Anzahl der darunter liegenden Symbole beinhaltet.
 * 
 * @author Daniel Pigotow
 *
 */

class Node extends Tree {

    private long occs;

    private Tree left; // 0
    private Tree right; // 1

    /**
     * Konstruktor fuer eine neue Node
     * 
     * @param occs vorkommen der Symbole im gesamten Baum
     * @param left der linke kindsknoten
     * @param right der rechte kindsknoten
     */
    Node(long occs, Tree left, Tree right) {
        this.occs = occs;
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int getByte() {
        throw new IllegalArgumentException();
    }

    @Override
    public long getCount() {
        return this.occs;
    }

    @Override
    public void toCodetable(BitChain bitChain, BitChain[] codetable) {
        BitChain bitChainCopy = bitChain.copy();
        bitChainCopy.append(Bit.ZERO);
        this.left.toCodetable(bitChainCopy, codetable);
        bitChain.append(Bit.ONE);
        this.right.toCodetable(bitChain, codetable);
    }

    @Override
    protected void toGraphviz(StringBuilder sb, String prefix) {
        sb.append(prefix).append(" [label=\"(").append(this.occs).append(")\"]\n");
        sb.append(prefix).append(" -> ").append(prefix + "0").append(" [label=\"0\"]\n");
        sb.append(prefix).append(" -> ").append(prefix + "1").append(" [label=\"1\"]\n");
        this.left.toGraphviz(sb, prefix + "0");
        this.right.toGraphviz(sb, prefix + "1");
    }

    /**
     * toString Hilfsmethode zum besseren Debuggen
     * 
     * @return die Stringdarstellung
     */
    public String toString() {
        return "[" + this.occs + ", " + this.left.toString() + ", " + this.right.toString() + "]";
    }

    @Override
    protected int translateByte(Iterator<Bit> iterator) {
        if (iterator.hasNext()) {
            Bit bit = iterator.next();
            switch (bit) {
                case ZERO:
                    return this.left.translateByte(iterator);

                case ONE:
                    return this.right.translateByte(iterator);

                default:
                    throw new IllegalStateException("Can never happen");
            }
        } else {
            throw new IllegalStateException("Iterator ist zuende obwohl nicht im leaf");

        }
    }

}
